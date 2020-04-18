package dev.anhcraft.yumn.biomes.land;

import com.google.common.collect.ImmutableList;
import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.features.overworld.MountainFeature;
import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getGenerator().getHeight(noise) - 5).is(MATERIALS)
                .up(5).is(Material.LIGHT_GRAY_CONCRETE);
    }

    @NotNull
    public List<YumnFeature<OverworldContext>> getFeatures() {
        return ImmutableList.<YumnFeature<OverworldContext>>builder().add(new MountainFeature()).addAll(super.getFeatures()).build();
    }
}
