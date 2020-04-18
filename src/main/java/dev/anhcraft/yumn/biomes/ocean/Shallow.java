package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.jvmkit.utils.MathUtil;
import dev.anhcraft.yumn.biomes.YumnBiome;
import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.generators.overworld.OverworldGenerator;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import dev.anhcraft.yumn.utils.noise.SimplexOctaveNoise;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class Shallow extends YumnBiome<OverworldContext> {
    public Shallow() {
        super(Biome.OCEAN, 32, 1600);
    }

    public Shallow(Biome minecraftBiome, double temperature, double precipitation) {
        super(minecraftBiome, temperature, precipitation);
    }

    @Override
    public NoiseProvider initBiomeNoise(OverworldContext context){
        return new SimplexOctaveNoise(context.getSeed())
                .setFrequency(0.25)
                .setScale(context.getGenerator().TERRAIN_NOISE_SCALE * 0.045);
    }

    @Override
    public double redistribute(OverworldContext context, NoiseProvider biomeNoise, int localX, int localZ, double noise){
        OverworldGenerator gen = context.getGenerator();
        final double h = (gen.WATER_LEVEL - gen.MID_WATER_LEVEL) * 0.36;
        final double noiseRatio = gen.getNoise(gen.WATER_LEVEL - (gen.MID_WATER_LEVEL + h));
        final double noiseOffset = gen.getNoise(gen.MID_WATER_LEVEL + h);
        int rx = (context.getChunkX() << 4) + localX;
        int rz = (context.getChunkZ() << 4) + localZ;
        double v = ((SimplexOctaveNoise) biomeNoise).noise(rx, rz)*noiseRatio+noiseOffset;
        return MathUtil.clampDouble(v, noiseOffset, gen.getNoise(gen.WATER_LEVEL));
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getGenerator().getHeight(noise)-1).is(Material.STONE)
                .up(1).is(Material.SAND)
                .to(context.getGenerator().WATER_LEVEL).is(Material.WATER);
    }
}
