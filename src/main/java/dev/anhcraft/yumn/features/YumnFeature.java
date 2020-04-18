package dev.anhcraft.yumn.features;

import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import dev.anhcraft.yumn.utils.noise.NoiseProvider;

public abstract class YumnFeature<T extends Context<?>> {
    public abstract NoiseProvider initNoise(T context);
    public abstract void implement(T context, NoiseProvider noiseProvider, int localX, int localZ, HeightMapCell cell);

    @Override
    public int hashCode(){
        return this.getClass().getSimpleName().hashCode();
    }
}
