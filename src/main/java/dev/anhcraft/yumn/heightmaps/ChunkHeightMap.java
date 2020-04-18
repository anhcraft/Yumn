package dev.anhcraft.yumn.heightmaps;

import dev.anhcraft.jvmkit.utils.Array2d;

public class ChunkHeightMap extends Array2d<HeightMapCell> {
    public ChunkHeightMap(HeightMapCell[][] data) {
        super(data);
    }
}
