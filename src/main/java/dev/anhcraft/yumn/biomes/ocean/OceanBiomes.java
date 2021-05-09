package dev.anhcraft.yumn.biomes.ocean;

import dev.anhcraft.yumn.biomes.BiomeCategory;

public class OceanBiomes extends BiomeCategory {
    public OceanBiomes() {
        registerBiome(0, WarmOcean.class);
        registerBiome(1, FrozenOcean.class);
        registerBiome(2, DeepWarmOcean.class);
        registerBiome(3, DeepFrozenOcean.class);
        registerBiome(4, DeepOcean.class);
        registerBiome(5, Ocean.class);
        registerBiome(6, Shallow.class);

        registerGroup(0.4, 0, 1, 5, 2, 2, 3, 3, 4, 4);
        registerGroup(0.99, 0, 1, 5);
        registerGroup(1, 0, 1, 5, 6, 6);
    }
}
