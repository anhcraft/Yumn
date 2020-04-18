package dev.anhcraft.yumn.heightmaps.renovation;

import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.heightmaps.ChunkHeightMap;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import dev.anhcraft.yumn.heightmaps.HeightMapProcessor;
import dev.anhcraft.yumn.utils.RelativePos;

import java.util.function.BiFunction;

public class CliffMarker extends HeightMapProcessor<Context<?>> {
    public CliffMarker(ChunkHeightMap originChunk, Context<?> context, BiFunction<Integer, Integer, ChunkHeightMap> heightMapProvider) {
        super(originChunk, context, heightMapProvider);
    }

    @Override
    protected void process(HeightMapCell origin, int localX, int localZ) {
        double s = origin.getTerrainNoise();
        int i = 0;
        for (int v : RelativePos.ALL) {
            HeightMapCell cell = getRelative(
                    RelativePos.getX(v),
                    RelativePos.getZ(v)
            );
            if (cell != null && getContext().getGenerator().getHeight(s - cell.getTerrainNoise()) >= 3) {
                if(origin.isCliff()) {
                    origin.getCliffDir()[i] = v;
                } else {
                    origin.setCliff(true);
                    origin.setCliffDir(new int[]{0, 0, 0, 0, 0, 0, 0, 0});
                }
            }
            i++;
        }
    }
}
