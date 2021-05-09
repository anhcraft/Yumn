package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.jvmkit.utils.MathUtil;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.generators.WorldGenerator;
import dev.anhcraft.yumn.utils.noise.NoiseGenerator;
import dev.anhcraft.yumn.utils.noise.OctaveNoiseGenerator;
import org.bukkit.block.Biome;

public abstract class LandBiomeT1 extends LandBiome {
    public LandBiomeT1(Biome minecraftBiome, double temperature, double precipitation) {
        super(minecraftBiome, temperature, precipitation);
    }

    @Override
    public double transform(Context context, NoiseGenerator biomeNoise, int localX, int localZ, double noise) {
        WorldGenerator gen = context.getWorldGenerator();
        final double noiseRatio = gen.getNoise(gen.LAND_HEIGHT_T1);
        int rx = (context.getChunkX() << 4) + localX;
        int rz = (context.getChunkZ() << 4) + localZ;
        double n = f1(((OctaveNoiseGenerator) biomeNoise).noise(rx, rz)) + f2(noise);
       // System.out.println(n+"/"+noiseRatio);
        return gen.getNoise(gen.WATER_LEVEL) + MathUtil.clampDouble(n, 0, noiseRatio);
    }
}
