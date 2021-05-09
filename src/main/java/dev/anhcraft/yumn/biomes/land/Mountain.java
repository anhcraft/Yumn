package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.generators.Context;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class Mountain extends LandBiomeT3 {
    public static final Material[] MATERIALS = new Material[]{
            Material.STONE,
            Material.STONE,
            Material.ANDESITE
    };

    public Mountain(Biome minecraftBiome, double temperature, double precipitation) {
        super(minecraftBiome, temperature, precipitation);
    }

    public Mountain() {
        super(Biome.MOUNTAINS, 15, 120);
    }

    @Override
    public void generate(Context context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getWorldGenerator().getHeight(noise) - 5).is(MATERIALS)
                .up(5).is(Material.LIGHT_GRAY_CONCRETE);
    }
}
