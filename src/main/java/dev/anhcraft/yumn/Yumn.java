package dev.anhcraft.yumn;

import co.aikar.commands.PaperCommandManager;
import dev.anhcraft.jvmkit.utils.FileUtil;
import dev.anhcraft.jvmkit.utils.IOUtil;
import dev.anhcraft.jvmkit.utils.Pair;
import dev.anhcraft.jvmkit.utils.function.ByteArraySupplier;
import dev.anhcraft.yumn.biomes.BiomeManager;
import dev.anhcraft.yumn.commands.YumnCommand;
import dev.anhcraft.yumn.events.EventListener;
import dev.anhcraft.yumn.generators.overworld.OverworldGenerator;
import dev.anhcraft.yumn.nms.StructureManager;
import dev.anhcraft.yumn.utils.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Yumn extends JavaPlugin implements Listener {
    private static Yumn instance;
    private final BiomeManager biomeManager = new BiomeManager();
    public final ScheduledExecutorService pool = Executors.newScheduledThreadPool(4);
    public final Map<UUID, Pair<Location, Location>> playerSelections = new HashMap<>();
    public File structDir;
    private ItemStack wand;

    @NotNull
    public ItemStack getWand(){
        return wand.clone();
    }

    public boolean isWand(@Nullable ItemStack item) {
        return wand.isSimilar(item);
    }

    @NotNull
    public static Yumn getInstance(){
        return instance;
    }

    @NotNull
    public BiomeManager getBiomeManager() {
        return biomeManager;
    }

    @Override
    public void onLoad() {
        instance = this;

        getLogger().warning("=== YUMN WORLD GENERATOR ===");
        getLogger().warning("[Author: anhcraft]");
        getLogger().warning("This plugin is in BETA and only accessible by certain people");
        getLogger().warning("You are allowed to use this for free, even commercial-purposes");
        getLogger().warning("However, please aware that it is in development and may cause");
        getLogger().warning("unexpected issues that include bugs, bad performance, etc.");
        getLogger().warning("However, hope you can enjoy what this plugin brings to your server");
        getLogger().warning("And finally, please do not share, redistribute, resell this plugin.");

        structDir = new File(getDataFolder(), "structures");
        //noinspection ResultOfMethodCallIgnored
        structDir.mkdirs();
        for(File file : structDir.listFiles((dir, name) -> name.endsWith(".struct"))){
            getLogger().info("Loaded structure " + StructureManager.loadStructure(file));
        }
        for (Structure s : Structure.values()) {
            if(!StructureManager.hasStructure(s.getId())) {
                getLogger().warning(s.getId()+" not found!");
            }
        }
        pool.scheduleAtFixedRate(Logger::printQueue, 1, 3, TimeUnit.SECONDS);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getScheduler().runTaskTimerAsynchronously(this, OverworldGenerator.CACHED_HEIGHT_MAPS::clean, 0, 300);

        new PaperCommandManager(this).registerCommand(new YumnCommand());

        wand = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta meta = Objects.requireNonNull(wand.getItemMeta());
        meta.setDisplayName(ChatColor.RED + "[YUMN] Selection Wand");
        meta.setUnbreakable(true);
        wand.setItemMeta(meta);
    }

    @Override
    public void onDisable() {
        pool.shutdownNow();
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, String id) {
        return new OverworldGenerator();
    }
}
