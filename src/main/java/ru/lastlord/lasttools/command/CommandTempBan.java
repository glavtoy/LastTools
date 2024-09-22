package ru.lastlord.lasttools.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.configuration.Config;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.HashMap;
import java.util.StringJoiner;

public class CommandTempBan implements CommandExecutor {

    public CommandTempBan() {
        LastTools.instance.getCommand("tempban").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length >= 3) {
                if (player.hasPermission("lasttools.tempban") || player.hasPermission("*") || player.isOp()) {
                    final String targetName = args[0];
                    final StringJoiner durationStringJoiner = new StringJoiner(" ");
                    final StringJoiner reasonStringJoiner = new StringJoiner(" ");
                    boolean durationParsed = false;
                    boolean fail = false;
                    for (int i = 1; i < args.length; i++) {
                        if (args[i].matches("\\d+[dhms]")) {
                            durationStringJoiner.add(args[i]);
                            durationParsed = true;
                        } else if (durationParsed) {
                            reasonStringJoiner.add(args[i]);
                        } else {
                            if (player.hasPermission("lasttools.tempban") || player.hasPermission("*") || player.isOp()) {
                                ChatUtil.sendMessage(player, "tempban.usage", new HashMap<>());
                            } else {
                                fail = true;
                                for (Rank rank : RankManager.ranks) {
                                    if (rank.getPermissions().contains("lasttools.tempban") || rank.getPermissions().contains("*")) {
                                        final HashMap<String, String> placeholders = new HashMap<>();
                                        placeholders.put("rank", rank.getPrefix());
                                        ChatUtil.sendMessage(player, "no-perms", placeholders);
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                    if (!fail) {
                        String durationString = durationStringJoiner.toString().trim();
                        String reason = reasonStringJoiner.toString().trim();
                        long duration = parseDuration(durationString);
                        if (duration != -1) {
                            if (!LastTools.database.isBanned(targetName)) {
                                if (!RankManager.getRank(targetName).join().getTag().equalsIgnoreCase("admin")) {
                                    LastTools.database.temporaryBan(targetName, reason, player.getName(), duration);
                                    final Player targetPlayer = Bukkit.getPlayer(targetName);
                                    if (targetPlayer != null)
                                        targetPlayer.kick(Component.text(Config.getTemporaryBanLayout(duration, player.getName(), reason)));
                                    final String path = "locale.tempban.time-format.";
                                    final String days = (duration / 86400) + Config.getConfig().getString(path + "days");
                                    final String hours = (duration % 86400 / 3600) + Config.getConfig().getString(path + "hours");
                                    final String minutes = (duration % 3600 / 60) + Config.getConfig().getString(path + "minutes");
                                    final String seconds = (duration % 60) + Config.getConfig().getString(path + "seconds");
                                    final String totalTime = String.format("%s %s %s %s", days, hours, minutes, seconds);
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("player", targetName);
                                    placeholders.put("admin", player.getName());
                                    placeholders.put("reason", reason);
                                    placeholders.put("format-time", totalTime);
                                    ChatUtil.broadcastMessage("tempban.broadcast", placeholders);
                                } else {
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("player", targetName);
                                    ChatUtil.sendMessage(player, "ban.not-can", placeholders);
                                }
                            } else {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("player", targetName);
                                ChatUtil.sendMessage(player, "ban.already-banned", placeholders);
                            }
                        }
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.tempban") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.tempban") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendMessage(player, "tempban.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.tempban") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            }
        } else {
            if (args.length >= 3) {
                    final String targetName = args[0];
                    final StringJoiner durationStringJoiner = new StringJoiner(" ");
                    final StringJoiner reasonStringJoiner = new StringJoiner(" ");
                    boolean durationParsed = false;
                    boolean fail = false;
                    for (int i = 1; i < args.length; i++) {
                        if (args[i].matches("\\d+[dhms]")) {
                            durationStringJoiner.add(args[i]);
                            durationParsed = true;
                        } else if (durationParsed) {
                            reasonStringJoiner.add(args[i]);
                        } else {
                                fail = true;
                                for (Rank rank : RankManager.ranks) {
                                    if (rank.getPermissions().contains("lasttools.tempban") || rank.getPermissions().contains("*")) {
                                        final HashMap<String, String> placeholders = new HashMap<>();
                                        placeholders.put("rank", rank.getPrefix());
                                        ChatUtil.sendMessage(sender, "no-perms", placeholders);
                                        break;
                                    }
                                }
                            break;
                        }
                    }
                    if (!fail) {
                        String durationString = durationStringJoiner.toString().trim();
                        String reason = reasonStringJoiner.toString().trim();
                        long duration = parseDuration(durationString);
                        if (duration != -1) {
                            if (!LastTools.database.isBanned(targetName)) {
                                if (!RankManager.getRank(targetName).join().getTag().equalsIgnoreCase("admin")) {
                                    LastTools.database.temporaryBan(targetName, reason, "CONSOLE", duration);
                                    final Player targetPlayer = Bukkit.getPlayer(targetName);
                                    if (targetPlayer != null)
                                        targetPlayer.kick(Component.text(Config.getTemporaryBanLayout(duration, "CONSOLE", reason)));
                                    final String path = "locale.tempban.time-format.";
                                    final String days = (duration / 86400) + Config.getConfig().getString(path + "days");
                                    final String hours = (duration % 86400 / 3600) + Config.getConfig().getString(path + "hours");
                                    final String minutes = (duration % 3600 / 60) + Config.getConfig().getString(path + "minutes");
                                    final String seconds = (duration % 60) + Config.getConfig().getString(path + "seconds");
                                    final String totalTime = String.format("%s %s %s %s", days, hours, minutes, seconds);
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("player", targetName);
                                    placeholders.put("admin", "CONSOLE");
                                    placeholders.put("reason", reason);
                                    placeholders.put("format-time", totalTime);
                                    ChatUtil.broadcastMessage("tempban.broadcast", placeholders);
                                } else {
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("player", targetName);
                                    ChatUtil.sendMessage(sender, "ban.not-can", placeholders);
                                }
                            } else {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("player", targetName);
                                ChatUtil.sendMessage(sender, "ban.already-banned", placeholders);
                            }
                        }
                    }
            } else {
                    ChatUtil.sendMessage(sender, "tempban.usage", new HashMap<>());
            }
        }
        return true;
    }

    private static long parseDuration(String durationString) {
        long duration = 0;
        final String[] parts = durationString.split(" ");
        for (String part : parts) {
            if (part.endsWith("d")) {
                duration += Long.parseLong(part.substring(0, part.length() - 1)) * 86400;
            } else if (part.endsWith("h")) {
                duration += Long.parseLong(part.substring(0, part.length() - 1)) * 3600;
            } else if (part.endsWith("m")) {
                duration += Long.parseLong(part.substring(0, part.length() - 1)) * 60;
            } else if (part.endsWith("s")) {
                duration += Long.parseLong(part.substring(0, part.length() - 1));
            } else {
                return -1;
            }
        }
        return duration;
    }
}
