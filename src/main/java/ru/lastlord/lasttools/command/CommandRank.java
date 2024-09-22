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

public class CommandRank implements CommandExecutor, TabCompleter {

    public CommandRank() {
        LastTools.instance.getCommand("rank").setExecutor(this);
        LastTools.instance.getCommand("rank").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (player.hasPermission("lasttools.rank.set") || player.hasPermission("lasttools.rank.*") || player.hasPermission("*") || player.isOp()) {
                        final String target = args[1];
                        final Rank rank = RankManager.getRankByTag(args[2]);
                        if (rank != null) {
                            if (!LastTools.database.containsPlayer(target).join()) LastTools.database.addNewPlayer(target);
                            RankManager.setRank(target, rank);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", target);
                            placeholders.put("rank", RankManager.getInlineListedRanks());
                            ChatUtil.sendMessage(player, "rank.set", placeholders);
                        } else {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("ranks", RankManager.getInlineListedRanks());
                            ChatUtil.sendMessage(player, "available-ranks", placeholders);
                        }
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.rank.set") || rank.getPermissions().contains("lasttools.rank.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (player.hasPermission("lasttools.rank.add") || player.hasPermission("lasttools.rank.*") || player.hasPermission("*") || player.isOp()) {
                        final String target = args[1];
                        final Rank rank = RankManager.getRankByTag(args[2]);
                        final Rank targetRank = RankManager.getRank(target).join();
                        if (rank != null ) {
                            if (!LastTools.database.containsPlayer(target).join()) LastTools.database.addNewPlayer(target);
                            if (targetRank.getIndex() < rank.getIndex()) {
                                RankManager.setRank(target, rank);
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("player", target);
                                placeholders.put("rank", RankManager.getInlineListedRanks());
                                ChatUtil.sendMessage(player, "rank.add", placeholders);
                            } else {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("player", target);
                                placeholders.put("rank", RankManager.getInlineListedRanks());
                                ChatUtil.sendMessage(player, "rank.add", placeholders);
                            }
                        } else {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("ranks", RankManager.getInlineListedRanks());
                            ChatUtil.sendMessage(player, "available-ranks", placeholders);
                        }
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.rank.add") || rank.getPermissions().contains("lasttools.rank.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else {
                    if (player.hasPermission("lasttools.rank.set") || player.hasPermission("lasttools.rank.add") || player.hasPermission("lasttools.rank.*") || player.hasPermission("*") || player.isOp()) {
                        ChatUtil.sendMessage(player, "rank.usage", new HashMap<>());
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.rank.set") || rank.getPermissions().contains("lasttools.rank.add") || rank.getPermissions().contains("lasttools.rank.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.rank.set") || player.hasPermission("lasttools.rank.add") || player.hasPermission("lasttools.rank.*") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendMessage(player, "rank.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.rank.set") || rank.getPermissions().contains("lasttools.rank.add") || rank.getPermissions().contains("lasttools.rank.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            }
        } else {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set")) {
                    final String target = args[1];
                    final Rank rank = RankManager.getRankByTag(args[2]);
                    if (rank != null) {
                        if (!LastTools.database.containsPlayer(target).join()) LastTools.database.addNewPlayer(target);
                        RankManager.setRank(target, rank);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", target);
                        placeholders.put("rank", rank.getPrefix());
                        ChatUtil.sendMessage(sender, "rank.set", placeholders);
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("ranks", RankManager.getInlineListedRanks());
                        ChatUtil.sendMessage(sender, "available-ranks", placeholders);
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    final String target = args[1];
                    final Rank rank = RankManager.getRankByTag(args[2]);
                    final Rank targetRank = RankManager.getRank(target).join();
                    if (rank != null ) {
                        if (!LastTools.database.containsPlayer(target).join()) LastTools.database.addNewPlayer(target);
                        if (targetRank.getIndex() < rank.getIndex()) {
                            RankManager.setRank(target, rank);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", target);
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(sender, "rank.add", placeholders);
                        } else {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", target);
                            placeholders.put("rank", targetRank.getPrefix());
                            ChatUtil.sendMessage(sender, "rank.add", placeholders);
                        }
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("ranks", RankManager.getInlineListedRanks());
                        ChatUtil.sendMessage(sender, "available-ranks", placeholders);
                    }
                } else {
                    ChatUtil.sendMessage(sender, "rank.usage", new HashMap<>());
                }
            } else {
                ChatUtil.sendMessage(sender, "rank.usage", new HashMap<>());
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.hasPermission("lasttools.rank.set") || player.hasPermission("lasttools.rank.*") || player.hasPermission("*") || player.isOp()) {
                if (args.length == 1) {
                    if (player.hasPermission("lasttools.rank.set") || player.hasPermission("lasttools.rank.*") || player.hasPermission("*") || player.isOp()) {
                        list.add("set");
                    }
                    if (player.hasPermission("lasttools.rank.add") || player.hasPermission("lasttools.rank.*") || player.hasPermission("*") || player.isOp()) {
                        list.add("add");
                    }
                }
                else if (args.length == 2) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
                else if (args.length == 3) RankManager.ranks.forEach(rank -> list.add(rank.getTag()));
            }
        } else {
            if (args.length == 1) {
                list.add("set");
                list.add("add");
            }
            else if (args.length == 2) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
            else if (args.length == 3) RankManager.ranks.forEach(rank -> list.add(rank.getTag()));
        }
        return list;
    }
}
