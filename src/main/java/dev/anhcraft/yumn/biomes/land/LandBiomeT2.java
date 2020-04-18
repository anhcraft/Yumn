package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.generators.overworld.OverworldGenerator;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import dev.anhcraft.yumn.utils.noise.SimplexOctaveNoise;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.Nullable;

public abstract class LandBiomeT2 extends LandBiome {
    public LandBiomeT2(Biome minecraftBiome, double temperature, double precipitation) {
        super(minecraftBiome, temperature, precipitation);
    }

    @Override
    public double getScaleMultiplier(OverworldContext context){
        return 0.1;
    }

    @Override
    public int getHeightRatioBuff(OverworldContext context, @Nullable NoiseProvider biomeNoise, int localX, int localZ, double noise){
        return (int) (30 * noise);
    }

    @Override
    public double redistribute(OverworldContext context, NoiseProvider biomeNoise, int localX, int localZ, double noise){
        OverworldGenerator gen = context.getGenerator();
        final double noiseRatio = gen.getNoise(gen.LAND_HEIGHT_T2 + getHeightRatioBuff(context, biomeNoise, localX, localZ, noise));
        final double noiseOffset = gen.getNoise(gen.LAND_LEVEL + gen.LAND_HEIGHT_T1);
        int rx = (context.getChunkX() << 4) + localX;
        int rz = (context.getChunkZ() << 4) + localZ;
        return ((SimplexOctaveNoise) biomeNoise).noise(rx, rz)*noiseRatio+noiseOffset;
    }
}
