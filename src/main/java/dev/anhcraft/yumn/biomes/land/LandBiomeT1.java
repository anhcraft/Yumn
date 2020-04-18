package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.jvmkit.utils.MathUtil;
import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.generators.overworld.OverworldGenerator;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import dev.anhcraft.yumn.utils.noise.SimplexOctaveNoise;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.Nullable;

public abstract class LandBiomeT1 extends LandBiome {
    public LandBiomeT1(Biome minecraftBiome, double temperature, double precipitation) {
        super(minecraftBiome, temperature, precipitation);
    }

    private double f1(double v){
        return 1 / (1 + Math.pow(Math.E, -v));
    }

    private double f2(double v){
        return Math.sin(Math.pow(Math.E, v) * v * 0.3);
    }

    @Override
    public int getHeightRatioBuff(OverworldContext context, @Nullable NoiseProvider biomeNoise, int localX, int localZ, double noise){
        return (int) (30 * noise);
    }

    @Override
    public double redistribute(OverworldContext context, NoiseProvider biomeNoise, int localX, int localZ, double noise){
        OverworldGenerator gen = context.getGenerator();
        final double noiseRatio = gen.getNoise(gen.LAND_HEIGHT_T1 + getHeightRatioBuff(context, biomeNoise, localX, localZ, noise));
        final double noiseOffset = gen.getNoise(gen.WATER_LEVEL - 5);
        int rx = (context.getChunkX() << 4) + localX;
        int rz = (context.getChunkZ() << 4) + localZ;
        double n = ((SimplexOctaveNoise) biomeNoise).noise(rx, rz);
        n *= f1(noise) + f2(noise);
        return MathUtil.clampDouble(n, 0, 1) *noiseRatio+noiseOffset;
    }
}
