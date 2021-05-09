package dev.anhcraft.yumn.populators.overworld;

import dev.anhcraft.yumn.generators.WorldGenerator;
import dev.anhcraft.yumn.nms.FastBlockModifier;
import dev.anhcraft.yumn.populators.OverworldPopulator;
import dev.anhcraft.yumn.utils.BiomeCollection;
import dev.anhcraft.yumn.utils.RandomUtil;
import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class RavinePopulator extends OverworldPopulator {
    private static final int RAVINE_MAX_WIDTH = 300;
    private static final int RAVINE_MIN_WIDTH = 100;
    private static final int RAVINE_MAX_DEEP = 25;
    private static final int RAVINE_MIN_DEEP = 15;
    private static final double RAVINE_MAX_RADIUS = 5;
    private static final double RAVINE_MIN_RADIUS = 3;
    private static final double RAVINE_MAX_OFFSET = 3;
    private static final double RAVINE_MIN_OFFSET = -3;

    public RavinePopulator(WorldGenerator generator) {
        super(generator);
    }

    @Override
    public void populate(@NotNull World world, XoRoShiRo128StarStarRandomGenerator random, @NotNull Chunk chunk) {
        if (random.nextInt(1200) != 0) return;
        Location start = new Location(
                world,
                random.nextInt(16) + (chunk.getX() << 4),
                getGenerator().getMaxNaturalHeight(),
                random.nextInt(16) + (chunk.getZ() << 4)
        );
        boolean found = false;
        for (int y = start.getBlockY(); y >= getGenerator().OCEAN_SURFACE_LEVEL; y--) {
            if (start.getBlock().getType().isSolid()) {
                found = true;
                break;
            }
            start.subtract(0, 1, 0);
        }
        if (!found) return;
        double offsetX = RandomUtil.rand(random, RAVINE_MIN_OFFSET, RAVINE_MAX_OFFSET);
        double offsetZ = RandomUtil.rand(random, RAVINE_MIN_OFFSET, RAVINE_MAX_OFFSET);
        int width = RAVINE_MIN_WIDTH;
        int maxWidth = RandomUtil.rand(random, RAVINE_MIN_WIDTH, RAVINE_MAX_WIDTH);
        while (width++ < maxWidth) {
            int depth = RandomUtil.rand(random, RAVINE_MIN_DEEP, RAVINE_MAX_DEEP);
            Location location = start.clone().subtract(0, depth, 0);
            if (location.getY() < RAVINE_MAX_RADIUS * 1.5) {
                continue;
            }
            double radius = RandomUtil.rand(random, RAVINE_MIN_RADIUS, RAVINE_MAX_RADIUS);
            boolean ground = true;
            do {
                for (int r = 0; r < radius; r++) {
                    for (int deg = 0; deg < 360; deg++) {
                        double rad = Math.toRadians(deg);
                        Location loc = location.clone().add(
                                Math.cos(rad) * r,
                                0,
                                Math.sin(rad) * r
                        );
                        Block block = loc.getBlock();
                        boolean underWater = BiomeCollection.AQUATIC.contains(block.getBiome()) && loc.getY() <= getGenerator().WATER_LEVEL || block.getType() == Material.WATER;
                        if (ground) {
                            FastBlockModifier.change(block, underWater ? Material.RED_SAND : Material.GRAVEL);
                        } else {
                            FastBlockModifier.change(block, underWater ? Material.WATER : Material.AIR);
                        }
                    }
                }
                ground = false;
            } while (location.add(0, 1, 0).getY() <= getGenerator().getMaxNaturalHeight());
            if (random.nextInt(50) == 0) {
                offsetX = RandomUtil.rand(random, RAVINE_MIN_OFFSET, RAVINE_MAX_OFFSET);
                offsetZ = RandomUtil.rand(random, RAVINE_MIN_OFFSET, RAVINE_MAX_OFFSET);
            }
            start = start.add(offsetX, 0, offsetZ);
        }
    }
}

