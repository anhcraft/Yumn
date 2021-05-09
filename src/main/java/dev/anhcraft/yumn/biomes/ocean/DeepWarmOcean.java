package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.generators.Context;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class DeepWarmOcean extends OceanBiomeT2 {
    public DeepWarmOcean() {
        super(Biome.DEEP_WARM_OCEAN, 33, 1200);
    }

    @Override
    public void generate(Context context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getWorldGenerator().getHeight(noise) - 1).is(Material.STONE)
                .up(1).is(Material.SAND)
                .to(context.getWorldGenerator().WATER_LEVEL).is(Material.WATER);
    }
}
