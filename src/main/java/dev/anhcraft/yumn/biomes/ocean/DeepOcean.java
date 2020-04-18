package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class DeepOcean extends OceanBiomeT2 {
    public DeepOcean() {
        super(Biome.DEEP_OCEAN, 30, 1300);
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getGenerator().getHeight(noise)-1).is(Material.STONE)
                .up(1).is(Material.GRAVEL)
                .to(context.getGenerator().WATER_LEVEL).is(Material.WATER);
    }
}
