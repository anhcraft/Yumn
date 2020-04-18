package dev.anhcraft.yumn.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import dev.anhcraft.jvmkit.utils.Pair;
import dev.anhcraft.yumn.Yumn;
import dev.anhcraft.yumn.nms.StructureManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.Objects;

@CommandAlias("yumn|ym")
public class YumnCommand extends BaseCommand {
    @Subcommand("pos1")
    @CommandPermission("yumn.pos1")
    public void pos1(Player player) {
        Location location = player.getLocation();
        Pair<Location, Location> pair = Yumn.getInstance().playerSelections.get(player.getUniqueId());
        if(pair == null) {
            pair = new Pair<>(location, null);
            Yumn.getInstance().playerSelections.put(player.getUniqueId(), pair);
        } else {
            pair.setFirst(location);
        }
        player.sendMessage(ChatColor.GREEN + "Position 1 has been set to " + String.format("%s %s %s %s", Objects.requireNonNull(location.getWorld()).getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    @Subcommand("pos2")
    @CommandPermission("yumn.pos2")
    public void pos2(Player player) {
        Location location = player.getLocation();
        Pair<Location, Location> pair = Yumn.getInstance().playerSelections.get(player.getUniqueId());
        if(pair == null) {
            pair = new Pair<>(null, location);
            Yumn.getInstance().playerSelections.put(player.getUniqueId(), pair);
        } else {
            pair.setSecond(location);
        }
        player.sendMessage(ChatColor.GREEN + "Position 2 has been set to " + String.format("%s %s %s %s", Objects.requireNonNull(location.getWorld()).getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    @Subcommand("wand")
    @CommandPermission("yumn.wand.get")
    public void wand(Player player) {
        player.getInventory().addItem(Yumn.getInstance().getWand());
        player.sendMessage(ChatColor.GREEN + "Selection Wand given");
    }

    @Subcommand("struct paste")
    @CommandPermission("yumn.struct.paste")
    public void paste(Player player, String name) {
        Location location = player.getLocation();
        if(StructureManager.pasteStructure(location, name, false)) {
            player.sendMessage(ChatColor.GREEN + "Structure " + name + " placed");
        } else {
            player.sendMessage(ChatColor.RED + "Structure not found");
        }
    }

    @Subcommand("struct save")
    @CommandPermission("yumn.struct.save")
    public void save(Player player, String name) {
        Pair<Location, Location> pair = Yumn.getInstance().playerSelections.get(player.getUniqueId());
        if(pair == null) {
            player.sendMessage(ChatColor.RED + "Please select a region first");
            return;
        }
        if(pair.getFirst() == null) {
            player.sendMessage(ChatColor.RED + "Position 1 not existed");
            return;
        }
        if(pair.getSecond() == null) {
            player.sendMessage(ChatColor.RED + "Position 2 not existed");
            return;
        }
        if(!Objects.equals(pair.getFirst().getWorld(), pair.getSecond().getWorld())) {
            player.sendMessage(ChatColor.RED + "Two positions are not in the same world");
            return;
        }
        StructureManager.createAndSaveStructure(name, pair.getFirst().getWorld(), pair.getFirst(), pair.getSecond());
        player.sendMessage(ChatColor.GREEN + "Structure " + name + " saved");
    }
}
