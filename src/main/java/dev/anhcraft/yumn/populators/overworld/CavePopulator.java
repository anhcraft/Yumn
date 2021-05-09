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
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class CavePopulator extends OverworldPopulator {
    // kích thước đoạn mở đầu hang động
    private static final double BEGIN_CAVE_MAX_SIZE = 2.5;
    private static final double BEGIN_CAVE_MIN_SIZE = 1;
    // kích thước các đoạn hang động
    private static final double CAVE_MAX_SIZE = 3.5;
    private static final double CAVE_MIN_SIZE = 1.5;
    // offset làm xê dịch các đoạn hang động
    // nên là số nhỏ hơn hoặc bằng CAVE_MIN_SIZE
    private static final double CAVE_MAX_OFFSET = 3;
    private static final double CAVE_MIN_OFFSET = -3;
    // độ chia khoảng cách, càng lớn thì các đoạn hang động càng sát nhau theo chiều dọc
    // nên là số nhỏ hơn hoặc bằng CAVE_MIN_SIZE
    private static final int CAVE_VERTICAL_DIVIDER = 15;
    // số lượng đoạn hang động khởi đầu cần có ít nhất
    private static final int BEGIN_CAVE_MIN = 8;
    // số lượng đoạn hang động khởi đầu cần có nhiều nhất
    private static final int BEGIN_CAVE_MAX = 18;
    // số lượng đoạn hang động cần có ít nhất
    private static final int CAVE_MIN_SUBCAVE = 80;

    public CavePopulator(WorldGenerator generator) {
        super(generator);
    }

    @Override
    public void populate(@NotNull World world, XoRoShiRo128StarStarRandomGenerator random, @NotNull Chunk chunk) {
        if (random.nextInt(20) == 0) {
            boolean found = false;
            Location start = new Location(
                    world,
                    random.nextInt(16) + (chunk.getX() << 4),
                    getGenerator().getMaxNaturalHeight(),
                    random.nextInt(16) + (chunk.getZ() << 4)
            );
            for (int y = start.getBlockY(); y >= getGenerator().OCEAN_SURFACE_LEVEL; y--) {
                if (start.getBlock().getType().isSolid()) {
                    found = true;
                    break;
                }
                start.subtract(0, 1, 0);
            }
            if (!found) return;
            int beginCaveAmount = RandomUtil.rand(random, BEGIN_CAVE_MIN, BEGIN_CAVE_MAX);
            beginCave(start, random, 0, beginCaveAmount);
        }
    }

    private void beginCave(Location loc, XoRoShiRo128StarStarRandomGenerator random, int current, int max) {
        if (loc.getY() < CAVE_MAX_SIZE * 1.8) {
            return;
        }
        if (max <= current) {
            subCave(loc, random, 0);
        } else {
            double size = RandomUtil.rand(random, BEGIN_CAVE_MIN_SIZE, BEGIN_CAVE_MAX_SIZE);
            sphere(loc, size);
            loc.subtract(
                    RandomUtil.rand(random, CAVE_MIN_OFFSET, CAVE_MAX_OFFSET),
                    size / CAVE_VERTICAL_DIVIDER,
                    RandomUtil.rand(random, CAVE_MIN_OFFSET, CAVE_MAX_OFFSET)
            );
            beginCave(loc, random, current + 1, max);
        }
    }

    private void subCave(Location loc, XoRoShiRo128StarStarRandomGenerator rand, int i) {
        if (loc.getY() < CAVE_MAX_SIZE * 1.5) {
            return;
        }
        // chỉ dc phép dừng khi đạt đủ số lượng yêu cầu ít nhất
        if (rand.nextInt(100) > 95 && CAVE_MIN_SUBCAVE < i) {
            return;
        }
        double size = RandomUtil.rand(rand, CAVE_MIN_SIZE, CAVE_MAX_SIZE);
        sphere(loc, size);
        if (rand.nextInt(70) < 2) {
            loc = loc.subtract(
                    RandomUtil.rand(rand, CAVE_MIN_OFFSET, CAVE_MAX_OFFSET),
                    0,
                    RandomUtil.rand(rand, CAVE_MIN_OFFSET, CAVE_MAX_OFFSET)
            );
            loc.add(0, size / CAVE_VERTICAL_DIVIDER, 0);
        } else {
            loc.subtract(
                    RandomUtil.rand(rand, CAVE_MIN_OFFSET, CAVE_MAX_OFFSET),
                    size / CAVE_VERTICAL_DIVIDER,
                    RandomUtil.rand(rand, CAVE_MIN_OFFSET, CAVE_MAX_OFFSET)
            );
        }
        subCave(loc, rand, i + 1);
    }

    private void sphere(Location location, double radius) {
        for (double y = -radius; y < radius; y++) {
            for (double x = -radius; x < radius; x++) {
                for (double z = -radius; z < radius; z++) {
                    if ((x * x + y * y + z * z) < radius * radius) {
                        location.add(x, y, z);
                        Block block = location.getBlock();
                        if (location.getY() <= getGenerator().WATER_LEVEL && (BiomeCollection.AQUATIC.contains(block.getBiome()) || block.getType() == Material.WATER)) {
                            FastBlockModifier.change(block, Material.WATER);
                        } else {
                            FastBlockModifier.change(block, Material.CAVE_AIR);
                            Block down = block.getRelative(BlockFace.DOWN);
                            if (down.getType() == Material.DIRT) {
                                FastBlockModifier.change(down, Material.STONE);
                            }
                        }
                        location.subtract(x, y, z);
                    }
                }
            }
        }
    }
}
