package dev.anhcraft.yumn.utils;

import dev.anhcraft.jvmkit.utils.MathUtil;

public class NoiseUtil {
    public static double normalize(double noise) {
        return normalize(noise, true);
    }

    public static double normalize(double noise, boolean clamp) {
        if (clamp) {
            return MathUtil.clampDouble((noise + 1) / 2d, 0, 1);
        } else {
            return (noise + 1) / 2d;
        }
    }

    public static double lerp(double noise1, double noise2, double alpha) {
        return noise1 + alpha * (noise2 - noise1);
    }
}
