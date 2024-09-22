package ru.lastlord.lasttools.command;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.HashMap;

public class CommandWash implements CommandExecutor {

    public CommandWash() {
        LastTools.instance.getCommand("wash").setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.hasPermission("lasttools.wash") || player.hasPermission("*") || player.isOp()) {
                ChatUtil.sendMessage(player, "wash.process", new HashMap<>());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (World world : Bukkit.getWorlds()) {
                            for (Chunk chunk : world.getLoadedChunks()) {
                                for (Entity entity : chunk.getEntities()) {
                                    if (entity instanceof Item) {
                                        entity.remove();
                                    }
                                }
                            }
                        }
                    }
                }.runTaskAsynchronously(LastTools.instance);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        int amount = 0;
                        for (World world : Bukkit.getWorlds()) {
                            for (Chunk chunk : world.getLoadedChunks()) {
                                for (Entity entity : chunk.getEntities()) {
                                    if (entity instanceof Item) {
                                        amount++;
                                        entity.remove();
                                    }
                                }
                            }
                        }
                        if (amount == 0) {
                            ChatUtil.sendMessage(player, "wash.done", new HashMap<>());
                            cancel();
                        }
                    }
                }.runTaskTimerAsynchronously(LastTools.instance, 0, 20);
            } else {
                for (Rank rank : RankManager.ranks) {
                    if (rank.getPermissions().contains("lasttools.wash") || rank.getPermissions().contains("*")) {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("rank", rank.getPrefix());
                        ChatUtil.sendMessage(player, "no-perms", placeholders);
                        break;
                    }
                }
            }
        } else {
            ChatUtil.sendMessage(sender, "wash.process", new HashMap<>());
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (World world : Bukkit.getWorlds()) {
                        for (Chunk chunk : world.getLoadedChunks()) {
                            for (Entity entity : chunk.getEntities()) {
                                if (entity instanceof Item) {
                                    entity.remove();
                                }
                            }
                        }
                    }
                }
            }.runTaskAsynchronously(LastTools.instance);
            new BukkitRunnable() {

                @Override
                public void run() {
                    int amount = 0;
                    for (World world : Bukkit.getWorlds()) {
                        for (Chunk chunk : world.getLoadedChunks()) {
                            for (Entity entity : chunk.getEntities()) {
                                if (entity instanceof Item) {
                                    amount++;
                                    entity.remove();
                                }
                            }
                        }
                    }
                    if (amount == 0) {
                        ChatUtil.sendMessage(sender, "wash.done", new HashMap<>());
                        cancel();
                    }
                }
            }.runTaskTimerAsynchronously(LastTools.instance, 0, 20);
        }
        return true;
    }
}
