package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.generators.Context;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class OakForestHills extends LandBiomeT2 {
    public OakForestHills() {
        super(Biome.FOREST, 19, 700);
    }

    @Override
    public void generate(Context context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getWorldGenerator().getHeight(noise) - 3).is(Material.STONE)
                .to(context.getWorldGenerator().getHeight(noise) - 1).is(Material.DIRT)
                .up(1).is(Material.GRASS_BLOCK);
    }
}
