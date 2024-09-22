package ru.lastlord.lasttools.command;

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

public class CommandTempMute implements CommandExecutor {

    public CommandTempMute() {
        LastTools.instance.getCommand("tempmute").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length >= 3) {
                if (player.hasPermission("lasttools.tempmute") || player.hasPermission("*") || player.isOp()) {
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
                            if (player.hasPermission("lasttools.tempmute") || player.hasPermission("*") || player.isOp()) {
                                ChatUtil.sendMessage(player, "tempmute.usage", new HashMap<>());
                            } else {
                                fail = true;
                                for (Rank rank : RankManager.ranks) {
                                    if (rank.getPermissions().contains("lasttools.tempmute") || rank.getPermissions().contains("*")) {
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
                            if (!LastTools.database.isMuted(targetName)) {
                                if (!RankManager.getRank(targetName).join().getTag().equalsIgnoreCase("admin")) {
                                    LastTools.database.temporaryMute(targetName, reason, player.getName(), duration);
                                    final Player targetPlayer = Bukkit.getPlayer(targetName);
                                    if (targetPlayer != null)
                                        targetPlayer.sendMessage(Config.getTemporaryMuteLayout(duration, player.getName(), reason));
                                    final String path = "locale.tempmute.time-format.";
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
                                    ChatUtil.broadcastMessage("tempmute.broadcast", placeholders);
                                } else {
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("player", targetName);
                                    ChatUtil.sendMessage(player, "mute.not-can", placeholders);
                                }
                            } else {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("player", targetName);
                                ChatUtil.sendMessage(player, "mute.already-muted", placeholders);
                            }
                        }
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.tempmute") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.tempmute") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendMessage(player, "tempmute.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.tempmute") || rank.getPermissions().contains("*")) {
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
                        ChatUtil.sendMessage(sender, "tempmute.usage", new HashMap<>());
                        break;
                    }
                }
                if (!fail) {
                    String durationString = durationStringJoiner.toString().trim();
                    String reason = reasonStringJoiner.toString().trim();
                    long duration = parseDuration(durationString);
                    if (duration != -1) {
                        if (!LastTools.database.isMuted(targetName)) {
                            if (!RankManager.getRank(targetName).join().getTag().equalsIgnoreCase("admin")) {
                                LastTools.database.temporaryMute(targetName, reason, "CONSOLE", duration);
                                final Player targetPlayer = Bukkit.getPlayer(targetName);
                                final HashMap<String, String> placeholders1 = new HashMap<>();
                                placeholders1.put("admin", "CONSOLE");
                                placeholders1.put("reason", reason);
                                placeholders1.put("time", String.valueOf(duration));
                                if (targetPlayer != null)
                                    targetPlayer.sendMessage(Config.getTemporaryMuteLayout(duration, "CONSOLE", reason));
                                final String path = "locale.tempmute.time-format.";
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
                                ChatUtil.broadcastMessage("tempmute.broadcast", placeholders);
                            } else {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("player", targetName);
                                ChatUtil.sendMessage(sender, "mute.not-can", placeholders);
                            }
                        } else {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", targetName);
                            ChatUtil.sendMessage(sender, "mute.already-muted", placeholders);
                        }
                    }
                }
            } else {
                ChatUtil.sendMessage(sender, "tempmute.usage", new HashMap<>());
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

