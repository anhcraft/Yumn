package dev.anhcraft.yumn.populators.overworld;

import com.google.common.collect.ImmutableSet;
import dev.anhcraft.yumn.Structure;
import dev.anhcraft.yumn.generators.WorldGenerator;
import dev.anhcraft.yumn.populators.OverworldPopulator;
import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class RockPopulator extends OverworldPopulator {
    private static final Set<Material> ALLOWED_SURFACES = ImmutableSet.of(
            Material.GRASS_BLOCK,
            Material.STONE,
            Material.DIRT,
            Material.COARSE_DIRT
    );

    public RockPopulator(WorldGenerator generator) {
        super(generator);
    }

    @Override
    public void populate(@NotNull World world, @NotNull XoRoShiRo128StarStarRandomGenerator random, @NotNull Chunk chunk) {
        if (random.nextInt(15) == 0) {
            boolean found = false;
            Location start = new Location(
                    world,
                    random.nextInt(16) + (chunk.getX() << 4),
                    getGenerator().LAND_LEVEL,
                    random.nextInt(16) + (chunk.getZ() << 4)
            );
            while (start.getY() <= getGenerator().getMaxNaturalHeight()) {
                Block b = start.getBlock();
                if (ALLOWED_SURFACES.contains(b.getType()) && a(b.getRelative(BlockFace.UP))) {
                    found = true;
                    break;
                }
                start.add(0, 1, 0);
            }
            if (!found) return;
            (random.nextBoolean() ? Structure.LARGE_ROCK_1 : Structure.LARGE_ROCK_2).spawn(start.add(0, 1, 0), true);
        }
    }

    private boolean a(Block relative) {
        return relative.isEmpty() || relative.isLiquid() || relative.isPassable();
    }
}
