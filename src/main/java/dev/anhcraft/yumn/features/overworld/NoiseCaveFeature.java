package dev.anhcraft.yumn.features.overworld;

import dev.anhcraft.yumn.features.YumnFeature;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.heightmaps.HeightMapCell;
import dev.anhcraft.yumn.utils.noise.OctaveNoiseGenerator;
import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.Material;

public class NoiseCaveFeature extends YumnFeature {
    private final OctaveNoiseGenerator holeNoise;
    private final OctaveNoiseGenerator surfaceNoise;
    private final XoRoShiRo128StarStarRandomGenerator randomizer;

    public NoiseCaveFeature(Context context) {
        super(context);

        holeNoise = new OctaveNoiseGenerator(context.getSeed())
                .setScale(100)
                .setFrequency(0.7)
                .setAmplitude(0.179)
                .setLacunarity(1.165)
                .setPersistence(1.106)
                .setOctaves(4)
                .setNormalized(false);
        surfaceNoise = new OctaveNoiseGenerator(context.getSeed())
                .setScale(30)
                .setFrequency(0.5)
                .setAmplitude(1)
                .setLacunarity(1.5)
                .setPersistence(0.3)
                .setOctaves(3)
                .setNormalized(false);
        randomizer = new XoRoShiRo128StarStarRandomGenerator(context.getSeed());
    }

    @Override
    public void implement(int localX, int localZ, HeightMapCell cell) {
        int x = (getContext().getChunkX() << 4) + localX;
        int z = (getContext().getChunkZ() << 4) + localZ;
        int min = (int) (surfaceNoise.noise(x, z) * 15 + 3);
        int max = Math.max(getContext().getWorldGenerator().WATER_LEVEL, getContext().getWorldGenerator().getHeight(cell.getNoise()) + 1);
        boolean wasSolid = true;
        for (int i = min; i <= max; i++) {
            double v = holeNoise.noise(x, i, z);
            v += Math.pow(holeNoise.noise(x << 2, z << 2), 5);
            v += Math.sin(Math.pow(Math.E, v) * v * 0.3);
            if (v <= 0.3) {
                if(i <= getContext().getWorldGenerator().WATER_LEVEL) {
                    if(wasSolid && randomizer.nextInt(5) == 0) {
                        getContext().getChunkData().setBlock(localX, i - 1, localZ, Material.MAGMA_BLOCK);
                    }
                    getContext().getChunkData().setBlock(localX, i, localZ, Material.WATER);
                } else {
                    getContext().getChunkData().setBlock(localX, i, localZ, Material.CAVE_AIR);
                }
                wasSolid = false;
            } else {
                wasSolid = true;
            }
        }
    }
}
