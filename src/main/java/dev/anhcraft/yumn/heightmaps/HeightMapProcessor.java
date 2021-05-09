package dev.anhcraft.yumn.heightmaps;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.anhcraft.jvmkit.utils.Array2d;
import dev.anhcraft.yumn.generators.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public abstract class HeightMapProcessor {
    private final Table<Integer, Integer, ChunkHeightMap> neighbors = HashBasedTable.create();
    private final BiFunction<Integer, Integer, ChunkHeightMap> heightMapProvider;
    private final ChunkHeightMap originChunk;
    private final Context context;
    private int globalX;
    private int globalZ;

    public HeightMapProcessor(ChunkHeightMap originChunk, Context context, BiFunction<Integer, Integer, ChunkHeightMap> heightMapProvider) {
        this.originChunk = originChunk;
        this.context = context;
        this.heightMapProvider = heightMapProvider;
    }

    @Nullable
    private ChunkHeightMap getHeightMap(int chunkX, int chunkZ) {
        if (chunkX == context.getChunkX() && chunkZ == context.getChunkZ()) return originChunk;
        int nx = chunkX - context.getChunkX();
        int nz = chunkZ - context.getChunkZ();
        ChunkHeightMap whm = neighbors.get(nx, nz);
        if (whm == null) {
            whm = heightMapProvider.apply(chunkX, chunkZ);
            if (whm == null) {
                return null;
            }
            neighbors.put(nx, nz, whm);
        }
        return whm;
    }

    @NotNull
    public Context getContext() {
        return context;
    }

    public int getGlobalX() {
        return globalX;
    }

    public int getGlobalZ() {
        return globalZ;
    }

    public void process(@NotNull Array2d<HeightMapCell>.Point2d p) {
        globalX = (context.getChunkX() << 4) + p.getX();
        globalZ = (context.getChunkZ() << 4) + p.getY();
        process(p.get(), p.getX(), p.getY());
    }

    @Nullable
    protected HeightMapCell getRelative(int deltaX, int deltaZ) {
        int x = globalX + deltaX;
        int z = globalZ + deltaZ;
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        ChunkHeightMap hm = getHeightMap(chunkX, chunkZ);
        return hm == null ? null : hm.getBackend()[x - (chunkX << 4)][z - (chunkZ << 4)];
    }

    protected abstract void process(HeightMapCell origin, int localX, int localZ);
}
