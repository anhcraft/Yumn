package dev.anhcraft.yumn.features.overworld;


import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;
import dev.anhcraft.yumn.utils.noise.SimplexOctaveNoise;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

public class OreFeature<T extends Context<?>> extends YumnFeature<T> {
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
        public Material pick(double chanceScaledTo1){
            for(Map.Entry<Double, Material> e : ores.entrySet()){
                if(chanceScaledTo1 <= e.getKey()){
                    return e.getValue();
                }
            }
            return null;
        }
    }

    private final Map<Integer, OreChoices> oreGroups = new TreeMap<>();

    public OreFeature(){
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
    }

    public Material pick(int h, double chanceScaled0To1){
        for(Map.Entry<Integer, OreChoices> e : oreGroups.entrySet()){
            if(h <= e.getKey()) {
                return e.getValue().pick(chanceScaled0To1);
            }
        }
        return null;
    }

    @Override
    public NoiseProvider initNoise(T context) {
        return new SimplexOctaveNoise(context.getSeed())
                .setScale(1)
                .setFrequency(0.3);
    }

    @Override
    public void implement(T context, NoiseProvider noiseProvider, int localX, int localZ, HeightMapCell cell) {
        int x = (context.getChunkX() << 4) + localX;
        int z = (context.getChunkZ() << 4) + localZ;
        for (int y = 0; y < context.getGenerator().getMaxNaturalHeight(); y++) {
            Material mt = context.getChunkData().getType(localX, y, localZ);
            if(mt == Material.STONE) {
                double n = ((SimplexOctaveNoise) noiseProvider).noise(x, y, z);
                Material m = pick(y, n);
                if (m != null) {
                    context.getChunkData().setBlock(localX, y, localZ, m);
                }
            }
        }
    }
}
