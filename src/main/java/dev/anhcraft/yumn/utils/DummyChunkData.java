package dev.anhcraft.yumn.utils;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

public class DummyChunkData implements ChunkGenerator.ChunkData {
    private final int maxHeight;

    public DummyChunkData(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull Material material) {

    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull MaterialData material) {

    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {

    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, @NotNull Material material) {

    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, @NotNull MaterialData material) {

    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, @NotNull BlockData blockData) {

    }

    @Override
    public @NotNull Material getType(int x, int y, int z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull MaterialData getTypeAndData(int x, int y, int z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull BlockData getBlockData(int x, int y, int z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getData(int x, int y, int z) {
        throw new UnsupportedOperationException();
    }
}
