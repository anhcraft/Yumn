package dev.anhcraft.yumn.heightmaps;

import dev.anhcraft.yumn.biomes.YumnBiome;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class HeightMapCell {
    private final Set<YumnBiome<?>> nearbyBiomes = new HashSet<>();
    private double oldTerrainNoise;
    private double terrainNoise;
    private double continentNoise;
    private YumnBiome<?> biome;
    private boolean biomeEdge;
    private boolean sharpness;
    private boolean cliff;
    private int[] cliffDir;

    public HeightMapCell(double terrainNoise, double continentNoise, @NotNull YumnBiome<?> biome) {
        this.terrainNoise = terrainNoise;
        oldTerrainNoise = terrainNoise;
        this.continentNoise = continentNoise;
        this.biome = biome;
    }

    @Deprecated
    public double getOldTerrainNoise() {
        return oldTerrainNoise;
    }

    public double getTerrainNoise() {
        return terrainNoise;
    }

    public double getContinentNoise() {
        return continentNoise;
    }

    @Deprecated
    public void setOldTerrainNoise(double oldTerrainNoise) {
        this.oldTerrainNoise = oldTerrainNoise;
    }

    public void setTerrainNoise(double terrainNoise) {
        this.terrainNoise = terrainNoise;
    }

    public void setContinentNoise(double continentNoise) {
        this.continentNoise = continentNoise;
    }

    public void setBiome(YumnBiome<?> biome) {
        this.biome = biome;
    }

    public boolean isBiomeEdge() {
        return biomeEdge;
    }

    public void setBiomeEdge(boolean biomeEdge) {
        this.biomeEdge = biomeEdge;
    }

    @NotNull
    public YumnBiome<?> getBiome() {
        return biome;
    }

    @NotNull
    public Set<YumnBiome<?>> getNearbyBiomes() {
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
