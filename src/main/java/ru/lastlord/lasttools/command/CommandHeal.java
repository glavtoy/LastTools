package ru.lastlord.lasttools.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.configuration.Config;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CommandHeal implements CommandExecutor, TabCompleter {

    final HashMap<UUID, Integer> cooldowns = new HashMap<>();

    public CommandHeal() {
        LastTools.instance.getCommand("heal").setExecutor(this);
        LastTools.instance.getCommand("heal").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 0) {
                if (player.hasPermission("lasttools.heal") || player.hasPermission("lasttools.heal.*") || player.hasPermission("*") || player.isOp()) {
                    if (!cooldowns.containsKey(player.getUniqueId())) cooldowns.put(player.getUniqueId(), 0);
                    if (cooldowns.get(player.getUniqueId()) == 0) {
                        if (!player.hasPermission("lasttools.heal.*") && !player.hasPermission("lasttools.heal.bypass") && !player.hasPermission("*") && !player.isOp()) {
                            cooldowns.put(player.getUniqueId(), Config.getConfig().getInt("locale.heal.cooldown.time"));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (cooldowns.get(player.getUniqueId()) > 0)
                                        cooldowns.put(player.getUniqueId(), cooldowns.get(player.getUniqueId()) - 1);
                                    else cancel();
                                }
                            }.runTaskTimer(LastTools.instance, 0, 20);
                        }
                        player.setHealth(20);
                        ChatUtil.sendMessage(player, "heal.player", new HashMap<>());
                    } else {
                        final int totalSecs = cooldowns.get(player.getUniqueId());
                        final int hours = totalSecs / 3600;
                        final int minutes = (totalSecs % 3600) / 60;
                        final int seconds = totalSecs % 60;
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("h", String.valueOf(hours));
                        placeholders.put("m", String.valueOf(minutes));
                        placeholders.put("s", String.valueOf(seconds));
                        ChatUtil.sendMessage(player, "heal.cooldown.message", placeholders);
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.heal") || rank.getPermissions().contains("lasttools.heal.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else if (args.length == 1) {
                if (player.hasPermission("lasttools.heal.other") || player.hasPermission("lasttools.heal.*") || player.hasPermission("*") || player.isOp()) {
                    if (!cooldowns.containsKey(player.getUniqueId())) cooldowns.put(player.getUniqueId(), 0);
                    if (cooldowns.get(player.getUniqueId()) == 0) {
                        if (!player.hasPermission("lasttools.heal.*") && !player.hasPermission("lasttools.heal.bypass") && !player.hasPermission("*") && !player.isOp()) {
                            cooldowns.put(player.getUniqueId(), Config.getConfig().getInt("locale.heal.cooldown.time"));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (cooldowns.get(player.getUniqueId()) > 0)
                                        cooldowns.put(player.getUniqueId(), cooldowns.get(player.getUniqueId()) - 1);
                                    else cancel();
                                }
                            }.runTaskTimer(LastTools.instance, 0, 20);
                        }
                        final Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            target.setHealth(20);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[0]);
                            ChatUtil.sendMessage(player, "heal.other", placeholders);
                        } else {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[0]);
                            ChatUtil.sendMessage(player, "offline-player", placeholders);
                        }
                    } else {
                        final int totalSecs = cooldowns.get(player.getUniqueId());
                        final int hours = totalSecs / 3600;
                        final int minutes = (totalSecs % 3600) / 60;
                        final int seconds = totalSecs % 60;
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("h", String.valueOf(hours));
                        placeholders.put("m", String.valueOf(minutes));
                        placeholders.put("s", String.valueOf(seconds));
                        ChatUtil.sendMessage(player, "heal.cooldown.message", placeholders);
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.heal.other") || rank.getPermissions().contains("lasttools.heal.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.heal") || player.hasPermission("lasttools.heal.other") || player.hasPermission("lasttools.heal.*") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendListMessage(player, "heal.multiply-use", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.heal") || rank.getPermissions().contains("lasttools.heal.other") || rank.getPermissions().contains("lasttools.heal.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            }
        } else {
            if (args.length == 1) {
                final Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    target.setHealth(20);
                    final HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("player", args[0]);
                    ChatUtil.sendMessage(sender, "heal.other", placeholders);
                } else {
                    final HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("player", args[0]);
                    ChatUtil.sendMessage(sender, "offline-player", placeholders);
                }
            } else {
                ChatUtil.sendListMessage(sender, "heal.multiply-use", new HashMap<>());
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("lasttools.heal.other") || player.hasPermission("lasttools.heal.*") || player.isOp()) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
            }
        } else {
            if (args.length == 1) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
        }
        return list;
    }
}