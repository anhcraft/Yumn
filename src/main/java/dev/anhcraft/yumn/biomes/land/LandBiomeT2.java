package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.jvmkit.utils.MathUtil;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.generators.WorldGenerator;
import dev.anhcraft.yumn.utils.noise.NoiseGenerator;
import dev.anhcraft.yumn.utils.noise.OctaveNoiseGenerator;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.Nullable;

public abstract class LandBiomeT2 extends LandBiome {
    public LandBiomeT2(Biome minecraftBiome, double temperature, double precipitation) {
        super(minecraftBiome, temperature, precipitation);
    }

    @Override
    public double getScaleMultiplier(Context context) {
        return 0.1;
    }

    @Override
    public double transform(Context context, NoiseGenerator biomeNoise, int localX, int localZ, double noise) {
        WorldGenerator gen = context.getWorldGenerator();
        final double noiseRatio = gen.getNoise(gen.LAND_HEIGHT_T1 + gen.LAND_HEIGHT_T2);
        int rx = (context.getChunkX() << 4) + localX;
        int rz = (context.getChunkZ() << 4) + localZ;
        double n = ((OctaveNoiseGenerator) biomeNoise).noise(rx, rz);
        n *= f1(noise) + f2(noise);
        return gen.getNoise(gen.WATER_LEVEL + gen.LAND_HEIGHT_T1 * 0.5) + MathUtil.clampDouble(n, 0, noiseRatio);
    }
}
