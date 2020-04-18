package dev.anhcraft.yumn.utils.noise;

public class DummySimplexNoise extends SimplexNoise {
    public static final DummySimplexNoise INSTANCE = new DummySimplexNoise();

    private DummySimplexNoise() {
        super(0);
    }
}
