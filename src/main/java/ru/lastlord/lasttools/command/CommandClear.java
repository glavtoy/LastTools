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
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandClear implements CommandExecutor, TabCompleter {

    public CommandClear() {
        LastTools.instance.getCommand("clear").setExecutor(this);
        LastTools.instance.getCommand("clear").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 0) {
                if (player.hasPermission("lasttools.clear") || player.hasPermission("lasttools.clear.*") || player.hasPermission("*") || player.isOp()) {
                    player.getInventory().clear();
                    player.getInventory().setHelmet(null);
                    player.getInventory().setChestplate(null);
                    player.getInventory().setLeggings(null);
                    player.getInventory().setBoots(null);
                    player.getInventory().setItemInOffHand(null);
                    ChatUtil.sendMessage(player, "clear.player", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.clear") || rank.getPermissions().contains("lasttools.clear.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else if (args.length == 1) {
                if (player.hasPermission("lasttools.clear.other") || player.hasPermission("lasttools.clear.*") || player.hasPermission("*") || player.isOp()) {
                        final Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            target.getInventory().clear();
                            target.getInventory().setHelmet(null);
                            target.getInventory().setChestplate(null);
                            target.getInventory().setLeggings(null);
                            target.getInventory().setBoots(null);
                            target.getInventory().setItemInOffHand(null);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[0]);
                            ChatUtil.sendMessage(player, "clear.other", placeholders);
                        } else {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[0]);
                            ChatUtil.sendMessage(player, "offline-player", placeholders);
                        }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.clear.other") || rank.getPermissions().contains("lasttools.clear.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.clear") || player.hasPermission("lasttools.clear.other") || player.hasPermission("lasttools.clear.*") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendListMessage(player, "clear.multiply-use", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.clear") || rank.getPermissions().contains("lasttools.clear.other") || rank.getPermissions().contains("lasttools.clear.*") || rank.getPermissions().contains("*")) {
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
                    target.getInventory().clear();
                    target.getInventory().setHelmet(null);
                    target.getInventory().setChestplate(null);
                    target.getInventory().setLeggings(null);
                    target.getInventory().setBoots(null);
                    target.getInventory().setItemInOffHand(null);
                    final HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("player", args[0]);
                    ChatUtil.sendMessage(sender, "clear.other", placeholders);
                } else {
                    final HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("player", args[0]);
                    ChatUtil.sendMessage(sender, "offline-player", placeholders);
                }
            } else {
                ChatUtil.sendListMessage(sender, "clear.multiply-use", new HashMap<>());
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
                if (player.hasPermission("lasttools.clear.other") || player.hasPermission("lasttools.clear.*") || player.isOp()) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
            }
        } else {
            if (args.length == 1) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
        }
        return list;
    }
}
