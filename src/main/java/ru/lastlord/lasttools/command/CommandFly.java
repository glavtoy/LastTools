package ru.lastlord.lasttools.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
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

public class CommandFly implements CommandExecutor, TabCompleter {

    public CommandFly() {
        LastTools.instance.getCommand("fly").setExecutor(this);
        LastTools.instance.getCommand("fly").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 0) {
                if (player.hasPermission("lasttools.fly") || player.hasPermission("lasttools.fly.*") || player.hasPermission("*") || player.isOp()) {
                    if (!player.getAllowFlight()) {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        ChatUtil.sendMessage(player, "fly.player.enable", new HashMap<>());
                    } else if (player.getAllowFlight()) {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        ChatUtil.sendMessage(player, "fly.player.disable", new HashMap<>());
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.fly") || rank.getPermissions().contains("lasttools.fly.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else if (args.length == 1) {
                if (player.hasPermission("lasttools.fly.other") || player.hasPermission("lasttools.fly.*") || player.hasPermission("*") || player.isOp()) {
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (!target.getAllowFlight()) {
                            target.setAllowFlight(true);
                            target.setFlying(true);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[0]);
                            ChatUtil.sendMessage(player, "fly.other.on", placeholders);
                        } else if (target.getAllowFlight()) {
                            target.setAllowFlight(false);
                            target.setFlying(false);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[0]);
                            ChatUtil.sendMessage(player, "fly.other.off", placeholders);
                        }
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[0]);
                        ChatUtil.sendMessage(player, "offline-player", placeholders);
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.fly.other") || rank.getPermissions().contains("lasttools.fly.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.fly") || player.hasPermission("lasttools.fly.other") || player.hasPermission("lasttools.fly.*") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendListMessage(player, "fly.multiply-use", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.fly") || rank.getPermissions().contains("lasttools.fly.other") || rank.getPermissions().contains("lasttools.fly.*") || rank.getPermissions().contains("*")) {
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
                    if (!target.getAllowFlight()) {
                        target.setAllowFlight(true);
                        target.setFlying(true);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[0]);
                        ChatUtil.sendMessage(sender, "fly.other.on", placeholders);
                    } else if (target.getAllowFlight()) {
                        target.setAllowFlight(false);
                        target.setFlying(false);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[0]);
                        ChatUtil.sendMessage(sender, "fly.other.off", placeholders);
                    }
                } else {
                    final HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("player", args[0]);
                    ChatUtil.sendMessage(sender, "offline-player", placeholders);
                }
            } else {
                ChatUtil.sendListMessage(sender, "fly.multiply-use", new HashMap<>());
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
                if (player.hasPermission("lasttools.fly.other") || player.hasPermission("lasttools.fly.*") || player.isOp()) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
            }
        } else {
            if (args.length == 1) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
        }
        return list;
    }
}