package dev.anhcraft.yumn.utils.noise;

import dev.anhcraft.yumn.utils.NoiseUtil;

public class SimplexOctaveNoise extends NoiseProvider {
    private final SimplexNoise noise;
    private int octaves = 1;
    private double scale = 1;
    private double amplitude = 1;
    private double frequency = 0.5;
    private double lacunarity = 2;
    private double persistence = 0.25;
    private boolean normalized = true;

    public SimplexOctaveNoise(long seed){
        noise = new SimplexNoise(seed);
    }

    public SimplexOctaveNoise(SimplexNoise noise){
        this.noise = noise;
    }

    public int getOctaves() {
        return octaves;
    }

    public double getScale() {
        return scale;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getLacunarity() {
        return lacunarity;
    }

    public double getPersistence() {
        return persistence;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public SimplexOctaveNoise setScale(double scale){
        this.scale = scale;
        return this;
    }

    public SimplexOctaveNoise setOctaves(int octaves) {
        this.octaves = Math.max(octaves, 1);
        return this;
    }

    public SimplexOctaveNoise setAmplitude(double amplitude) {
        this.amplitude = amplitude;
        return this;
    }

    public SimplexOctaveNoise setFrequency(double frequency) {
        this.frequency = frequency;
        return this;
    }

    /**
     * A multiplier that determines how quickly the frequency increases for each successive octave.
     * @param lacunarity the value
     * @return this object
     */
    public SimplexOctaveNoise setLacunarity(double lacunarity) {
        this.lacunarity = lacunarity;
        return this;
    }

    /**
     * A multiplier that determines how quickly the amplitudes diminish for each successive octave.
     * @param persistence the value
     * @return this object
     */
    public SimplexOctaveNoise setPersistence(double persistence) {
        this.persistence = persistence;
        return this;
    }

    public SimplexOctaveNoise setNormalized(boolean normalized) {
        this.normalized = normalized;
        return this;
    }

    public double noise(double x, double y) {
        double value = 0.0;
        double amplt = amplitude;
        x *= frequency / scale;
        y *= frequency / scale;
        double max = 0;
        for (int i = 0; i < octaves; i++) {
            double n = NoiseUtil.normalize(noise.noise2(x, y)) * amplt;
            value += n;
            max += amplt;
            x *= lacunarity;
            y *= lacunarity;
            amplt *= persistence;
        }
        return normalized ? value / max : value;
    }

    public double noise(double x, double y, double z) {
        double value = 0.0;
        double amplt = amplitude;
        x *= frequency / scale;
        y *= frequency / scale;
        z *= frequency / scale;
        double max = 0;
        for (int i = 0; i < octaves; i++) {
            double n = NoiseUtil.normalize(noise.noise3_Classic(x, y, z)) * amplt;
            value += n;
            max += amplt;
            x *= lacunarity;
            y *= lacunarity;
            z *= lacunarity;
            amplt *= persistence;
        }
        return normalized ? value / max : value;
    }
}
