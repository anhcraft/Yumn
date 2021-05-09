package dev.anhcraft.yumn.biomes;

import dev.anhcraft.jvmkit.utils.MathUtil;
import dev.anhcraft.jvmkit.utils.PresentPair;
import dev.anhcraft.yumn.biomes.land.LandBiomes;
import dev.anhcraft.yumn.biomes.ocean.OceanBiomes;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.utils.Logger;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class BiomeManager {
    private static final Logger LOGGER = new Logger("BiomeManager");
    private static final BiomeManager INSTANCE = new BiomeManager();
    public static double a = 0, b = 0;
    public static double c = 0, d = 0;
    public static double e = 0, f = 0;
    private static boolean debug = false;

    static {
        try {
            Material.CHEST.createBlockData();
        } catch (Exception e) {
            debug = true;
            boolean ok = true;
            LOGGER.logf("--------------------------------------------------------------");
            LOGGER.logf("Starting biome validation...");
            for (BiomeCategory bc : INSTANCE.biomeCategories) {
                LOGGER.logf("> Checking category " + bc.getClass().getSimpleName());
                List<YumnBiome> biomes = bc.getBiomes();
                LOGGER.logf("1. Temperature");
                biomes.sort(Comparator.comparingDouble(YumnBiome::getTemperature));
                double last = 0;
                for (int i = 0; i < biomes.size(); i++) {
                    YumnBiome b = biomes.get(i);
                    if (i == 0) {
                        last = b.getTemperature();
                        LOGGER.logf("* %s: %.2f", b.getClass().getSimpleName(), last);
                    } else {
                        double c = b.getTemperature();
                        double x = MathUtil.round(c - last, 3);
                        if (x <= 0) {
                            LOGGER.logf("* %s: %.2f; Δ = %.2f (delta <= 0)", b.getClass().getSimpleName(), c, x);
                            ok = false;
                        } else {
                            double q = x / last * 100d;
                            if (q > 600) {
                                LOGGER.logf("* %s: %.2f; Δ = %.2f (%.2f%% > 600%%!!!) # Something wrong here...", b.getClass().getSimpleName(), c, x, q);
                                ok = false;
                            } else if (q > 400) {
                                LOGGER.logf("* %s: %.2f; Δ = %.2f (%.2f%% > 400%%!!!)", b.getClass().getSimpleName(), c, x, q);
                            } else {
                                LOGGER.logf("* %s: %.2f; Δ = %.2f (%.2f%%)", b.getClass().getSimpleName(), c, x, q);
                            }
                        }
                        last = x;
                    }
                }
                LOGGER.logf("2. Precipitation");
                biomes.sort(Comparator.comparingDouble(YumnBiome::getPrecipitation));
                last = 0;
                for (int i = 0; i < biomes.size(); i++) {
                    YumnBiome b = biomes.get(i);
                    if (i == 0) {
                        last = b.getPrecipitation();
                        LOGGER.logf("* %s: %.2f", b.getClass().getSimpleName(), last);
                    } else {
                        double c = b.getPrecipitation();
                        double x = MathUtil.round(c - last, 3);
                        if (x <= 0) {
                            LOGGER.logf("* %s: %.2f; Δ = %.2f (delta <= 0)", b.getClass().getSimpleName(), c, x);
                            ok = false;
                        } else {
                            double q = x / last * 100d;
                            if (q > 600) {
                                LOGGER.logf("* %s: %.2f; Δ = %.2f (%.2f%% > 600%%!!!) # Something wrong here...", b.getClass().getSimpleName(), c, x, q);
                                ok = false;
                            } else if (q > 400) {
                                LOGGER.logf("* %s: %.2f; Δ = %.2f (%.2f%% > 400%%!!!)", b.getClass().getSimpleName(), c, x, q);
                            } else {
                                LOGGER.logf("* %s: %.2f; Δ = %.2f (%.2f%%)", b.getClass().getSimpleName(), c, x, q);
                            }
                        }
                        last = x;
                    }
                }
            }
            if (!ok) {
                LOGGER.log("Please fix those problems first.");
                LOGGER.log("--------------------------------------------------------------");
                throw new IllegalStateException();
            } else {
                LOGGER.log("--------------------------------------------------------------");
            }
        }
    }

    private final BiomeCategory[] biomeCategories = new BiomeCategory[]{
            new OceanBiomes(),
            new LandBiomes()
    };

    @NotNull
    public static BiomeManager getInstance() {
        return INSTANCE;
    }

    public PresentPair<Double, BiomeCategory> localizeBiome(@NotNull Context context, double noise) {
        final double mid = 0.3;
        if (noise <= mid) {
            return new PresentPair<>(noise / mid, biomeCategories[0]);
        } else {
            return new PresentPair<>((noise - mid) / (1 - mid), biomeCategories[1]);
        }
    }

    @NotNull
    public YumnBiome pickBiome(@NotNull Context context, double noise, double precipitation, double chance) {
        if (debug) {
            c = Math.max(c, precipitation);
            d = Math.max(d, chance);
            if (a == 0 || b == 0) {
                a = precipitation;
                b = chance;
                e = precipitation;
                f = chance;
            } else {
                a = (a + precipitation) / 2;
                b = (b + chance) / 2;
                e = Math.min(e, precipitation);
                f = Math.min(f, chance);
            }
        }
        PresentPair<Double, BiomeCategory> x = localizeBiome(context, noise);
        YumnBiome biome = x.getSecond().pickBiome(x.getFirst(), 1 - noise, precipitation, chance);
        if (biome == null) {
            LOGGER.logf("Failed to pick biome {noise=%.3f; pcp=%.3f; chance=%.3f} => {localAlt=%.3f; biome=null}",
                    noise, precipitation, chance, x.getFirst());
            throw new IllegalStateException("Failed to pick biome!");
        } /*else {
            LOGGER.log("Picked biome {noise=%.3f,%.3f; pcp=%.3f; chance=%.3f} => {localAlt=%.3f; biome=%s}",
                    noise.getFirst(), noise.getSecond(), precipitation, chance, x.getFirst(), biome.getClass().getSimpleName());
        }*/
        return biome;
    }
}
