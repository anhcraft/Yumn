package dev.anhcraft.yumn.features.overworld;

import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import org.bukkit.Material;

public class BedrockLayerFeature extends YumnFeature {
    public BedrockLayerFeature(Context context) {
        super(context);
    }

    @Override
    public void implement(int localX, int localZ, HeightMapCell cell) {
        for (int i = 0; i < 3; i++) {
            getContext().getChunkData().setBlock(localX, i, localZ, Material.BEDROCK);
        }
    }
}
