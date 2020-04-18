package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.biomes.YumnBiome;
import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.generators.overworld.OverworldGenerator;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import dev.anhcraft.yumn.utils.noise.SimplexOctaveNoise;
import org.bukkit.block.Biome;

public abstract class OceanBiomeT2 extends YumnBiome<OverworldContext> {
    public OceanBiomeT2(Biome minecraftBiome, double temperature, double precipitation) {
        super(minecraftBiome, temperature, precipitation);
    }

    @Override
    public NoiseProvider initBiomeNoise(OverworldContext context){
        return new SimplexOctaveNoise(context.getSeed() + 1)
                .setFrequency(0.25)
                .setScale(context.getGenerator().TERRAIN_NOISE_SCALE * 0.04);
    }

    @Override
    public double redistribute(OverworldContext context, NoiseProvider biomeNoise, int localX, int localZ, double noise){
        OverworldGenerator gen = context.getGenerator();
        final double noiseRatio = gen.getNoise(gen.MID_WATER_LEVEL - (gen.OCEAN_SURFACE_LEVEL + 1) + getHeightRatioBuff(context, biomeNoise, localX, localZ, noise));
        final double noiseOffset = gen.getNoise(gen.OCEAN_SURFACE_LEVEL);
        int rx = (context.getChunkX() << 4) + localX;
        int rz = (context.getChunkZ() << 4) + localZ;
        double v = ((SimplexOctaveNoise) biomeNoise).noise(rx, rz)*noiseRatio+noiseOffset;
        return Math.max(v, noiseOffset);
    }
}
