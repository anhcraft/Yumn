package dev.anhcraft.yumn.heightmaps.renovation;

import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.heightmaps.ChunkHeightMap;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import dev.anhcraft.yumn.heightmaps.HeightMapProcessor;
import dev.anhcraft.yumn.utils.NoiseUtil;

import java.util.function.BiFunction;

public class Interpolation extends HeightMapProcessor<Context<?>> {
    private static final int RANGE = 7;

    public Interpolation(ChunkHeightMap originChunk, Context<?> context, BiFunction<Integer, Integer, ChunkHeightMap> heightMapProvider) {
        super(originChunk, context, heightMapProvider);
    }

    @Override
    protected void process(HeightMapCell origin, int localX, int localZ) {
        double noise = origin.getOldTerrainNoise();
        double sum = 0;
        int count = 0;
        for (int x = -RANGE; x <= RANGE; x++) {
            for (int z = -RANGE; z <= RANGE; z++) {
                if ((x * x + z * z) >= (RANGE * RANGE) || (x == 0 && z == 0)) continue;
                HeightMapCell cell = getRelative(x, z);
                if(cell != null) {
                    sum += NoiseUtil.lerp(noise, cell.getOldTerrainNoise(), .75);
                    count++;
                }
            }
        }
        if(count > 0) {
            origin.setTerrainNoise(sum / count);
        }
    }
}
