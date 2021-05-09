package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.generators.WorldGenerator;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class Desert extends LandBiomeT1 {
    public Desert() {
        super(Biome.DESERT, 50, 50);
    }

    @Override
    public void generate(Context context, int localX, int localZ, double noise) {
        WorldGenerator gen = context.getWorldGenerator();
        if (gen.getHeight(noise) <= gen.WATER_LEVEL) {
            super.generate(context, localX, localZ, noise);
        } else {
            context.newLayerBuilder().at(localX, localZ)
                    .to(context.getWorldGenerator().getHeight(noise) - 5).is(Material.STONE)
                    .to(context.getWorldGenerator().getHeight(noise) - 1).is(Material.SANDSTONE)
                    .up(1).is(Material.SAND, Material.SAND, Material.SANDSTONE);
        }
    }
}
