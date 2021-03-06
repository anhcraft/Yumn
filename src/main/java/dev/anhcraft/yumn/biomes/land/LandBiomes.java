package dev.anhcraft.yumn.biomes.land;

import dev.anhcraft.yumn.biomes.BiomeCategory;

public class LandBiomes extends BiomeCategory {
    public LandBiomes() {
        //registerBiome(0, Coast.class);
        //registerBiome(1, PlainCoast.class);
        registerBiome(2, Plains.class);
        registerBiome(3, SunflowerPlains.class);
        registerBiome(4, Desert.class);
        registerBiome(5, OakForest.class);
        registerBiome(6, FlowerForest.class);
        registerBiome(7, BirchForest.class);
        registerBiome(8, BambooForest.class);
        registerBiome(9, Jungle.class);
        registerBiome(10, Swamp.class);

        registerBiome(12, PlainHills.class);
        registerBiome(13, OakForestHills.class);
        registerBiome(14, BirchForestHills.class);
        registerBiome(15, BambooForestHills.class);
        registerBiome(16, JungleHills.class);

        //registerBiome(17, MountainEdge.class);
        registerBiome(18, Mountain.class);
        registerBiome(19, FrozenMountain.class);

        registerGroup(0.4, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        registerGroup(0.6, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 16);
        registerGroup(0.8, 12, 13, 14, 15, 16);
        registerGroup(1, 18, 19);
    }
}
