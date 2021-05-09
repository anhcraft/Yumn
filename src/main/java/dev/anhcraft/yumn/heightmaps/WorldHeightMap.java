package dev.anhcraft.yumn.heightmaps;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.Nullable;

public class WorldHeightMap {
    private static final long CLEAN_UP_TIME = 1000 * 60 * 3;
    private final Table<Integer, Integer, Chunk> cache = HashBasedTable.create();
    private final Object safeLock = new Object();

    public void put(int x, int z, ChunkHeightMap heightMap) {
        cache.put(x, z, new Chunk(heightMap));
    }

    @Nullable
    public ChunkHeightMap get(int x, int z) {
        synchronized (safeLock) {
            Chunk chunk = cache.get(x, z);
            if (chunk != null) {
                chunk.lastRequest = System.currentTimeMillis();
                return chunk.heightMap;
            }
        }
        return null;
    }

    /**
     * @deprecated Internal uses only!
     */
    public void clean() {
        synchronized (safeLock) {
            cache.values().removeIf(e -> System.currentTimeMillis() - e.lastRequest >= CLEAN_UP_TIME);
        }
    }

    public static class Chunk {
        private final ChunkHeightMap heightMap;
        private long lastRequest;

        public Chunk(ChunkHeightMap heightMap) {
            this.heightMap = heightMap;
            lastRequest = System.currentTimeMillis();
        }
    }
}
