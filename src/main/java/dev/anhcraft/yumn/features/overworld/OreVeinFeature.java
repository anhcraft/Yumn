package dev.anhcraft.yumn.features.overworld;


import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import dev.anhcraft.yumn.utils.noise.NoiseGenerator;
import dev.anhcraft.yumn.utils.noise.OctaveNoiseGenerator;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

public class OreVeinFeature extends YumnFeature {
    private final Map<Integer, OreChoices> oreGroups = new TreeMap<>();
    private final OctaveNoiseGenerator noise;

    public OreVeinFeature(Context context) {
        super(context);
        oreGroups.put(45, new OreChoices()
                .next(5, Material.EMERALD_ORE)
                .next(6, Material.DIAMOND_ORE)
                .next(9, Material.REDSTONE_ORE)
                .next(6, Material.LAPIS_ORE)
                .next(6, Material.GOLD_ORE)
                .next(9, Material.IRON_ORE)
                .next(9, Material.COAL_ORE)
        ); // 50
        oreGroups.put(80, new OreChoices()
                .next(13, Material.GOLD_ORE)
                .next(13, Material.IRON_ORE)
                .next(14, Material.COAL_ORE)
        ); // 40
        oreGroups.put(150, new OreChoices()
                .next(9, Material.IRON_ORE)
                .next(11, Material.COAL_ORE)
        ); // 20
        oreGroups.put(256, new OreChoices()
                .next(6, Material.IRON_ORE)
                .next(9, Material.COAL_ORE)
        ); // 15
        noise = new OctaveNoiseGenerator(context.getSeed())
                .setScale(1)
                .setFrequency(0.3);
    }

    public Material pick(int h, double chanceScaled0To1) {
        for (Map.Entry<Integer, OreChoices> e : oreGroups.entrySet()) {
            if (h <= e.getKey()) {
                return e.getValue().pick(chanceScaled0To1);
            }
        }
        return null;
    }

    @Override
    public void implement(int localX, int localZ, HeightMapCell cell) {
        int x = (getContext().getChunkX() << 4) + localX;
        int z = (getContext().getChunkZ() << 4) + localZ;
        for (int y = 0; y < getContext().getWorldGenerator().getMaxNaturalHeight(); y++) {
            Material mt = getContext().getChunkData().getType(localX, y, localZ);
            if (mt == Material.STONE) {
                double n = noise.noise(x, y, z);
                Material m = pick(y, n);
                if (m != null) {
                    getContext().getChunkData().setBlock(localX, y, localZ, m);
                }
            }
        }
    }

    private static class OreChoices {
        private final Map<Double, Material> ores = new TreeMap<>();
        private double sum;

        /*public OreChoices(){
            System.out.println("----------------");
        }*/

        public OreChoices next(int chanceScaledTo100, Material mt) {
            sum += chanceScaledTo100;
            ores.put((sum / 100d) * 0.5 /* ratio */, mt);
            //System.out.println(sum + " " + mt.name());
            return this;
        }

        @Nullable
        public Material pick(double chanceScaledTo1) {
            for (Map.Entry<Double, Material> e : ores.entrySet()) {
                if (chanceScaledTo1 <= e.getKey()) {
                    return e.getValue();
                }
            }
            return null;
        }
    }
}
