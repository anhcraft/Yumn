package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.biomes.YumnBiome;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.generators.WorldGenerator;
import dev.anhcraft.yumn.utils.noise.NoiseGenerator;
import dev.anhcraft.yumn.utils.noise.OctaveNoiseGenerator;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class Shallow extends YumnBiome {
    public Shallow() {
        super(Biome.OCEAN, 32, 1600);
    }

    public Shallow(Biome minecraftBiome, double temperature, double precipitation) {
        super(minecraftBiome, temperature, precipitation);
    }

    @Override
    public NoiseGenerator initBiomeNoise(Context context) {
        return new OctaveNoiseGenerator(context.getSeed())
                .setFrequency(0.25)
                .setScale(context.getWorldGenerator().NOISE_SCALE * 6 * 0.045);
    }

    @Override
    public double transform(Context context, NoiseGenerator biomeNoise, int localX, int localZ, double noise) {
        WorldGenerator gen = context.getWorldGenerator();
        final double h = (gen.WATER_LEVEL - gen.MID_WATER_LEVEL) * 0.36;
        final double noiseRatio = gen.getNoise(gen.WATER_LEVEL - (gen.MID_WATER_LEVEL + h));
        final double noiseOffset = gen.getNoise(gen.MID_WATER_LEVEL + h);
        int rx = (context.getChunkX() << 4) + localX;
        int rz = (context.getChunkZ() << 4) + localZ;
        double v = ((OctaveNoiseGenerator) biomeNoise).noise(rx, rz) * noiseRatio + noiseOffset;
        return Math.max(v, noiseOffset);
    }

    @Override
    public void generate(Context context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getWorldGenerator().getHeight(noise) - 1).is(Material.STONE)
                .up(1).is(Material.SAND)
                .to(context.getWorldGenerator().WATER_LEVEL).is(Material.WATER);
    }
}
