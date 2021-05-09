package dev.anhcraft.yumn.biomes;

import com.google.common.collect.ImmutableList;
import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.features.overworld.BedrockLayerFeature;
import dev.anhcraft.yumn.features.overworld.NoiseCaveFeature;
import dev.anhcraft.yumn.features.overworld.OreVeinFeature;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.utils.noise.NoiseGenerator;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class YumnBiome {
    private final Biome minecraftBiome;
    private final double temperature;
    private final double precipitation;

    /**
     * Constructs a new biome.
     *
     * @param minecraftBiome The original Minecraft biome.
     * @param temperature    The temperature (°C); in scale of 100 °C.
     * @param precipitation  Precipitation in millimeters.
     */
    protected YumnBiome(@NotNull Biome minecraftBiome, double temperature, double precipitation) {
        this.minecraftBiome = minecraftBiome;
        this.temperature = temperature / 100d;
        this.precipitation = precipitation / 1000d;
    }

    @Nullable
    public NoiseGenerator initBiomeNoise(Context context) {
        return null;
    }

    public double transform(Context context, @Nullable NoiseGenerator biomeNoise, int localX, int localZ, double noise) {
        return noise;
    }

    public abstract void generate(Context context, int localX, int localZ, double noise);

    public List<YumnFeature> buildFeatures(Context context) {
        return ImmutableList.of(
                new NoiseCaveFeature(context),
                new OreVeinFeature(context),
                new BedrockLayerFeature(context)
        );
    }

    @NotNull
    public Biome getMinecraftBiome() {
        return minecraftBiome;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YumnBiome yumnBiome = (YumnBiome) o;
        return Double.compare(yumnBiome.temperature, temperature) == 0 &&
                Double.compare(yumnBiome.precipitation, precipitation) == 0 &&
                minecraftBiome == yumnBiome.minecraftBiome;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minecraftBiome, temperature, precipitation);
    }
}
