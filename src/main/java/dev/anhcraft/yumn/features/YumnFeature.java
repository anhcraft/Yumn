package dev.anhcraft.yumn.features;

import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;

public abstract class YumnFeature {
    private final Context context;

    protected YumnFeature(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public abstract void implement(int localX, int localZ, HeightMapCell cell);

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}
