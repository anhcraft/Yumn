package dev.anhcraft.yumn.populators;

import dev.anhcraft.yumn.generators.WorldGenerator;
import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public abstract class OverworldPopulator {
    private final WorldGenerator generator;

    protected OverworldPopulator(WorldGenerator generator) {
        this.generator = generator;
    }

    public WorldGenerator getGenerator() {
        return generator;
    }

    public abstract void populate(@NotNull World world, XoRoShiRo128StarStarRandomGenerator random, @NotNull Chunk chunk);
}
