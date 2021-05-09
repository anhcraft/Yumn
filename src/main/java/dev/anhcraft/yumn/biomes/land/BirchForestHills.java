package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.generators.Context;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class BirchForestHills extends LandBiomeT2 {
    public BirchForestHills() {
        super(Biome.BIRCH_FOREST_HILLS, 18, 600);
    }

    @Override
    public void generate(Context context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getWorldGenerator().getHeight(noise) - 3).is(Material.STONE)
                .to(context.getWorldGenerator().getHeight(noise) - 1).is(Material.DIRT)
                .up(1).is(Material.GRASS_BLOCK);
    }
}
