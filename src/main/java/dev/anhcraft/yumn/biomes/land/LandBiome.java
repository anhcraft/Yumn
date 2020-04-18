package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.biomes.YumnBiome;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.generators.overworld.OverworldGenerator;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import dev.anhcraft.yumn.utils.noise.SimplexOctaveNoise;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

public abstract class LandBiome extends YumnBiome<OverworldContext> {
    protected LandBiome(@NotNull Biome minecraftBiome, double temperature, double precipitation) {
        super(minecraftBiome, temperature, precipitation);
    }

    public double getScaleMultiplier(OverworldContext context){
        return 0.07;
    }

    @Override
    public NoiseProvider initBiomeNoise(OverworldContext context){
        return new SimplexOctaveNoise(context.getSeed())
                .setOctaves(5)
                .setFrequency(0.3)
                .setLacunarity(1.8)
                .setAmplitude(1)
                .setPersistence(0.5)
                .setScale(context.getGenerator().TERRAIN_NOISE_SCALE * getScaleMultiplier(context));
    }

    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        OverworldGenerator gen = context.getGenerator();
        Context<?>.LayerBuilder b = context.newLayerBuilder().at(localX, localZ)
                .to(gen.getHeight(noise)-1).is(Material.STONE);
        if(gen.getHeight(noise) == gen.WATER_LEVEL) {
            b.up(1).is(Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.SAND, Material.SAND, Material.GRAVEL);
        } else {
            b.up(1).is(Material.SAND).to(gen.WATER_LEVEL).is(Material.WATER);
            if(this instanceof Swamp) {
                b.up(1).is(Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.LILY_PAD);
            }
        }
    }
}
