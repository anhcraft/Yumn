package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class WarmOcean extends OceanBiomeT1 {
    public WarmOcean() {
        super(Biome.WARM_OCEAN, 35, 1400);
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getGenerator().getHeight(noise)-1).is(Material.STONE)
                .up(1).is(Material.SAND, Material.SAND, Material.SAND, Material.SAND, Material.GRASS_BLOCK)
                .to(context.getGenerator().WATER_LEVEL).is(Material.WATER);
    }
}
