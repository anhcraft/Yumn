package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.generators.Context;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class WarmOcean extends OceanBiomeT1 {
    public WarmOcean() {
        super(Biome.WARM_OCEAN, 35, 1400);
    }

    @Override
    public void generate(Context context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getWorldGenerator().getHeight(noise) - 1).is(Material.STONE)
                .up(1).is(Material.SAND, Material.SAND, Material.SAND, Material.SAND, Material.GRASS_BLOCK)
                .to(context.getWorldGenerator().WATER_LEVEL).is(Material.WATER);
    }
}
