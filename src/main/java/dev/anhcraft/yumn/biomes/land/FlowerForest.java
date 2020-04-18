package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.generators.overworld.OverworldGenerator;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class FlowerForest extends LandBiomeT1 {
    public FlowerForest() {
        super(Biome.FLOWER_FOREST, 30.6, 770);
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        OverworldGenerator gen = context.getGenerator();
        if(gen.getHeight(noise) <= gen.WATER_LEVEL){
            super.generate(context, localX, localZ, noise);
        } else {
            context.newLayerBuilder().at(localX, localZ)
                    .to(gen.getHeight(noise) - 1).is(Material.STONE)
                    .up(1).is(Material.GRASS_BLOCK);
        }
    }
}
