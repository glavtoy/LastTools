package ru.lastlord.lasttools.command;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.configuration.Config;
import ru.lastlord.lasttools.hologram.manager.HologramManager;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.*;

public class CommandGamemode implements CommandExecutor, TabCompleter {

    public CommandGamemode() {
        LastTools.instance.getCommand("gamemode").setExecutor(this);
        LastTools.instance.getCommand("gamemode").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0")) {
                    if (player.hasPermission("lasttools.gamemode.survival") || player.hasPermission("lasttools.gamemode.*") || player.hasPermission("*") || player.isOp()) {
                        player.setGameMode(GameMode.SURVIVAL);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("gamemode", "survival");
                        ChatUtil.sendMessage(player, "gamemode.player", placeholders);
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.gamemode.survival") || rank.getPermissions().contains("lasttools.gamemode.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1")) {
                    if (player.hasPermission("lasttools.gamemode.creative") || player.hasPermission("lasttools.gamemode.*") || player.hasPermission("*") || player.isOp()) {
                        player.setGameMode(GameMode.CREATIVE);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("gamemode", "creative");
                        ChatUtil.sendMessage(player, "gamemode.player", placeholders);
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.gamemode.creative") || rank.getPermissions().contains("lasttools.gamemode.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2")) {
                    if (player.hasPermission("lasttools.gamemode.adventure") || player.hasPermission("lasttools.gamemode.*") || player.hasPermission("*") || player.isOp()) {
                        player.setGameMode(GameMode.ADVENTURE);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("gamemode", "adventure");
                        ChatUtil.sendMessage(player, "gamemode.player", placeholders);
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.gamemode.adventure") || rank.getPermissions().contains("lasttools.gamemode.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3")) {
                    if (player.hasPermission("lasttools.gamemode.spectator") || player.hasPermission("lasttools.gamemode.*") || player.hasPermission("*") || player.isOp()) {
                        player.setGameMode(GameMode.SPECTATOR);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("gamemode", "spectator");
                        ChatUtil.sendMessage(player, "gamemode.player", placeholders);
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.gamemode.spectator") || rank.getPermissions().contains("lasttools.gamemode.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else {
                    if (player.hasPermission("lasttools.gamemode.survival") || player.hasPermission("lasttools.gamemode.creative") || player.hasPermission("lasttools.gamemode.adventure") || player.hasPermission("lasttools.gamemode.spectator") || player.hasPermission("lasttools.gamemode.*") || player.hasPermission("*") || player.isOp()) {
                        ChatUtil.sendMessage(player, "gamemode.usage.player", new HashMap<>());
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.gamemode.survival") || rank.getPermissions().contains("lasttools.gamemode.creative") || rank.getPermissions().contains("lasttools.gamemode.adventure") || rank.getPermissions().contains("lasttools.gamemode.spectator") || rank.getPermissions().contains("lasttools.gamemode.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                }
            } else if (args.length == 2) {
                final Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0")) {
                        if (player.hasPermission("lasttools.gamemode.survival.other") || player.hasPermission("lasttools.gamemode.*.other") || player.hasPermission("*") || player.isOp()) {
                            target.setGameMode(GameMode.SURVIVAL);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("gamemode", "survival");
                            placeholders.put("player", args[1]);
                            ChatUtil.sendMessage(player, "gamemode.other", placeholders);
                        } else {
                            for (Rank rank : RankManager.ranks) {
                                if (rank.getPermissions().contains("lasttools.gamemode.survival.other") || rank.getPermissions().contains("lasttools.gamemode.*.other") || rank.getPermissions().contains("*")) {
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("rank", rank.getPrefix());
                                    ChatUtil.sendMessage(player, "no-perms", placeholders);
                                    break;
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1")) {
                        if (player.hasPermission("lasttools.gamemode.creative.other") || player.hasPermission("lasttools.gamemode.*.other") || player.hasPermission("*") || player.isOp()) {
                            target.setGameMode(GameMode.CREATIVE);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("gamemode", "creative");
                            placeholders.put("player", args[1]);
                            ChatUtil.sendMessage(player, "gamemode.other", placeholders);
                        } else {
                            for (Rank rank : RankManager.ranks) {
                                if (rank.getPermissions().contains("lasttools.gamemode.creative.other") || rank.getPermissions().contains("lasttools.gamemode.*.other") || rank.getPermissions().contains("*")) {
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("rank", rank.getPrefix());
                                    ChatUtil.sendMessage(player, "no-perms", placeholders);
                                    break;
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2")) {
                        if (player.hasPermission("lasttools.gamemode.adventure.other") || player.hasPermission("lasttools.gamemode.*.other") || player.hasPermission("*") || player.isOp()) {
                            target.setGameMode(GameMode.ADVENTURE);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("gamemode", "adventure");
                            placeholders.put("player", args[1]);
                            ChatUtil.sendMessage(player, "gamemode.other", placeholders);
                        } else {
                            for (Rank rank : RankManager.ranks) {
                                if (rank.getPermissions().contains("lasttools.gamemode.adventure.other") || rank.getPermissions().contains("lasttools.gamemode.*.other") || rank.getPermissions().contains("*")) {
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("rank", rank.getPrefix());
                                    ChatUtil.sendMessage(player, "no-perms", placeholders);
                                    break;
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3")) {
                        if (player.hasPermission("lasttools.gamemode.spectator.other") || player.hasPermission("lasttools.gamemode.*") || player.hasPermission("*") || player.isOp()) {
                            target.setGameMode(GameMode.SPECTATOR);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("gamemode", "spectator");
                            placeholders.put("player", args[1]);
                            ChatUtil.sendMessage(player, "gamemode.other", placeholders);
                        } else {
                            for (Rank rank : RankManager.ranks) {
                                if (rank.getPermissions().contains("lasttools.gamemode.spectator.other") || rank.getPermissions().contains("lasttools.gamemode.*.other") || rank.getPermissions().contains("*")) {
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("rank", rank.getPrefix());
                                    ChatUtil.sendMessage(player, "no-perms", placeholders);
                                    break;
                                }
                            }
                        }
                    } else {
                        if (player.hasPermission("lasttools.gamemode.survival.other") || player.hasPermission("lasttools.gamemode.creative.other") || player.hasPermission("lasttools.gamemode.adventure.other") || player.hasPermission("lasttools.gamemode.spectator.other") || player.hasPermission("lasttools.gamemode.*.other") || player.hasPermission("*") || player.isOp()) {
                            ChatUtil.sendMessage(player, "gamemode.usage.other", new HashMap<>());
                        } else {
                            for (Rank rank : RankManager.ranks) {
                                if (rank.getPermissions().contains("lasttools.gamemode.survival.other") || rank.getPermissions().contains("lasttools.gamemode.creative.other") || rank.getPermissions().contains("lasttools.gamemode.adventure.other") || rank.getPermissions().contains("lasttools.gamemode.spectator.other") || rank.getPermissions().contains("lasttools.gamemode.*.other") || rank.getPermissions().contains("*")) {
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("rank", rank.getPrefix());
                                    ChatUtil.sendMessage(player, "no-perms", placeholders);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    final HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("player", args[1]);
                    ChatUtil.sendMessage(player, "offline-player", placeholders);
                }
            } else {
                if (player.hasPermission("lasttools.gamemode.survival") || player.hasPermission("lasttools.gamemode.creative") || player.hasPermission("lasttools.gamemode.adventure") || player.hasPermission("lasttools.gamemode.spectator") || player.hasPermission("lasttools.gamemode.*") || player.hasPermission("*") || player.isOp()) {
                    final HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("player", Config.getConfig().getString("locale.gamemode.usage.player"));
                    placeholders.put("other", Config.getConfig().getString("locale.gamemode.usage.other"));
                    ChatUtil.sendListMessage(player, "gamemode.usage.multiply-use", placeholders);
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.gamemode.survival") || rank.getPermissions().contains("lasttools.gamemode.creative") || rank.getPermissions().contains("lasttools.gamemode.adventure") || rank.getPermissions().contains("lasttools.gamemode.spectator") || rank.getPermissions().contains("lasttools.gamemode.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            }
        } else {
            if (args.length == 2) {
                final Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0")) {
                        target.setGameMode(GameMode.SURVIVAL);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("gamemode", "survival");
                        placeholders.put("player", args[1]);
                        ChatUtil.sendMessage(sender, "gamemode.other", placeholders);
                    } else if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1")) {
                        target.setGameMode(GameMode.CREATIVE);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("gamemode", "creative");
                        placeholders.put("player", args[1]);
                        ChatUtil.sendMessage(sender, "gamemode.other", placeholders);
                    } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2")) {
                        target.setGameMode(GameMode.ADVENTURE);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("gamemode", "adventure");
                        placeholders.put("player", args[1]);
                        ChatUtil.sendMessage(sender, "gamemode.other", placeholders);
                    } else if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3")) {
                        target.setGameMode(GameMode.SPECTATOR);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("gamemode", "spectator");
                        placeholders.put("player", args[1]);
                        ChatUtil.sendMessage(sender, "gamemode.other", placeholders);
                    } else {
                        ChatUtil.sendMessage(sender, "gamemode.usage.other", new HashMap<>());
                    }
                } else {
                    final HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("player", args[1]);
                    ChatUtil.sendMessage(sender, "offline-player", placeholders);
                }
            } else {
                ChatUtil.sendMessage(sender, "gamemode.usage.other", new HashMap<>());
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
                if (player.hasPermission("lasttools.gamemode.survival") || player.hasPermission("lasttools.gamemode.*") || player.isOp()) {
                    list.add("survival");
                    list.add("0");
                }
                if (player.hasPermission("lasttools.gamemode.creative") || player.hasPermission("lasttools.gamemode.*") || player.isOp()) {
                    list.add("creative");
                    list.add("1");
                }
                if (player.hasPermission("lasttools.gamemode.adventure") || player.hasPermission("lasttools.gamemode.*") || player.isOp()) {
                    list.add("adventure");
                    list.add("2");
                }
                if (player.hasPermission("lasttools.gamemode.spectator") || player.hasPermission("lasttools.gamemode.*") || player.isOp()) {
                    list.add("spectator");
                    list.add("3");
                }
            } else {
                if (player.hasPermission("lasttools.gamemode.survival.other") || player.hasPermission("lasttools.gamemode.creative.other") || player.hasPermission("lasttools.gamemode.adventure.other") || player.hasPermission("lasttools.gamemode.spectator.other") || player.hasPermission("lasttools.gamemode.*.other") || player.isOp()) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
            }
        } else {
            if (args.length == 1) {
                list.add("survival");
                list.add("creative");
                list.add("adventure");
                list.add("spectator");
                list.add("0");
                list.add("1");
                list.add("2");
                list.add("3");
            } else if (args.length == 2) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
        }
        return list;
    }
}
