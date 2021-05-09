package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.utils.noise.NoiseGenerator;
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
    public void generate(Context context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getWorldGenerator().getHeight(noise) - 1).is(Material.STONE)
                .up(1).is(Material.SAND, Material.GRAVEL, Material.GRAVEL, Material.GRAVEL)
                .to(context.getWorldGenerator().WATER_LEVEL).is(Material.WATER);
    }
}
