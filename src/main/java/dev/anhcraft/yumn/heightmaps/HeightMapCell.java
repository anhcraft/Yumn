package dev.anhcraft.yumn.heightmaps;

import dev.anhcraft.yumn.biomes.YumnBiome;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class HeightMapCell {
    private final Set<YumnBiome> nearbyBiomes = new HashSet<>();
    private double noise;
    private YumnBiome biome;
    private boolean biomeEdge;
    private boolean sharpness;
    private boolean cliff;
    private int[] cliffDir;

    public HeightMapCell(double noise, @NotNull YumnBiome biome) {
        this.noise = noise;
        this.biome = biome;
    }

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public boolean isBiomeEdge() {
        return biomeEdge;
    }

    public void setBiomeEdge(boolean biomeEdge) {
        this.biomeEdge = biomeEdge;
    }

    @NotNull
    public YumnBiome getBiome() {
        return biome;
    }

    public void setBiome(YumnBiome biome) {
        this.biome = biome;
    }

    @NotNull
    public Set<YumnBiome> getNearbyBiomes() {
        return nearbyBiomes;
    }

    public boolean isSharpness() {
        return sharpness;
    }

    public void setSharpness(boolean sharpness) {
        this.sharpness = sharpness;
    }

    public boolean isCliff() {
        return cliff;
    }

    public void setCliff(boolean cliff) {
        this.cliff = cliff;
    }

    public int[] getCliffDir() {
        return cliffDir;
    }

    public void setCliffDir(int[] cliffDir) {
        this.cliffDir = cliffDir;
    }
}
