package dev.anhcraft.yumn.nms;

import com.google.common.collect.ImmutableList;
import dev.anhcraft.yumn.Yumn;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FilenameUtils;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StructureManager {
    private static final Map<String, NBTTagCompound> STRUCTURES = new HashMap<>();

    @NotNull
    public static List<String> listStructures() {
        return ImmutableList.copyOf(STRUCTURES.keySet());
    }

    @Nullable
    public static NBTTagCompound getStructure(@NotNull String name) {
        return STRUCTURES.get(name);
    }

    public static boolean hasStructure(@NotNull String name) {
        return STRUCTURES.containsKey(name);
    }

    public static void storeStructure(@NotNull String name, @NotNull NBTTagCompound data) {
        STRUCTURES.put(name, data);
    }

    public static String loadStructure(@NotNull File file) {
        String name = FilenameUtils.getBaseName(file.getName());
        try {
            NBTTagCompound compound = NBTCompressedStreamTools.a(new FileInputStream(file));
            storeStructure(name, compound);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String loadStructure(String name, InputStream stream) {
        try {
            NBTTagCompound compound = NBTCompressedStreamTools.a(stream);
            storeStructure(name, compound);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static void saveStructure(@NotNull String name) {
        NBTTagCompound compound = getStructure(name);
        if (compound == null) return;
        File f = new File(Yumn.getInstance().structDir, name + ".struct");
        try {
            //noinspection ResultOfMethodCallIgnored
            f.createNewFile();
            NBTCompressedStreamTools.a(compound, new FileOutputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createAndSaveStructure(@NotNull String name, @NotNull org.bukkit.World w, @NotNull Location first, @NotNull Location second) {
        NBTTagCompound compound = writeStructure(w, first, second);
        storeStructure(name, compound);
        File f = new File(Yumn.getInstance().structDir, name + ".struct");
        try {
            //noinspection ResultOfMethodCallIgnored
            f.createNewFile();
            NBTCompressedStreamTools.a(compound, new FileOutputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NBTTagCompound writeStructure(@NotNull org.bukkit.World w, @NotNull Location first, @NotNull Location second) {
        int minX = Math.min(first.getBlockX(), second.getBlockX());
        int maxX = Math.max(first.getBlockX(), second.getBlockX());
        int minY = Math.min(first.getBlockY(), second.getBlockY());
        int maxY = Math.max(first.getBlockY(), second.getBlockY());
        int minZ = Math.min(first.getBlockZ(), second.getBlockZ());
        int maxZ = Math.max(first.getBlockZ(), second.getBlockZ());
        World world = ((CraftWorld) w).getHandle();
        NBTTagCompound root = new NBTTagCompound();
        NBTTagCompound blocks = new NBTTagCompound();
        long i = 0;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPosition pos = new BlockPosition(x, y, z);
                    IBlockData data = world.getType(pos);
                    if (!data.isAir()) {
                        NBTTagCompound b = new NBTTagCompound();
                        b.setInt("x", x - minX);
                        b.setInt("y", y - minY);
                        b.setInt("z", z - minZ);
                        b.setString("data", CraftBlockData.fromData(data).getAsString());
                        TileEntity te = world.getTileEntity(pos);
                        if (te != null) {
                            NBTTagCompound teb = new NBTTagCompound();
                            te.save(teb);
                            teb.remove("x");
                            teb.remove("y");
                            teb.remove("z");
                            b.set("tileEntity", teb);
                        }
                        blocks.set(String.valueOf(i), b);
                        i++;
                    }
                }
            }
        }
        root.setInt("width", maxX - minX + 1);
        root.setInt("depth", maxZ - minZ + 1);
        root.setInt("height", maxY - minY + 1);
        root.set("blocks", blocks);
        return root;
    }

    public static boolean pasteStructure(@NotNull Location location, @NotNull String name, boolean fast) {
        NBTTagCompound compound = STRUCTURES.get(name);
        if (compound == null) {
            return false;
        }
        pasteStructure(location, compound, fast);
        return true;
    }

    public static void pasteStructure(@NotNull Location location, @NotNull NBTTagCompound root, boolean fast) {
        pasteStructure(Objects.requireNonNull(location.getWorld()), root, new BlockPosition(location.getX(), location.getY(), location.getZ()), fast);
    }

    public static void pasteStructure(@NotNull org.bukkit.World w, @NotNull NBTTagCompound root, @NotNull BlockPosition origin, boolean fast) {
        World world = ((CraftWorld) w).getHandle();
        NBTTagCompound blocks = root.getCompound("blocks");
        for (String s : blocks.getKeys()) {
            NBTTagCompound b = blocks.getCompound(s);
            BlockPosition pos = origin.a(
                    (double) b.getInt("x"), // to match the method a(double, double, double)
                    b.getInt("y"),
                    b.getInt("z")
            );
            if (fast) {
                FastBlockModifier.change(world, pos, Bukkit.createBlockData(b.getString("data")));
            } else {
                IBlockData old = world.getType(pos);
                IBlockData blockData = ((CraftBlockData) Bukkit.createBlockData(b.getString("data"))).getState();
                boolean success = world.setTypeAndData(pos, blockData, 1042);
                if (success) {
                    world.getMinecraftWorld().notify(pos, old, blockData, 3);
                }
            }
            TileEntity te = world.getTileEntity(pos);
            if (te != null) {
                NBTTagCompound tileEntity = b.getCompound("tileEntity").clone();
                tileEntity.setInt("x", pos.getX());
                tileEntity.setInt("y", pos.getY());
                tileEntity.setInt("z", pos.getZ());
                world.setTileEntity(pos, TileEntity.create(world.getType(pos), tileEntity));
            }
        }
    }
}
