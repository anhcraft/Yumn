package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.Nullable;

public class Ocean extends OceanBiomeT1 {
    public Ocean() {
        super(Biome.OCEAN, 27, 1000);
    }

    public Ocean(Biome minecraftBiome, double temperature, double precipitation) {
        super(minecraftBiome, temperature, precipitation);
    }

    @Override
    public int getHeightRatioBuff(OverworldContext context, @Nullable NoiseProvider biomeNoise, int localX, int localZ, double noise){
        return (int) (-3 * noise);
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getGenerator().getHeight(noise)-1).is(Material.STONE)
                .up(1).is(Material.SAND, Material.GRAVEL, Material.GRAVEL, Material.GRAVEL)
                .to(context.getGenerator().WATER_LEVEL).is(Material.WATER);
    }
}
