package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.generators.overworld.OverworldGenerator;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class Desert extends LandBiomeT1 {
    public Desert() {
        super(Biome.DESERT, 50, 50);
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        OverworldGenerator gen = context.getGenerator();
        if(gen.getHeight(noise) <= gen.WATER_LEVEL){
            super.generate(context, localX, localZ, noise);
        } else {
            context.newLayerBuilder().at(localX, localZ)
                    .to(context.getGenerator().getHeight(noise) - 5).is(Material.STONE)
                    .to(context.getGenerator().getHeight(noise) - 1).is(Material.SANDSTONE)
                    .up(1).is(Material.SAND, Material.SAND, Material.SANDSTONE);
        }
    }
}
