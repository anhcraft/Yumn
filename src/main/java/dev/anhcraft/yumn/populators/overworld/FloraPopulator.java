package dev.anhcraft.yumn.populators.overworld;

import dev.anhcraft.yumn.Structure;
import dev.anhcraft.yumn.generators.WorldGenerator;
import dev.anhcraft.yumn.nms.FastBlockModifier;
import dev.anhcraft.yumn.populators.OverworldPopulator;
import dev.anhcraft.yumn.utils.BiomeCollection;
import dev.anhcraft.yumn.utils.MaterialCollection;
import dev.anhcraft.yumn.utils.RandomUtil;
import it.unimi.dsi.util.XoRoShiRo128StarStarRandomGenerator;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Bamboo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FloraPopulator extends OverworldPopulator {
    private static final List<Material> F1_FLOWERS = MaterialCollection.TULIP.stream()
            .filter(m -> !MaterialCollection.POTTED.contains(m))
            .collect(Collectors.toList());

    static {
        F1_FLOWERS.add(Material.DANDELION);
        F1_FLOWERS.add(Material.ALLIUM);
        F1_FLOWERS.add(Material.POPPY);
        F1_FLOWERS.add(Material.AZURE_BLUET);
        F1_FLOWERS.add(Material.OXEYE_DAISY);
        F1_FLOWERS.add(Material.BLUE_ORCHID);
    }

    public FloraPopulator(WorldGenerator generator) {
        super(generator);
    }

    @Override
    public void populate(@NotNull World world, @NotNull XoRoShiRo128StarStarRandomGenerator random, @NotNull Chunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (random.nextInt(6) != 0) continue;
                int y = getGenerator().LAND_LEVEL;
                Location loc = new Location(
                        world,
                        x + (chunk.getX() << 4),
                        y,
                        z + (chunk.getZ() << 4)
                );
                for (; y <= getGenerator().getMaxNaturalHeight(); y++) {
                    loc.setY(y);
                    Block b = loc.getBlock();
                    if (b.getType() == Material.GRASS_BLOCK) {
                        Biome biome = b.getBiome();
                        if (biome == Biome.PLAINS && random.nextInt(150) == 0) {
                            Structure.OAK_TREE_1.spawn(loc.clone().add(0, 1, 0), true);
                        } else if (biome == Biome.BAMBOO_JUNGLE || biome == Biome.BAMBOO_JUNGLE_HILLS) {
                            int h = RandomUtil.rand(random, 12, 18);
                            int hh = h - RandomUtil.rand(random, 3, 5);
                            for (int i = 0; i < h; i++) {
                                if ((b = b.getRelative(BlockFace.UP)).isEmpty()) {
                                    if (i >= hh) {
                                        Bamboo bb = (Bamboo) Material.BAMBOO.createBlockData();
                                        bb.setLeaves(Bamboo.Leaves.values()[random.nextInt(Bamboo.Leaves.values().length)]);
                                        FastBlockModifier.change(b, bb);
                                    } else {
                                        FastBlockModifier.change(b, Material.BAMBOO);
                                    }
                                    y++;
                                } else {
                                    break;
                                }
                            }
                        } else if (biome == Biome.BEACH || biome == Biome.SNOWY_BEACH || BiomeCollection.AQUATIC.contains(biome)) {
                            int h = RandomUtil.rand(random, 3, 5);
                            for (int i = 0; i < h; i++) {
                                if ((b = b.getRelative(BlockFace.UP)).isEmpty()) {
                                    FastBlockModifier.change(b, Material.SUGAR_CANE);
                                    y++;
                                } else {
                                    break;
                                }
                            }
                        } else if ((biome == Biome.FOREST || biome == Biome.FLOWER_FOREST) && random.nextInt(30) == 0) {
                            Structure structure = null;
                            switch (random.nextInt(12)) {
                                case 0:
                                case 1:
                                case 2:
                                case 3: {
                                    structure = Structure.OAK_TREE_3;
                                    break;
                                }
                                case 4:
                                case 5:
                                case 6: {
                                    structure = Structure.OAK_TREE_2;
                                    break;
                                }
                                case 7:
                                case 8:
                                case 9: {
                                    structure = Structure.OAK_TREE_1;
                                    break;
                                }
                                case 10: {
                                    structure = Structure.FALLEN_OAK_LOG_1;
                                    break;
                                }
                                case 11: {
                                    structure = Structure.FALLEN_OAK_LOG_2;
                                    break;
                                }
                            }
                            Objects.requireNonNull(structure).spawn(loc.clone().add(0, 1, 0), true);
                        } else if ((biome == Biome.BIRCH_FOREST || biome == Biome.BIRCH_FOREST_HILLS) && random.nextInt(30) == 0) {
                            Structure structure = null;
                            switch (random.nextInt(7)) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4: {
                                    structure = Structure.BIRCH_TREE_1;
                                    break;
                                }
                                case 5: {
                                    structure = Structure.FALLEN_BIRCH_LOG_1;
                                    break;
                                }
                                case 6: {
                                    structure = Structure.FALLEN_BIRCH_LOG_2;
                                    break;
                                }
                            }
                            Objects.requireNonNull(structure).spawn(loc.clone().add(0, 1, 0), true);
                        } else if ((biome == Biome.SWAMP || biome == Biome.SWAMP_HILLS) && random.nextInt(30) == 0) {
                            Structure.SWAMP_OAK_TREE_1.spawn(loc.clone().add(0, 1, 0), true);
                        } else if ((biome == Biome.JUNGLE || biome == Biome.JUNGLE_HILLS) && random.nextInt(20) == 0) {
                            if (random.nextInt(6) == 0) {
                                Structure.JUNGLE_TREE_1.spawn(loc.clone().add(0, 1, 0), true);
                            } else {
                                FastBlockModifier.change(b.getRelative(BlockFace.UP), Material.JUNGLE_LEAVES);
                            }
                        } else if (biome == Biome.SUNFLOWER_PLAINS && random.nextInt(8) == 0) {
                            b = b.getRelative(BlockFace.UP);
                            Bisected bisected = (Bisected) Material.SUNFLOWER.createBlockData();
                            bisected.setHalf(Bisected.Half.BOTTOM);
                            FastBlockModifier.change(b, bisected);
                            b = b.getRelative(BlockFace.UP);
                            bisected = (Bisected) Material.SUNFLOWER.createBlockData();
                            bisected.setHalf(Bisected.Half.TOP);
                            FastBlockModifier.change(b, bisected);
                        } else if (biome == Biome.SUNFLOWER_PLAINS && random.nextInt(85) == 0) {
                            Structure.OAK_TREE_1.spawn(loc.clone().add(0, 1, 0), true);
                        } else {
                            int v;
                            if (biome == Biome.FLOWER_FOREST) {
                                v = RandomUtil.rand(random, 1, 120);
                            } else if (BiomeCollection.FOREST.contains(biome)) {
                                v = RandomUtil.rand(random, 1, 105);
                            } else {
                                v = RandomUtil.rand(random, 1, 100);
                            }
                            b = b.getRelative(BlockFace.UP);
                            if (b.isEmpty()) {
                                if (v <= 98) {
                                    FastBlockModifier.change(b, Material.GRASS);
                                } else {
                                    FastBlockModifier.change(b, F1_FLOWERS.get(random.nextInt(F1_FLOWERS.size())));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
