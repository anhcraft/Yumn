package dev.anhcraft.yumn.utils.noise;

public class DummyNoiseGenerator extends SimplexNoiseGenerator {
    public static final DummyNoiseGenerator INSTANCE = new DummyNoiseGenerator();

    private DummyNoiseGenerator() {
        super(0);
    }
}
