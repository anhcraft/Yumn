package dev.anhcraft.yumn.biomes;

import dev.anhcraft.jvmkit.utils.MathUtil;
import dev.anhcraft.jvmkit.utils.PresentPair;
import dev.anhcraft.yumn.biomes.land.LandBiomes;
import dev.anhcraft.yumn.biomes.ocean.OceanBiomes;
import dev.anhcraft.yumn.generators.Context;
import dev.anhcraft.yumn.generators.overworld.OverworldContext;
import dev.anhcraft.yumn.utils.DoublePair;
import dev.anhcraft.yumn.utils.Logger;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class BiomeManager {
    private static final Logger LOGGER = new Logger("BiomeManager");
    private static final BiomeManager INSTANCE = new BiomeManager();
    private final BiomeCategory[] biomeCategories = new BiomeCategory[]{
            new OceanBiomes(),
            new LandBiomes()
    };
    private static boolean debug = false;

    static {
        try {
            Material.CHEST.createBlockData();
        } catch (Exception e) {
            debug = true;
            boolean ok = true;
            System.out.println("--------------------------------------------------------------");
            System.out.println("Starting biome validation...");
            for (BiomeCategory bc : INSTANCE.biomeCategories) {
                System.out.println("> Checking category " + bc.getClass().getSimpleName());
                List<YumnBiome<?>> biomes = bc.getBiomes();
                System.out.println("1. Temperature");
                biomes.sort(Comparator.comparingDouble(YumnBiome::getTemperature));
                double last = 0;
                for (int i = 0; i < biomes.size(); i++) {
                    YumnBiome<?> b = biomes.get(i);
                    if (i == 0) {
                        last = b.getTemperature();
                        System.out.println(String.format("* %s: %.2f", b.getClass().getSimpleName(), last));
                    } else {
                        double c = b.getTemperature();
                        double x = MathUtil.round(c - last, 3);
                        if (x <= 0) {
                            System.out.println(String.format("* %s: %.2f; Δ = %.2f (delta <= 0)", b.getClass().getSimpleName(), c, x));
                            ok = false;
                        } else {
                            double q = x/last*100d;
                            if (q > 400) {
                                System.out.println(String.format("* %s: %.2f; Δ = %.2f (%.2f%% > 400%%!!!) # Something wrong here...", b.getClass().getSimpleName(), c, x, q));
                                ok = false;
                            } else if (q > 200) {
                                System.out.println(String.format("* %s: %.2f; Δ = %.2f (%.2f%% > 200%%!!!)", b.getClass().getSimpleName(), c, x, q));
                            } else {
                                System.out.println(String.format("* %s: %.2f; Δ = %.2f (%.2f%%)", b.getClass().getSimpleName(), c, x, q));
                            }
                        }
                        last = x;
                    }
                }
                System.out.println("2. Precipitation");
                biomes.sort(Comparator.comparingDouble(YumnBiome::getPrecipitation));
                last = 0;
                for (int i = 0; i < biomes.size(); i++) {
                    YumnBiome<?> b = biomes.get(i);
                    if (i == 0) {
                        last = b.getPrecipitation();
                        System.out.println(String.format("* %s: %.2f", b.getClass().getSimpleName(), last));
                    } else {
                        double c = b.getPrecipitation();
                        double x = MathUtil.round(c - last, 3);
                        if (x <= 0) {
                            System.out.println(String.format("* %s: %.2f; Δ = %.2f (delta <= 0)", b.getClass().getSimpleName(), c, x));
                            ok = false;
                        } else {
                            double q = x/last*100d;
                            if (q > 400) {
                                System.out.println(String.format("* %s: %.2f; Δ = %.2f (%.2f%% > 400%%!!!) # Something wrong here...", b.getClass().getSimpleName(), c, x, q));
                                ok = false;
                            } else if (q > 200) {
                                System.out.println(String.format("* %s: %.2f; Δ = %.2f (%.2f%% > 200%%!!!)", b.getClass().getSimpleName(), c, x, q));
                            } else {
                                System.out.println(String.format("* %s: %.2f; Δ = %.2f (%.2f%%)", b.getClass().getSimpleName(), c, x, q));
                            }
                        }
                        last = x;
                    }
                }
            }
            if(!ok){
                System.out.println("Please fix those problems first.");
                System.out.println("--------------------------------------------------------------");
                throw new IllegalStateException();
            } else {
                System.out.println("--------------------------------------------------------------");
            }
        }
    }

    @NotNull
    public static BiomeManager getInstance() {
        return INSTANCE;
    }

    public <T extends Context<?>> PresentPair<Double, BiomeCategory> localizeBiome(@NotNull T context, double continentNoise) {
        if (context instanceof OverworldContext) {
            final double mid = 0.6;
            if(continentNoise <= mid) {
                return new PresentPair<>(continentNoise / mid, biomeCategories[0]);
            } else {
                return new PresentPair<>((continentNoise - mid) / mid, biomeCategories[1]);
            }
        }
        throw new UnsupportedOperationException();
    }

    public static double a = 0, b = 0;
    public static double c = 0, d = 0;
    public static double e = 0, f = 0;

    @NotNull
    public <T extends Context<?>> YumnBiome<T> pickBiome(@NotNull T context, DoublePair noise, double precipitation, double chance){
        if(debug) {
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
        PresentPair<Double, BiomeCategory> x = localizeBiome(context, noise.getFirst());
        YumnBiome<?> biome = x.getSecond().pickBiome(x.getFirst(), 1 - noise.getSecond(), precipitation, chance);
        if(biome == null) {
            LOGGER.log("Failed to pick biome {noise=%.3f,%.3f; pcp=%.3f; chance=%.3f} => {localAlt=%.3f; biome=null}",
                    noise.getFirst(), noise.getSecond(), precipitation, chance, x.getFirst());
            throw new IllegalStateException("Failed to pick biome!");
        } /*else {
            LOGGER.log("Picked biome {noise=%.3f,%.3f; pcp=%.3f; chance=%.3f} => {localAlt=%.3f; biome=%s}",
                    noise.getFirst(), noise.getSecond(), precipitation, chance, x.getFirst(), biome.getClass().getSimpleName());
        }*/
        //noinspection unchecked
        return (YumnBiome<T>) biome;
    }
}
