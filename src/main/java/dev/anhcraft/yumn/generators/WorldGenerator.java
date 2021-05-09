package dev.anhcraft.yumn.generators;

import dev.anhcraft.jvmkit.utils.ArrayUtil;
import dev.anhcraft.jvmkit.utils.MathUtil;
import dev.anhcraft.yumn.Yumn;
import dev.anhcraft.yumn.biomes.BiomeManager;
import dev.anhcraft.yumn.biomes.YumnBiome;
import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.heightmaps.*;
import dev.anhcraft.yumn.heightmaps.renovation.Interpolation;
import dev.anhcraft.yumn.heightmaps.renovation.MessFixer;
import dev.anhcraft.yumn.populators.OverworldPopulator;
import dev.anhcraft.yumn.populators.overworld.*;
import dev.anhcraft.yumn.utils.BiomeCollection;
import dev.anhcraft.yumn.utils.DummyChunkData;
import dev.anhcraft.yumn.utils.Logger;
import dev.anhcraft.yumn.utils.RandomUtil;
import dev.anhcraft.yumn.utils.noise.DummyNoiseGenerator;
import dev.anhcraft.yumn.utils.noise.NoiseGenerator;
import dev.anhcraft.yumn.utils.noise.OctaveNoiseGenerator;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class WorldGenerator extends ChunkGenerator {
    public static final Logger LOGGER = new Logger("OverworldGenerator");
    public static final HeightMapManager CACHED_HEIGHT_MAPS = new HeightMapManager();

    public final int OCEAN_SURFACE_LEVEL = 40;
    public final double MID_WATER_LEVEL = 60;
    public final int WATER_LEVEL = 78;
    public final int LAND_LEVEL = WATER_LEVEL + 1;
    public final int LAND_HEIGHT_T1 = 5;
    public final int LAND_HEIGHT_T2 = 6;
    public final int LAND_HEIGHT_T3 = 10;

    public final int NOISE_SCALE = 50;
    public final int BIOME_PRECIPITATION_NOISE_SCALE = 500;
    public final int BIOME_CHANCE_NOISE_SCALE = 500;

    public int getMaxNaturalHeight() {
        return WATER_LEVEL + 50;
    }

    public double getNoisePerBlock() {
        return 1d / getMaxNaturalHeight();
    }

    public double getNoise(double height) {
        return getNoisePerBlock() * height;
    }

    public int getHeight(double noise) {
        return MathUtil.toInt(noise / getNoisePerBlock());
    }

    @Override
    @NotNull
    public List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        WorldGenerator gen = this;
        return ArrayUtil.toList(new BlockPopulator[]{
                new BlockPopulator() {
                    private final OverworldPopulator[] POPULATORS = new OverworldPopulator[]{
                            new CavePopulator(gen),
                            //new RavinePopulator(gen),
                            new FloraPopulator(gen),
                            new OceanFloraPopulator(gen),
                            new RockPopulator(gen),
                            new CampfirePopulator(gen)
                    };

                    @Override
                    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
                        XoRoShiRo128StarStarRandomGenerator randomizer = new XoRoShiRo128StarStarRandomGenerator(RandomUtil.getChunkSeed(world.getSeed(), chunk.getX(), chunk.getZ()));
                        Yumn.getInstance().pool.schedule(() -> {
                            for (OverworldPopulator populator : POPULATORS) {
                                populator.populate(world, randomizer, chunk);
                            }
                            LOGGER.logf("Populated chunk at %s %s successfully!", chunk.getX(), chunk.getZ());
                        }, 0, TimeUnit.MILLISECONDS);
                    }
                }
        });
    }

    public List<HeightMapProcessor> getHeightMapProcessors(ChunkHeightMap originChunk, Context context, BiFunction<Integer, Integer, ChunkHeightMap> heightMapProvider) {
        return ArrayUtil.toList(new HeightMapProcessor[]{
                //new BiomeEdgeMarker(originChunk, context, heightMapProvider),
                new Interpolation(originChunk, context, heightMapProvider),
                new MessFixer(originChunk, context, heightMapProvider)
        });
    }

    private double[][] generatePreHeightMap(long seed, int chunkX, int chunkZ) {
        OctaveNoiseGenerator gen1 = new OctaveNoiseGenerator(seed)
                        .setScale(NOISE_SCALE)
                        .setFrequency(0.102)
                        .setAmplitude(1)
                        .setLacunarity(1.663)
                        .setPersistence(0.27)
                        .setOctaves(3)
                        .setNormalized(false);
        OctaveNoiseGenerator gen2 = new OctaveNoiseGenerator(seed)
                        .setScale(NOISE_SCALE)
                        .setFrequency(0.848)
                        .setAmplitude(0.494)
                        .setLacunarity(0.731)
                        .setPersistence(1.004)
                        .setOctaves(6)
                        .setNormalized(false);
        double[][] heightmap = new double[16][16];
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                int rx = (chunkX << 4) + x;
                int rz = (chunkZ << 4) + z;
                double v = gen1.noise(rx + 1, rz + 1);
                v *= Math.sin(Math.pow(Math.E, v) * gen2.noise(rx, rz) * 0.3);
                heightmap[x][z] = v;
            }
        }
        return heightmap;
    }

    private double f1(double v) {
        return 1 / (1 + Math.pow(Math.E, -v));
    }

    private double f2(double v) {
        return Math.sin(Math.pow(Math.E, v) * v * 0.3);
    }

    @NotNull
    private ChunkHeightMap generateHeightMap(@NotNull Context context, boolean soft) {
        OctaveNoiseGenerator precipitationGen = new OctaveNoiseGenerator(context.getSeed() >> 4)
                .setOctaves(2)
                .setLacunarity(1.5)
                .setPersistence(3)
                .setScale(BIOME_PRECIPITATION_NOISE_SCALE);
        OctaveNoiseGenerator bioChanceGen = new OctaveNoiseGenerator(-(context.getSeed() >> 4))
                .setOctaves(2)
                .setLacunarity(1.5)
                .setPersistence(3)
                .setScale(BIOME_CHANCE_NOISE_SCALE);
        double[][] heightMap = generatePreHeightMap(context.getSeed(), context.getChunkX(), context.getChunkZ());
        HeightMapCell[][] heightMapCells = new HeightMapCell[16][16];
        Int2ObjectMap<NoiseGenerator> biomeNoises = new Int2ObjectOpenHashMap<>();

        double min = getNoise(OCEAN_SURFACE_LEVEL);

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                int rx = (context.getChunkX() << 4) + x;
                int rz = (context.getChunkZ() << 4) + z;
                double noise = heightMap[x][z];
                double bp = precipitationGen.noise(rx, rz);
                bp *= f1(bp) + f2(bp);
                double bc = bioChanceGen.noise(rx, rz);
                bc *= f1(bc) + f2(bc);
                YumnBiome biome = BiomeManager.getInstance().pickBiome(
                        context,
                        MathUtil.clampDouble(noise, 0, 1),
                        MathUtil.clampDouble(bp, 0, 1),
                        MathUtil.clampDouble(bc, 0, 1)
                );
                NoiseGenerator cnp = biomeNoises.compute(biome.hashCode(), (i, n) -> {
                    if (n != null) return n;
                    NoiseGenerator ssn = biome.initBiomeNoise(context);
                    return ssn == null ? DummyNoiseGenerator.INSTANCE : ssn;
                });
                NoiseGenerator nsp = cnp instanceof DummyNoiseGenerator ? null : cnp;
                double terrainNoise = biome.transform(context, nsp, x, z, noise);
                terrainNoise = Math.max(min, terrainNoise);
                heightMapCells[x][z] = new HeightMapCell(terrainNoise, biome);
            }
        }

        ChunkHeightMap noiseMap = new ChunkHeightMap(heightMapCells);

        BiFunction<Integer, Integer, ChunkHeightMap> hmp = (x, z) -> soft ? null : requestHeightMap(
                context.getWorld(),
                x, z,
                () -> new Context(
                        this,
                        context.getWorld(),
                        x, z,
                        context.getSeed(),
                        new XoRoShiRo128StarStarRandomGenerator(RandomUtil.getChunkSeed(context.getSeed(), x, z)),
                        new DummyChunkData(context.getWorld().getMaxHeight())
                ), true
        );
        for (HeightMapProcessor p : getHeightMapProcessors(noiseMap, context, hmp)) {
            noiseMap.forEach(0, 0, x -> {
                p.process(x);
                x.next();
            });
        }
        return noiseMap;
    }

    @NotNull
    public ChunkHeightMap requestHeightMap(@NotNull World world, int chunkX, int chunkZ, Supplier<Context> contextSupplier, boolean soft) {
        WorldHeightMap wm = CACHED_HEIGHT_MAPS.request(world.getUID());
        ChunkHeightMap map = wm.get(chunkX, chunkZ);
        if (map == null) {
            map = generateHeightMap(contextSupplier.get(), soft);
            if (!soft) {
                wm.put(chunkX, chunkZ, map);
            }
        }
        return map;
    }

    @Override
    @NotNull
    public ChunkGenerator.ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkGenerator.BiomeGrid biome) {
        ChunkGenerator.ChunkData cd = createChunkData(world);
        long s = world.getSeed(); // correct way!!! (don't get the seed from provided Random)
        Context ctx = new Context(this, world, chunkX, chunkZ, s, new XoRoShiRo128StarStarRandomGenerator(RandomUtil.getChunkSeed(s, chunkX, chunkZ)), cd);
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
                ((YumnBiome<Context>) cell.getBiome()).generate(ctx, point2d.getX(), point2d.getY(), cell.getTerrainNoise());
            }*/

            // smooth biome edges by mixing block & biome randomly (without ocean biomes)
            if (cell.isBiomeEdge() && !BiomeCollection.AQUATIC.contains(cell.getBiome().getMinecraftBiome()) && !cell.getNearbyBiomes().isEmpty() && random.nextInt(3) == 0) {
                YumnBiome b = cell.getNearbyBiomes().iterator().next();
                if (!BiomeCollection.AQUATIC.contains(b.getMinecraftBiome())) {
                    b.generate(ctx, point2d.getX(), point2d.getY(), cell.getNoise());
                    cell.setBiome(b);
                } else {
                    cell.getBiome().generate(ctx, point2d.getX(), point2d.getY(), cell.getNoise());
                }
            } else {
                cell.getBiome().generate(ctx, point2d.getX(), point2d.getY(), cell.getNoise());
            }

            for (YumnFeature feature : cell.getBiome().buildFeatures(ctx)) {
                feature.implement(point2d.getX(), point2d.getY(), cell);
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
