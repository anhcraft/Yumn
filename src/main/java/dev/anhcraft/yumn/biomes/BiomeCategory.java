package dev.anhcraft.yumn.biomes;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.doubles.Double2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BiomeCategory {
    private final Int2ObjectMap<YumnBiome<?>> biomes = new Int2ObjectOpenHashMap<>();
    private final Double2ObjectSortedMap<BiomeChoices> biomeRanking = new Double2ObjectAVLTreeMap<>();
    private double maxAltitude;

    protected void registerBiome(int index, Class<? extends YumnBiome<?>> biomeClass){
        try {
            biomes.put(index, biomeClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void registerGroup(double altitude, int... biomeIndexes){
        Object2ObjectMap<YumnBiome<?>, BiomeChoice> biomeList = new Object2ObjectOpenHashMap<>();
        double maxTemperature = 0;
        double maxPrecipitation = 0;
        double chanceUnit = 1d / biomeIndexes.length;
        double chanceSum = 0;
        for (int biomeIndex : biomeIndexes){
            YumnBiome<?> biome = biomes.get(biomeIndex);
            if(biome == null){
                continue;
            }
            BiomeChoice old = biomeList.get(biome);
            chanceSum += chanceUnit;
            if(old != null){
                old.setChance(old.getChance() + chanceUnit);
            } else {
                old = new BiomeChoice(biome);
                old.setChance(chanceSum);
                biomeList.put(biome, old);
            }
            maxTemperature = Math.max(maxTemperature, biome.getTemperature());
            maxPrecipitation = Math.max(maxPrecipitation, biome.getPrecipitation());
        }
        biomeRanking.put(altitude, new BiomeChoices(altitude, maxTemperature, maxPrecipitation, chanceSum, ImmutableSet.copyOf(biomeList.values())));
        maxAltitude = Math.max(maxAltitude, altitude);
    }

    @Nullable
    public YumnBiome<?> pickBiome(double localAltitude, double temperature, double precipitation, double chance){
        for (Map.Entry<Double, BiomeChoices> e : biomeRanking.double2ObjectEntrySet()) {
            if(localAltitude * maxAltitude <= e.getKey()) {
                BiomeChoices group = e.getValue();
                double t = temperature * group.getMaxTemperature();
                double p = precipitation * group.getMaxPrecipitation();
                double c = chance * group.getMaxChance();
                double pt = t;
                double pp = p;
                double pc = c;
                BiomeChoice b = null;
                for (BiomeChoice biome : group.getBiomes()) {
                    double tt = biome.getBiome().getTemperature();
                    double tp = biome.getBiome().getPrecipitation();
                    double tc = biome.getChance();
                    if (t <= tt && p <= tp && c <= tc) {
                        if(b != null && (tt >= pt || tp >= pp || tc >= pc)) continue;
                        b = biome;
                        pt = tt;
                        pp = tp;
                        pc = tc;
                    }
                }
                return (b == null ? group.getBiomes().iterator().next() : b).getBiome();
            }
        }
        return null;
    }

    @NotNull
    public List<YumnBiome<?>> getBiomes(){
        return new ArrayList<>(biomes.values());
    }
}
