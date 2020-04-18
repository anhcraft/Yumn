package dev.anhcraft.yumn.heightmaps.renovation;

import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import dev.anhcraft.yumn.heightmaps.HeightMapProcessor;
import dev.anhcraft.yumn.heightmaps.ChunkHeightMap;

import java.util.function.BiFunction;

public class BiomeEdgeMarker extends HeightMapProcessor<Context<?>> {
    public BiomeEdgeMarker(ChunkHeightMap originChunk, Context<?> context, BiFunction<Integer, Integer, ChunkHeightMap> heightMapProvider) {
        super(originChunk, context, heightMapProvider);
    }

    @Override
    protected void process(HeightMapCell origin, int localX, int localZ) {
        final int range = 5;
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                if ((x * x + z * z) >= (range * range)) continue;
                HeightMapCell cell = getRelative(x, z);
                if (cell != null && !cell.getBiome().equals(origin.getBiome())) {
                    cell.setBiomeEdge(true);
                    origin.setBiomeEdge(true);
                    origin.getNearbyBiomes().add(cell.getBiome());
                }
            }
        }
    }
}
