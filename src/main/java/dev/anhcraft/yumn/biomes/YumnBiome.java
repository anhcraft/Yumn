package dev.anhcraft.yumn.biomes;

import com.google.common.collect.ImmutableList;
import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.features.overworld.BedrockLayerFeature;
import dev.anhcraft.yumn.features.overworld.CliffFeature;
import dev.anhcraft.yumn.features.overworld.OreFeature;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class YumnBiome<T extends Context<?>> {
    private final Biome minecraftBiome;
    private final double temperature;
    private final double precipitation;

    /**
     * Constructs a new biome.
     * @param minecraftBiome The original Minecraft biome.
     * @param temperature The temperature (°C); in scale of 100 °C.
     * @param precipitation Precipitation in millimeters.
     */
    protected YumnBiome(@NotNull Biome minecraftBiome, double temperature, double precipitation) {
        this.minecraftBiome = minecraftBiome;
        this.temperature = temperature / 100d;
        this.precipitation = precipitation / 1000d;
    }

    public int getHeightRatioBuff(T context, @Nullable NoiseProvider biomeNoise, int localX, int localZ, double noise){
        return 0;
    }

    @Nullable
    public NoiseProvider initBiomeNoise(T context){
        return null;
    }

    public double redistribute(T context, @Nullable NoiseProvider biomeNoise, int localX, int localZ, double noise){
        return noise;
    }

    public abstract void generate(T context, int localX, int localZ, double noise);

    public List<YumnFeature<T>> getFeatures() {
        return ImmutableList.of(
                //new CliffFeature(),
                new OreFeature<>(),
                new BedrockLayerFeature<>()
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
        YumnBiome<?> yumnBiome = (YumnBiome<?>) o;
        return Double.compare(yumnBiome.temperature, temperature) == 0 &&
                Double.compare(yumnBiome.precipitation, precipitation) == 0 &&
                minecraftBiome == yumnBiome.minecraftBiome;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minecraftBiome, temperature, precipitation);
    }
}
