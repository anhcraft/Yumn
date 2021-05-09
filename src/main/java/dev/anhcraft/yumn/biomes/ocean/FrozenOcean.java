package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.generators.Context;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class FrozenOcean extends Ocean {
    public FrozenOcean() {
        super(Biome.FROZEN_OCEAN, 15, 500);
    }

    @Override
    public void generate(Context context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getWorldGenerator().getHeight(noise) - 1).is(Material.STONE)
                .up(1).is(Material.GRAVEL)
                .to(context.getWorldGenerator().WATER_LEVEL - 1).is(Material.WATER)
                .up(1).is(Material.WATER, Material.ICE, Material.ICE, Material.ICE);
    }
}
