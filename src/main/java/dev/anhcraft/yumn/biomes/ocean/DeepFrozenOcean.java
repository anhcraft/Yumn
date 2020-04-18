package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class DeepFrozenOcean extends OceanBiomeT2 {
    public DeepFrozenOcean() {
        super(Biome.FROZEN_OCEAN, 10, 100);
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getGenerator().getHeight(noise)-1).is(Material.STONE)
                .up(1).is(Material.GRAVEL, Material.SAND, Material.SAND)
                .to(context.getGenerator().WATER_LEVEL-1).is(Material.WATER)
                .up(1).is(Material.ICE);
    }
}
