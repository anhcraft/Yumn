package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class DeepWarmOcean extends OceanBiomeT2 {
    public DeepWarmOcean() {
        super(Biome.DEEP_WARM_OCEAN, 33, 1200);
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getGenerator().getHeight(noise)-1).is(Material.STONE)
                .up(1).is(Material.SAND)
                .to(context.getGenerator().WATER_LEVEL).is(Material.WATER);
    }
}
