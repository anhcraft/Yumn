package dev.anhcraft.yumn.features.overworld;

import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import org.bukkit.Material;

public class BedrockLayerFeature<T extends Context<?>> extends YumnFeature<T> {
    @Override
    public NoiseProvider initNoise(T context) {
        return null;
    }

    @Override
    public void implement(T context, NoiseProvider noiseProvider, int localX, int localZ, HeightMapCell cell) {
        for (int i = 0; i < 3; i++) {
            context.getChunkData().setBlock(localX, i, localZ, Material.BEDROCK);
        }
    }
}
