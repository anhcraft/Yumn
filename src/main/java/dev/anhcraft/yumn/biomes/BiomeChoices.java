package dev.anhcraft.yumn.biomes;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class BiomeChoices {
    private final double minAltitude;
    private final double maxTemperature;
    private final double maxPrecipitation;
    private final double maxChance;
    private final Collection<BiomeChoice> biomes;

    public BiomeChoices(double minAltitude, double maxTemperature, double maxPrecipitation, double maxChance, @NotNull Collection<BiomeChoice> biomes) {
        this.minAltitude = minAltitude;
        this.maxTemperature = maxTemperature;
        this.maxPrecipitation = maxPrecipitation;
        this.maxChance = maxChance;
        this.biomes = biomes;
    }

    public double getMinAltitude() {
        return minAltitude;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public double getMaxPrecipitation() {
        return maxPrecipitation;
    }

    public double getMaxChance() {
        return maxChance;
    }

    @NotNull
    public Collection<BiomeChoice> getBiomes() {
        return biomes;
    }
}
