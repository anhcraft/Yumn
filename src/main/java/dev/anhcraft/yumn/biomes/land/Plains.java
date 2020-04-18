package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.generators.overworld.OverworldGenerator;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.Nullable;

public class Plains extends LandBiomeT1 {
    public Plains() {
        super(Biome.PLAINS, 34, 950);
    }

    @Override
    public int getHeightRatioBuff(OverworldContext context, @Nullable NoiseProvider biomeNoise, int localX, int localZ, double noise){
        return 3;
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
