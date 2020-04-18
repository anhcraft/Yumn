package dev.anhcraft.yumn.biomes;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BiomeChoice {
    private final YumnBiome<?> biome;
    private double chance;

    public BiomeChoice(@NotNull YumnBiome<?> biome) {
        this.biome = biome;
    }

    @NotNull
    public YumnBiome<?> getBiome() {
        return biome;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiomeChoice that = (BiomeChoice) o;
        return biome.equals(that.biome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(biome);
    }
}
