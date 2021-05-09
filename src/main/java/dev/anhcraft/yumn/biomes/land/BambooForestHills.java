package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.generators.Context;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class BambooForestHills extends LandBiomeT2 {
    public BambooForestHills() {
        super(Biome.BAMBOO_JUNGLE_HILLS, 24, 830);
    }

    @Override
    public void generate(Context context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getWorldGenerator().getHeight(noise) - 3).is(Material.STONE)
                .to(context.getWorldGenerator().getHeight(noise) - 1).is(Material.DIRT)
                .up(1).is(Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.PODZOL);
    }
}
