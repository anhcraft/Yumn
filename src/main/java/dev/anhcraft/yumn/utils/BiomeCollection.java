package dev.anhcraft.yumn.utils;

import org.bukkit.block.Biome;

import java.util.EnumSet;

public class BiomeCollection {
    public static final EnumSet<Biome> AQUATIC = EnumSet.noneOf(Biome.class);
    public static final EnumSet<Biome> WARM_OCEAN = EnumSet.noneOf(Biome.class);
    public static final EnumSet<Biome> COLD_OCEAN = EnumSet.noneOf(Biome.class);
    public static final EnumSet<Biome> DEEP_OCEAN = EnumSet.noneOf(Biome.class);
    public static final EnumSet<Biome> FOREST = EnumSet.noneOf(Biome.class);

    static {
        for(Biome b : Biome.values()){
            if(b.name().contains("OCEAN") || b.name().contains("RIVER")) AQUATIC.add(b);
            if(b.name().contains("WARM_OCEAN")) WARM_OCEAN.add(b);
            if(b.name().contains("COLD_OCEAN")) COLD_OCEAN.add(b);
            if(b.name().contains("DEEP")) DEEP_OCEAN.add(b);
            if(b.name().contains("FOREST")) FOREST.add(b);
        }
    }
}
