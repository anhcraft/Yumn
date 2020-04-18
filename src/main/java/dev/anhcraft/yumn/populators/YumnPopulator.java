package dev.anhcraft.yumn.populators;

import dev.anhcraft.yumn.generators.YumnGenerator;
import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public abstract class YumnPopulator {
    private final YumnGenerator generator;

    protected YumnPopulator(YumnGenerator generator) {
        this.generator = generator;
    }

    public YumnGenerator getGenerator() {
        return generator;
    }

    public abstract void populate(@NotNull World world, XoRoShiRo128StarStarRandomGenerator random, @NotNull Chunk chunk);
}
