package dev.anhcraft.yumn.features.overworld;

import dev.anhcraft.yumn.biomes.BiomeManager;
import dev.anhcraft.yumn.biomes.land.LandBiomes;
import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.generators.overworld.OverworldGenerator;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import dev.anhcraft.yumn.utils.noise.SimplexOctaveNoise;
import org.bukkit.Material;

import static dev.anhcraft.yumn.biomes.land.Mountain.MATERIALS;

public class MountainFeature extends YumnFeature<OverworldContext> {
    @Override
    public NoiseProvider initNoise(OverworldContext context) {
        return new SimplexOctaveNoise(context.getSeed())
                .setScale(context.getGenerator().CONTINENT_NOISE_SCALE * 0.01)
                .setFrequency(0.3)
                .setOctaves(3);
    }

    @Override
    public void implement(OverworldContext context, NoiseProvider noiseProvider, int localX, int localZ, HeightMapCell cell) {
        final OverworldGenerator gen = context.getGenerator();
        int caveHeight = 25;
        double reducedCaveNoise = ((SimplexOctaveNoise) noiseProvider).noise(
                (context.getChunkX() << 4) + localX,
                (context.getChunkZ() << 4) + localZ
        ) * 5;
        caveHeight -= reducedCaveNoise;
        int minHeight = gen.LAND_LEVEL + gen.LAND_HEIGHT_T1 + gen.LAND_HEIGHT_T2 + gen.LAND_HEIGHT_T3;
        int height = Math.min(gen.getHeight(cell.getTerrainNoise()), minHeight + caveHeight);

        double altitudeDelta = LandBiomes.MOUNTAIN_ALTITUDE - LandBiomes.MOUNTAIN_EDGE_ALTITUDE;
        double lb = BiomeManager.getInstance().localizeBiome(context, cell.getContinentNoise()).getFirst();
        lb -= LandBiomes.MOUNTAIN_EDGE_ALTITUDE;
        double ratio = 1 / altitudeDelta * lb; // this ratio can higher than 1

        boolean lastSurface = true;

        for (int i = minHeight + 1; i <= height; i++){
            double v = ((SimplexOctaveNoise) noiseProvider).noise(
                    (context.getChunkX() << 4) + localX,
                    i * 2,
                    (context.getChunkZ() << 4) + localZ
            );
            if(ratio <= 0.2){
                context.getChunkData().setBlock(localX, i, localZ,  Material.AIR);
            } else if(v <= 0.36){
                context.getChunkData().setBlock(localX, i, localZ,  Material.AIR);
                if(lastSurface) {
                    context.getChunkData().setBlock(localX, i - 1, localZ, context.getRandomizer().nextDoubleFast() < 0.3 ? Material.GRAVEL : Material.GRASS_BLOCK);
                    lastSurface = false;
                }
            } else {
                context.getChunkData().setBlock(localX, i, localZ, MATERIALS[context.getRandomizer().nextInt(MATERIALS.length)]);
                lastSurface = true;
            }
        }
    }
}
