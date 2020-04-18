package dev.anhcraft.yumn.features.overworld;

import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import dev.anhcraft.yumn.utils.RelativePos;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;

public class CliffFeature<T extends Context<?>> extends YumnFeature<T> {
    @Override
    public NoiseProvider initNoise(T context) {
        return null;
    }

    @Override
    public void implement(T context, NoiseProvider noiseProvider, int localX, int localZ, HeightMapCell cell) {
        if(cell.isCliff()) {
            int localY = context.getGenerator().getHeight(cell.getTerrainNoise());
            BlockData bd = context.getChunkData().getBlockData(localX, localY, localZ);
            for(int v : cell.getCliffDir()){
                if(v > 0) {
                    context.getChunkData().setBlock(
                            localX + RelativePos.getX(v),
                            localY,
                            localZ + RelativePos.getZ(v),
                            bd
                    );
                }
            }
        }
    }
}
