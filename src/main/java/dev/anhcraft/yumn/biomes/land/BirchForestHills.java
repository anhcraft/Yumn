package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class BirchForestHills extends LandBiomeT2 {
    public BirchForestHills() {
        super(Biome.BIRCH_FOREST_HILLS, 18, 600);
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getGenerator().getHeight(noise) - 3).is(Material.STONE)
                .to(context.getGenerator().getHeight(noise) - 1).is(Material.DIRT)
                .up(1).is(Material.GRASS_BLOCK);
    }
}
