package dev.anhcraft.yumn.populators;

import dev.anhcraft.yumn.generators.overworld.OverworldGenerator;

public abstract class OverworldPopulator extends YumnPopulator {
    public OverworldPopulator(OverworldGenerator generator) {
        super(generator);
    }

    @Override
    public OverworldGenerator getGenerator() {
        return (OverworldGenerator) super.getGenerator();
    }
}
