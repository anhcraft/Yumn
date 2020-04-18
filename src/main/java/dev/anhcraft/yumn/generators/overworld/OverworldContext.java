package dev.anhcraft.yumn.generators.overworld;

import dev.anhcraft.yumn.generators.Context;
import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

public class OverworldContext extends Context<OverworldGenerator> {
    public OverworldContext(OverworldGenerator generator, World world, int chunkX, int chunkZ, long seed, @NotNull XoRoShiRo128StarStarRandomGenerator randomizer, ChunkGenerator.ChunkData chunkData) {
        super(generator, world, chunkX, chunkZ, seed, randomizer, chunkData);
    }
}
