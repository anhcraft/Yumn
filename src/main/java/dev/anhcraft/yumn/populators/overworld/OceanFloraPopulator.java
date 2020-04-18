package dev.anhcraft.yumn.populators.overworld;

import dev.anhcraft.yumn.generators.overworld.OverworldGenerator;
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
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.CoralWallFan;
import org.bukkit.block.data.type.SeaPickle;
import org.jetbrains.annotations.NotNull;

public class OceanFloraPopulator extends OverworldPopulator {
    private static final int MAX_DISTANCE_WATER_LEVEL = 15;
    private static final int MIN_DISTANCE_WATER_LEVEL = 5;
    private static final int MAX_CORAL_CREATION_TIMES = 10;
    private static final int MIN_CORAL_CREATION_TIMES = 5;

    public OceanFloraPopulator(OverworldGenerator generator) {
        super(generator);
    }

    @Override
    public void populate(@NotNull World world, @NotNull XoRoShiRo128StarStarRandomGenerator random, @NotNull Chunk chunk) {
        for(int x = 0; x < 16; x++){
            for(int z = 0; z < 16; z++) {
                if(random.nextInt(16) != 0) continue;
                Block center = new Location(
                        world,
                        x + (chunk.getX() << 4),
                        getGenerator().OCEAN_SURFACE_LEVEL,
                        z + (chunk.getZ() << 4)
                ).getBlock();
                boolean generateCoral = BiomeCollection.WARM_OCEAN.contains(center.getBiome()) && random.nextInt(10) >= 7;
                if(generateCoral) {
                    Block b = center;
                    int y = b.getY();
                    outer:
                    while(y++ < getGenerator().WATER_LEVEL - 5){
                        if(!BiomeCollection.AQUATIC.contains(b.getBiome())) break;
                        if(b.getType() != Material.WATER) {
                            b = b.getRelative(BlockFace.UP);
                            continue;
                        }
                        Material coral = MaterialCollection.CORAL_BLOCK.randomPick(random);
                        int mc = RandomUtil.rand(random, MIN_CORAL_CREATION_TIMES, MAX_CORAL_CREATION_TIMES);
                        for (int i = 0; i < mc; i++) {
                            if(b.getType() != Material.WATER) break outer;
                            FastBlockModifier.change(b, coral);
                            boolean genSeaPickle = i == mc - 1 && random.nextInt(15) == 0;
                            if(random.nextInt(7) > 3) {
                                if(random.nextInt(10) > 6) {
                                    BlockFace bf = RandomUtil.randHorizontalBlockFace(random);
                                    Block rel = b.getRelative(bf);
                                    if(rel.getType() != Material.WATER) break outer;
                                    Material mt = MaterialCollection.CORAL_WALL_FAN.randomPick(random);
                                    CoralWallFan coralWallFan = (CoralWallFan) mt.createBlockData();
                                    coralWallFan.setFacing(bf);
                                    FastBlockModifier.change(rel, coralWallFan);
                                }
                                if(!genSeaPickle) {
                                    b = b.getRelative(BlockFace.UP);
                                    continue;
                                }
                            }
                            else {
                                if(random.nextInt(7) > 5) {
                                    Block rel = b.getRelative(BlockFace.UP);
                                    if(rel.getType() != Material.WATER) break outer;
                                    FastBlockModifier.change(rel, MaterialCollection.CORAL_FAN.randomPick(random));
                                }
                                // dont move up!
                                b = b.getRelative(RandomUtil.randHorizontalBlockFace(random));
                                if(!genSeaPickle) continue;
                            }

                            Block rel = b.getRelative(BlockFace.UP);
                            if(rel.getType() == Material.WATER) {
                                SeaPickle seaPickle = (SeaPickle) Material.SEA_PICKLE.createBlockData();
                                seaPickle.setPickles(RandomUtil.rand(random, seaPickle.getMinimumPickles(), seaPickle.getMaximumPickles()));
                                FastBlockModifier.change(rel, seaPickle);
                            }
                        }
                        break; // only create one-time!!!
                    }
                } else {
                    int y = getGenerator().WATER_LEVEL - RandomUtil.rand(random, MIN_DISTANCE_WATER_LEVEL, MAX_DISTANCE_WATER_LEVEL);
                    Block block = new Location(world,
                            x + (chunk.getX() << 4),
                            y,
                            z + (chunk.getZ() << 4)
                    ).getBlock();
                    if(block.getType() != Material.WATER) continue;
                    // 0: kelp plant
                    // 1: kelp
                    // 2: coral
                    // 3: sea grass
                    // 4: tall sea grass
                    // 5: soulsand / magma block
                    int generateType = RandomUtil.rand(random, 0, 3);
                    while (y-- >= getGenerator().OCEAN_SURFACE_LEVEL) {
                        if(!BiomeCollection.AQUATIC.contains(block.getBiome())) break;
                        if(generateType == 0) {
                            if(block.getType() != Material.WATER) break;
                            FastBlockModifier.change(block, Material.KELP_PLANT);
                        } else {
                            if(block.getType() == Material.WATER && block.getRelative(BlockFace.DOWN).getType() == Material.WATER) continue;
                            if(generateType == 1){
                                Ageable age = (Ageable) Material.KELP.createBlockData();
                                age.setAge(age.getMaximumAge());
                                FastBlockModifier.change(block, age);
                            } else if(generateType == 2){
                                FastBlockModifier.change(block, MaterialCollection.CORAL.randomPick(random));
                            } else if(generateType == 3) {
                                FastBlockModifier.change(block, Material.SEAGRASS);
                            } else if(generateType == 4) {
                                FastBlockModifier.change(block, Material.TALL_SEAGRASS);
                                FastBlockModifier.change(block.getRelative(BlockFace.UP), Material.TALL_SEAGRASS);
                            } else {
                                FastBlockModifier.change(block.getRelative(BlockFace.DOWN), random.nextInt(15) > 4 ? Material.MAGMA_BLOCK : Material.SOUL_SAND);
                            }
                            break;
                        }
                        block = block.getRelative(BlockFace.DOWN);
                    }
                }
            }
        }
    }
}
