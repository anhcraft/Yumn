package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class PlainHills extends LandBiomeT2 {
    public PlainHills() {
        super(Biome.PLAINS, 16, 400);
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getGenerator().getHeight(noise) - 3).is(Material.STONE)
                .to(context.getGenerator().getHeight(noise) - 1).is(Material.DIRT)
                .up(1).is(Material.GRASS_BLOCK);
    }
}
