package dev.anhcraft.yumn.nms;

import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R1.block.data.CraftBlockData;

public class FastBlockModifier {
    public static IBlockData change(org.bukkit.World world, int x, int y, int z, Material material){
        return change(world, new BlockPosition(x, y, z), material.createBlockData());
    }

    public static IBlockData change(org.bukkit.World world, int x, int y, int z, BlockData blockData){
        return change(world, new BlockPosition(x, y, z), blockData);
    }

    public static IBlockData change(org.bukkit.block.Block block, Material material){
        return change(block.getWorld(), ((CraftBlock) block).getPosition(), material.createBlockData());
    }

    public static IBlockData change(org.bukkit.block.Block block, BlockData blockData){
        return change(block.getWorld(), ((CraftBlock) block).getPosition(), blockData);
    }

    public static IBlockData change(org.bukkit.World world, BlockPosition position, Material material){
        return change(world, position, material.createBlockData());
    }

    public static IBlockData change(World world, BlockPosition position, BlockData blockData){
        Chunk nmsChunk = world.getChunkAt(position.getX() >> 4, position.getZ() >> 4);
        IBlockData ibd = ((CraftBlockData) blockData).getState();
        return setType(position, ibd, true, nmsChunk);
    }

    public static IBlockData change(org.bukkit.World world, BlockPosition position, BlockData blockData){
        World nmsWorld = ((CraftWorld) world).getHandle();
        Chunk nmsChunk = nmsWorld.getChunkAt(position.getX() >> 4, position.getZ() >> 4);
        IBlockData ibd = ((CraftBlockData) blockData).getState();
        return setType(position, ibd, true, nmsChunk);
    }

    public static IBlockData setType(BlockPosition blockposition, IBlockData iblockdata, boolean updateLight, Chunk chunk) {
        int i = blockposition.getX() & 15;
        int j = blockposition.getY();
        int k = blockposition.getZ() & 15;
        ChunkSection chunksection = chunk.getSections()[j >> 4];
        if (chunksection == Chunk.a) {
            if (iblockdata.isAir()) {
                return null;
            }

            chunksection = new ChunkSection(j >> 4 << 4);
            chunk.getSections()[j >> 4] = chunksection;
        }

        IBlockData iblockdata1 = chunksection.setType(i, j & 15, k, iblockdata);
        if (iblockdata1 == iblockdata) {
            return null;
        } else {
            Block block = iblockdata.getBlock();
            Block block1 = iblockdata1.getBlock();
            chunk.heightMap.get(HeightMap.Type.MOTION_BLOCKING).a(i, j, k, iblockdata);
            chunk.heightMap.get(HeightMap.Type.MOTION_BLOCKING_NO_LEAVES).a(i, j, k, iblockdata);
            chunk.heightMap.get(HeightMap.Type.OCEAN_FLOOR).a(i, j, k, iblockdata);
            chunk.heightMap.get(HeightMap.Type.WORLD_SURFACE).a(i, j, k, iblockdata);

            if(updateLight) {
                chunk.world.getChunkProvider().getLightEngine().a(blockposition, true);
            }

            if (!chunk.world.isClientSide) {
                // normal call: iblockdata1.remove(chunk.world, blockposition, iblockdata, flag);
                // special code from the call (without async check):
                if (block.isTileEntity() && iblockdata.getBlock() != iblockdata1.getBlock()) {
                    chunk.world.removeTileEntity(blockposition);
                }
            } else if (block1 != block && block1 instanceof ITileEntity) {
                chunk.world.removeTileEntity(blockposition);
            }

            if (chunksection.getType(i, j & 15, k).getBlock() != block) {
                return null;
            } else {
                TileEntity tileentity;
                if (block1 instanceof ITileEntity) {
                    tileentity = chunk.a(blockposition, Chunk.EnumTileEntityState.CHECK);
                    if (tileentity != null) {
                        tileentity.invalidateBlockCache();
                    }
                }

                /*if (!chunk.world.isClientSide && doPlace && (!chunk.world.captureBlockStates || block instanceof BlockTileEntity)) {
                    iblockdata.onPlace(chunk.world, blockposition, iblockdata1, flag);
                }*/

                if (block instanceof ITileEntity) {
                    tileentity = chunk.a(blockposition, Chunk.EnumTileEntityState.CHECK);
                    if (tileentity == null) {
                        tileentity = ((ITileEntity)block).createTile(chunk.world);
                        chunk.world.setTileEntity(blockposition, tileentity);
                    } else {
                        tileentity.invalidateBlockCache();
                    }
                }

                chunk.setNeedsSaving(true);
                return iblockdata1;
            }
        }
    }
}
