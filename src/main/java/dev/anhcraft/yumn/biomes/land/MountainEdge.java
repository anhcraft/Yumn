package dev.anhcraft.yumn.biomes.land;

import com.google.common.collect.ImmutableList;
import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.features.overworld.MountainFeature;
import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MountainEdge extends LandBiomeT3 {
    public MountainEdge() {
        super(Biome.MOUNTAINS, 17, 260);
    }

    @Override
    public int getHeightRatioBuff(OverworldContext context, @Nullable NoiseProvider biomeNoise, int localX, int localZ, double noise){
        return (int) (30 * noise);
    }

    @Override
    public void generate(OverworldContext context, int localX, int localZ, double noise) {
        context.newLayerBuilder().at(localX, localZ)
                .to(context.getGenerator().getHeight(noise) - 5).is(Material.STONE, Material.GRAVEL, Material.ANDESITE)
                .up(5).is(Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRAVEL, Material.STONE);
    }

    @NotNull
    public List<YumnFeature<OverworldContext>> getFeatures() {
        return ImmutableList.<YumnFeature<OverworldContext>>builder().add(new MountainFeature()).addAll(super.getFeatures()).build();
    }
}
