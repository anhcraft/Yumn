package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class FrozenMountain extends Mountain {
    public FrozenMountain() {
        super(Biome.SNOWY_MOUNTAINS, 10, 100);
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getGenerator().getHeight(noise) - 5).is(MATERIALS)
                .up(4).is(Material.LIGHT_GRAY_CONCRETE)
                .up(1).is(Material.SNOW_BLOCK);
    }
}
