package dev.anhcraft.yumn.utils;

import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RandomUtil {
    private static final BlockFace[] LIST_BLOCK_FACE_HORIZONTAL = new BlockFace[]{
            BlockFace.NORTH,
            BlockFace.WEST,
            BlockFace.SOUTH,
            BlockFace.EAST,
    };

    /*
    public static long getSeed(Random random){
        Object obj = ReflectionUtil.getDeclaredField(Random.class, random, "seed");
        AtomicLong seed = (AtomicLong) Objects.requireNonNull(obj);
        return seed.get() ^ 0x5DEECE66DL;
    }*/

    public static long getChunkSeed(long world, long x, long z) {
        long hash = world >> 2;
        hash = hash * 37L + (~x);
        hash = hash * 37L + (z << 4);
        return hash;
    }

    public static <T> T rand(XoRoShiRo128StarStarRandomGenerator random, List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    public static int rand(XoRoShiRo128StarStarRandomGenerator random, int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    public static double rand(XoRoShiRo128StarStarRandomGenerator random, double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    @NotNull
    public static BlockFace randHorizontalBlockFace(XoRoShiRo128StarStarRandomGenerator random) {
        return LIST_BLOCK_FACE_HORIZONTAL[random.nextInt(LIST_BLOCK_FACE_HORIZONTAL.length)];
    }
}
