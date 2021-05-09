package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.biomes.YumnBiome;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.generators.WorldGenerator;
import dev.anhcraft.yumn.utils.noise.NoiseGenerator;
import dev.anhcraft.yumn.utils.noise.OctaveNoiseGenerator;
import org.bukkit.block.Biome;

public abstract class OceanBiomeT2 extends YumnBiome {
    public OceanBiomeT2(Biome minecraftBiome, double temperature, double precipitation) {
        super(minecraftBiome, temperature, precipitation);
    }

    @Override
    public NoiseGenerator initBiomeNoise(Context context) {
        return new OctaveNoiseGenerator(context.getSeed() + 1)
                .setFrequency(0.25)
                .setScale(context.getWorldGenerator().NOISE_SCALE * 6 * 0.04);
    }

    @Override
    public double transform(Context context, NoiseGenerator biomeNoise, int localX, int localZ, double noise) {
        WorldGenerator gen = context.getWorldGenerator();
        final double noiseRatio = gen.getNoise(gen.MID_WATER_LEVEL - (gen.OCEAN_SURFACE_LEVEL + 1));
        final double noiseOffset = gen.getNoise(gen.OCEAN_SURFACE_LEVEL);
        int rx = (context.getChunkX() << 4) + localX;
        int rz = (context.getChunkZ() << 4) + localZ;
        double v = ((OctaveNoiseGenerator) biomeNoise).noise(rx, rz) * noiseRatio + noiseOffset;
        return Math.max(v, noiseOffset);
    }
}
