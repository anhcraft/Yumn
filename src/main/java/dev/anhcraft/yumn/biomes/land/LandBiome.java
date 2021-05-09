package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.biomes.YumnBiome;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.generators.WorldGenerator;
import dev.anhcraft.yumn.utils.noise.NoiseGenerator;
import dev.anhcraft.yumn.utils.noise.OctaveNoiseGenerator;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

public abstract class LandBiome extends YumnBiome {
    protected LandBiome(@NotNull Biome minecraftBiome, double temperature, double precipitation) {
        super(minecraftBiome, temperature, precipitation);
    }

    public double getScaleMultiplier(Context context) {
        return 0.07;
    }

    @Override
    public NoiseGenerator initBiomeNoise(Context context) {
        return new OctaveNoiseGenerator(context.getSeed())
                .setOctaves(5)
                .setFrequency(0.3)
                .setLacunarity(1.8)
                .setAmplitude(1)
                .setPersistence(0.5)
                .setScale(context.getWorldGenerator().NOISE_SCALE * 6 * getScaleMultiplier(context));
    }

    protected double f1(double v) {
        return 1 / (1 + Math.pow(Math.E, -v));
    }

    protected double f2(double v) {
        return Math.sin(Math.pow(Math.E, v) * v * 0.3);
    }

    public void generate(Context context, int localX, int localZ, double noise) {
        WorldGenerator gen = context.getWorldGenerator();
        Context.LayerBuilder b = context.newLayerBuilder().at(localX, localZ).to(gen.getHeight(noise) - 1).is(Material.STONE);
        if (gen.getHeight(noise) == gen.WATER_LEVEL) {
            b.up(1).is(Material.SAND);
        } else {
            b.up(1).is(Material.SAND).to(gen.WATER_LEVEL).is(Material.WATER);
        }
    }
}
