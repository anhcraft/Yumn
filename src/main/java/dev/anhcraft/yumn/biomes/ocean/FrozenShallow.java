package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class FrozenShallow extends Shallow {
    public FrozenShallow() {
        super(Biome.FROZEN_OCEAN, 18, 400);
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getGenerator().getHeight(noise)-1).is(Material.STONE)
                .up(1).is(Material.SAND)
                .to(context.getGenerator().WATER_LEVEL-1).is(Material.WATER)
                .to(context.getGenerator().WATER_LEVEL).is(Material.WATER, Material.ICE, Material.ICE, Material.ICE);
    }
}
