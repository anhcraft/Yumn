package dev.anhcraft.yumn.heightmaps.renovation;

import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import dev.anhcraft.yumn.heightmaps.HeightMapProcessor;
import dev.anhcraft.yumn.heightmaps.ChunkHeightMap;

import java.util.function.BiFunction;

public class MessFixer extends HeightMapProcessor<Context<?>> {
    public MessFixer(ChunkHeightMap originChunk, Context<?> context, BiFunction<Integer, Integer, ChunkHeightMap> heightMapProvider) {
        super(originChunk, context, heightMapProvider);
    }

    @Override
    protected void process(HeightMapCell origin, int localX, int localZ) {
        double s = origin.getTerrainNoise();
        HeightMapCell r1 = getRelative(1, 0);
        HeightMapCell r2 = getRelative(-1, 0);
        HeightMapCell r3 = getRelative(0, 1);
        HeightMapCell r4 = getRelative(0, -1);
        if(r1 != null && r2 != null && r3 != null && r4 != null) {
            double f1 = r1.getTerrainNoise() - s;
            double f2 = r2.getTerrainNoise() - s;
            double f3 = r3.getTerrainNoise() - s;
            double f4 = r4.getTerrainNoise() - s;
            if (f1 > 0 && f2 > 0 && f3 > 0 && f4 > 0) {
                origin.setTerrainNoise(s + (f1 + f2 + f3 + f4) / 4d);
                origin.setSharpness(true);
            }
        }
    }
}
