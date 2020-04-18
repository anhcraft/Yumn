package dev.anhcraft.yumn.generators;

import dev.anhcraft.jvmkit.utils.MathUtil;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

public abstract class YumnGenerator extends ChunkGenerator {
    private final World.Environment environment;

    protected YumnGenerator(@NotNull World.Environment environment) {
        this.environment = environment;
    }

    @NotNull
    public World.Environment getEnvironment() {
        return environment;
    }

    public abstract int getMaxNaturalHeight();

    public double getNoisePerBlock() {
        return 1d / getMaxNaturalHeight();
    }

    public double getNoise(double height) {
        return getNoisePerBlock() * height;
    }

    public int getHeight(double noise) {
        return MathUtil.toInt(noise / getNoisePerBlock());
    }
}
