package dev.anhcraft.yumn.generators.overworld;

import dev.anhcraft.jvmkit.utils.ArrayUtil;
import dev.anhcraft.yumn.Yumn;
import dev.anhcraft.yumn.biomes.BiomeManager;
import dev.anhcraft.yumn.biomes.YumnBiome;
import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.generators.YumnGenerator;
import dev.anhcraft.yumn.heightmaps.*;
import dev.anhcraft.yumn.heightmaps.renovation.BiomeEdgeMarker;
import dev.anhcraft.yumn.heightmaps.renovation.CliffMarker;
import dev.anhcraft.yumn.heightmaps.renovation.Interpolation;
import dev.anhcraft.yumn.heightmaps.renovation.MessFixer;
import dev.anhcraft.yumn.populators.OverworldPopulator;
import dev.anhcraft.yumn.populators.overworld.*;
import dev.anhcraft.yumn.utils.*;
import dev.anhcraft.yumn.utils.noise.DummySimplexNoise;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import dev.anhcraft.yumn.utils.noise.SimplexOctaveNoise;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class OverworldGenerator extends YumnGenerator {
    public static final Logger LOGGER = new Logger("OverworldGenerator");
    public static final HeightMapManager CACHED_HEIGHT_MAPS = new HeightMapManager();

    public final int OCEAN_SURFACE_LEVEL = 40;
    public final double MID_WATER_LEVEL = 65;
    public final int WATER_LEVEL = 78;
    public final int LAND_LEVEL = WATER_LEVEL + 1;
    public final int LAND_HEIGHT_T1 = 3;
    public final int LAND_HEIGHT_T2 = 3;
    public final int LAND_HEIGHT_T3 = 5;

    public final int CONTINENT_NOISE_SCALE = 750;
    public final int TERRAIN_NOISE_SCALE = 425;
    public final int BIOME_PRECIPITATION_NOISE_SCALE = 300;
    public final int BIOME_CHANCE_NOISE_SCALE = 300;

    public OverworldGenerator() {
        super(World.Environment.NORMAL);
    }

    @Override
    public int getMaxNaturalHeight() {
        return WATER_LEVEL + 45;
    }

    @Override
    @NotNull
    public List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        OverworldGenerator gen = this;
        return ArrayUtil.toList(new BlockPopulator[]{
                new BlockPopulator() {
                    private final OverworldPopulator[] POPULATORS = new OverworldPopulator[]{
                            new CavePopulator(gen),
                            new RavinePopulator(gen),
                            new FloraPopulator(gen),
                            new OceanFloraPopulator(gen),
                            new RockPopulator(gen),
                            new CampfirePopulator(gen)
                    };

                    @Override
                    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
                        XoRoShiRo128StarStarRandomGenerator randomizer = new XoRoShiRo128StarStarRandomGenerator(RandomUtil.getChunkSeed(world.getSeed(), chunk.getX(), chunk.getZ()));
                        Yumn.getInstance().pool.schedule(() -> {
                            for(OverworldPopulator populator : POPULATORS) {
                                populator.populate(world, randomizer, chunk);
                            }
                            LOGGER.log("Populated chunk at %s %s successfully!", chunk.getX(), chunk.getZ());
                        }, 0, TimeUnit.MILLISECONDS);
                    }
                }
        });
    }

    public List<HeightMapProcessor<?>> getHeightMapProcessors(ChunkHeightMap originChunk, Context<?> context, BiFunction<Integer, Integer, ChunkHeightMap> heightMapProvider) {
        return ArrayUtil.toList(new HeightMapProcessor<?>[]{
                new BiomeEdgeMarker(originChunk, context, heightMapProvider),
                new Interpolation(originChunk, context, heightMapProvider),
                new MessFixer(originChunk, context, heightMapProvider),
                new CliffMarker(originChunk, context, heightMapProvider)
        });
    }

    @NotNull
    private DoublePair[][] generatePreHeightMap(long seed, int chunkX, int chunkZ) {
        SimplexOctaveNoise gen1 = new SimplexOctaveNoise(seed)
                .setScale(CONTINENT_NOISE_SCALE)
                .setOctaves(3)
                .setLacunarity(1.5)
                .setPersistence(3);
        SimplexOctaveNoise gen2 = new SimplexOctaveNoise(-seed)
                .setOctaves(2)
                .setLacunarity(1.5)
                .setPersistence(3)
                .setScale(TERRAIN_NOISE_SCALE);
        DoublePair[][] heightmap = new DoublePair[16][16];
        double min = getNoise(OCEAN_SURFACE_LEVEL);
        double buff = 1 - min;
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                int rx = (chunkX << 4) + x;
                int rz = (chunkZ << 4) + z;
                double u = gen1.noise(rx, rz) * buff + min;
                double v = gen2.noise(rx, rz) * buff + min;
                heightmap[x][z] = new DoublePair(u, v);
            }
        }
        return heightmap;
    }

    private double f1(double v){
        return 1 / (1 + Math.pow(Math.E, -v));
    }

    private double f2(double v){
        return Math.sin(Math.pow(Math.E, v) * v * 0.3);
    }

    @NotNull
    private ChunkHeightMap generateHeightMap(@NotNull OverworldContext context, boolean soft){
        SimplexOctaveNoise precipitationGen = new SimplexOctaveNoise(context.getSeed() >> 4)
                .setOctaves(2)
                .setLacunarity(1.5)
                .setPersistence(3)
                .setScale(BIOME_PRECIPITATION_NOISE_SCALE);
        SimplexOctaveNoise bioChanceGen = new SimplexOctaveNoise(-(context.getSeed() >> 4))
                .setOctaves(2)
                .setLacunarity(1.5)
                .setPersistence(3)
                .setScale(BIOME_CHANCE_NOISE_SCALE);
        DoublePair[][] heightMap = generatePreHeightMap(context.getSeed(), context.getChunkX(), context.getChunkZ());
        HeightMapCell[][] heightMapCells = new HeightMapCell[16][16];
        Int2ObjectMap<NoiseProvider> biomeNoises = new Int2ObjectOpenHashMap<>();

        double min = getNoise(OCEAN_SURFACE_LEVEL);

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                int rx = (context.getChunkX() << 4) + x;
                int rz = (context.getChunkZ() << 4) + z;
                DoublePair pair = heightMap[x][z];
                double bp = precipitationGen.noise(rx, rz);
                bp *= f1(bp) + f2(bp);
                double bc = bioChanceGen.noise(rx, rz);
                bc *= f1(bc) + f2(bc);
                YumnBiome<OverworldContext> bio = BiomeManager.getInstance().pickBiome(context, pair, bp, bc);
                NoiseProvider cnp = biomeNoises.compute(bio.hashCode(), (i, n) -> {
                    if(n != null) return n;
                    NoiseProvider ssn = bio.initBiomeNoise(context);
                    return ssn == null ? DummySimplexNoise.INSTANCE : ssn;
                });
                NoiseProvider nsp = cnp instanceof DummySimplexNoise ? null : cnp;
                double terrainNoise = bio.redistribute(context, nsp, x, z, pair.getSecond());
                pair.setSecond(Math.max(min, terrainNoise));
                heightMapCells[x][z] = new HeightMapCell(terrainNoise, pair.getFirst(), bio);
            }
        }

        ChunkHeightMap noiseMap = new ChunkHeightMap(heightMapCells);

        BiFunction<Integer, Integer, ChunkHeightMap> hmp = (x, z) -> soft ? null : requestHeightMap(
                context.getWorld(),
                x, z,
                () -> new OverworldContext(
                        this,
                        context.getWorld(),
                        x, z,
                        context.getSeed(),
                        new XoRoShiRo128StarStarRandomGenerator(RandomUtil.getChunkSeed(context.getSeed(), x, z)),
                        new DummyChunkData(context.getWorld().getMaxHeight())
                ), true
        );
        for (HeightMapProcessor<?> p : getHeightMapProcessors(noiseMap, context, hmp)) {
            noiseMap.forEach(0, 0, x -> {
                p.process(x);
                x.next();
            });
        }
        return noiseMap;
    }

    @NotNull
    public ChunkHeightMap requestHeightMap(@NotNull World world, int chunkX, int chunkZ, Supplier<OverworldContext> contextSupplier, boolean soft){
        WorldHeightMap wm = CACHED_HEIGHT_MAPS.request(world.getUID());
        ChunkHeightMap map = wm.get(chunkX, chunkZ);
        if(map == null){
            map = generateHeightMap(contextSupplier.get(), soft);
            if(!soft) {
                wm.put(chunkX, chunkZ, map);
            }
        }
        return map;
    }

    @Override
    @NotNull
    public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
        ChunkData cd = createChunkData(world);
        long s = world.getSeed(); // correct way!!! (don't get the seed from provided Random)
        OverworldContext ctx = new OverworldContext(this, world, chunkX, chunkZ, s, new XoRoShiRo128StarStarRandomGenerator(RandomUtil.getChunkSeed(s, chunkX, chunkZ)), cd);
        Int2ObjectMap<NoiseProvider> cachedFeatureNoises = new Int2ObjectOpenHashMap<>();
        requestHeightMap(world, chunkX, chunkZ, () -> ctx, false).forEach(0, 0, point2d -> {
            HeightMapCell cell = point2d.get();
            /*
            if(cell.isSharpness()) {
                ctx.newLayerBuilder().at(point2d.getX(), point2d.getY())
                        .to(ctx.getGenerator().getHeight(cell.getTerrainNoise())).is(
                                cell.isBiomeEdge() ? Material.ORANGE_WOOL : Material.RED_WOOL
                );
            } else if(cell.isBiomeEdge()) {
                ctx.newLayerBuilder().at(point2d.getX(), point2d.getY())
                        .to(ctx.getGenerator().getHeight(cell.getTerrainNoise())).is(Material.YELLOW_WOOL);
            } else {
                //noinspection unchecked
                ((YumnBiome<OverworldContext>) cell.getBiome()).generate(ctx, point2d.getX(), point2d.getY(), cell.getTerrainNoise());
            }*/

            // smooth biome edges by mixing block & biome randomly (without ocean biomes)
            if(cell.isBiomeEdge() && !BiomeCollection.AQUATIC.contains(cell.getBiome().getMinecraftBiome()) && !cell.getNearbyBiomes().isEmpty() && random.nextInt(3) == 0) {
                YumnBiome<?> b = cell.getNearbyBiomes().iterator().next();
                if(!BiomeCollection.AQUATIC.contains(b.getMinecraftBiome())) {
                    //noinspection unchecked
                    ((YumnBiome<OverworldContext>) b).generate(ctx, point2d.getX(), point2d.getY(), cell.getTerrainNoise());
                    cell.setBiome(b);
                } else {
                    //noinspection unchecked
                    ((YumnBiome<OverworldContext>) cell.getBiome()).generate(ctx, point2d.getX(), point2d.getY(), cell.getTerrainNoise());
                }
            } else {
                //noinspection unchecked
                ((YumnBiome<OverworldContext>) cell.getBiome()).generate(ctx, point2d.getX(), point2d.getY(), cell.getTerrainNoise());
            }

            if (cell.getBiome().getFeatures() != null) {
                for (YumnFeature<?> feature : cell.getBiome().getFeatures()){
                    //noinspection unchecked
                    YumnFeature<OverworldContext> yf = (YumnFeature<OverworldContext>) feature;
                    NoiseProvider cnp = cachedFeatureNoises.compute(yf.hashCode(), (i, n) -> {
                        if(n != null) return n;
                        NoiseProvider ssn = yf.initNoise(ctx);
                        return ssn == null ? DummySimplexNoise.INSTANCE : ssn;
                    });
                    NoiseProvider nsp = cnp instanceof DummySimplexNoise ? null : cnp;
                    yf.implement(ctx, nsp, point2d.getX(), point2d.getY(), cell);
                }
            }

            for (int y = 0; y < world.getMaxHeight(); y++) {
                biome.setBiome(point2d.getX(), y, point2d.getY(), cell.getBiome().getMinecraftBiome());
                /*Material mt = cd.getType(point2d.getX(), y, point2d.getY());
                if(mt == Material.WATER) {
                    cd.setBlock(point2d.getX(), y, point2d.getY(), Material.AIR);
                }*/
            }
            point2d.next();
        });
        return cd;
    }

    @Override
    public boolean isParallelCapable() {
        return true;
    }
}
