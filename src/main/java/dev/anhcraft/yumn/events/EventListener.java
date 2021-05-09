package dev.anhcraft.yumn.events;

import dev.anhcraft.jvmkit.utils.Pair;
import dev.anhcraft.yumn.Yumn;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class EventListener implements Listener {
    @EventHandler
    public void quit(PlayerQuitEvent ev) {
        Yumn.getInstance().playerSelections.remove(ev.getPlayer().getUniqueId());
    }

    @EventHandler
    public void interact(PlayerInteractEvent ev) {
        if (ev.hasBlock() && (ev.getAction() == Action.LEFT_CLICK_BLOCK || ev.getAction() == Action.RIGHT_CLICK_BLOCK) && Yumn.getInstance().isWand(ev.getItem()) && ev.getPlayer().hasPermission("yumn.wand.use")) {
            boolean left = ev.getAction() == Action.LEFT_CLICK_BLOCK;
            Location location = Objects.requireNonNull(ev.getClickedBlock()).getLocation();
            Pair<Location, Location> pair = Yumn.getInstance().playerSelections.get(ev.getPlayer().getUniqueId());
            if (pair == null) {
                Yumn.getInstance().playerSelections.put(ev.getPlayer().getUniqueId(), new Pair<>(
                        left ? location : null,
                        left ? null : location
                ));
            } else if (left) {
                pair.setFirst(location);
            } else {
                pair.setSecond(location);
            }
            ev.getPlayer().sendMessage(ChatColor.GREEN + "Position " + (left ? 1 : 2) + " has been set to " + String.format("%s %s %s %s", Objects.requireNonNull(location.getWorld()).getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            ev.setCancelled(true);
        }
    }
}
