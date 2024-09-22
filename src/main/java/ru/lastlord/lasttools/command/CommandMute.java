package ru.lastlord.lasttools.command;

import net.kyori.adventure.text.Component;
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
import java.util.StringJoiner;

public class CommandMute implements CommandExecutor, TabCompleter {

    public CommandMute() {
        LastTools.instance.getCommand("mute").setExecutor(this);
        LastTools.instance.getCommand("mute").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length >= 2) {
                if (player.hasPermission("lasttools.mute") || player.hasPermission("*") || player.isOp()) {
                    final String targetName = args[0];
                    final StringJoiner stringJoiner = new StringJoiner(" ");
                    for (int i = 1; i < args.length; i++) {
                        if (i == args.length) stringJoiner.add(args[i]);
                        else stringJoiner.add(args[i]);
                    }
                    final String reason = stringJoiner.toString();
                    if (!LastTools.database.isMuted(targetName)) {
                        if (!RankManager.getRank(targetName).join().getTag().equalsIgnoreCase("admin")) {
                            LastTools.database.permanentMute(targetName, reason, player.getName());
                            final Player targetPlayer = Bukkit.getPlayer(targetName);
                            final HashMap<String, String> placeholders1 = new HashMap<>();
                            placeholders1.put("admin", player.getName());
                            placeholders1.put("reason", reason);
                            if (targetPlayer != null) ChatUtil.sendListMessage(targetPlayer, "mute.layout", placeholders1);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", targetName);
                            placeholders.put("admin", player.getName());
                            placeholders.put("reason", reason);
                            ChatUtil.broadcastMessage("mute.broadcast", placeholders);
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
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.mute") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.mute") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendMessage(player, "mute.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.mute") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            }
        } else {
            if (args.length >= 2) {
                final String targetName = args[0];
                final StringJoiner stringJoiner = new StringJoiner("\n");
                for (int i = 1; i < args.length; i++) {
                    if (i == args.length) stringJoiner.add(args[i]);
                    else stringJoiner.add(args[i] + " ");
                }
                final String reason = stringJoiner.toString();
                if (!LastTools.database.isMuted(targetName)) {
                    if (!RankManager.getRank(targetName).join().getTag().equalsIgnoreCase("admin")) {
                        LastTools.database.permanentMute(targetName, reason, "CONSOLE");
                        final Player targetPlayer = Bukkit.getPlayer(targetName);
                        final HashMap<String, String> placeholders1 = new HashMap<>();
                        placeholders1.put("admin", "CONSOLE");
                        placeholders1.put("reason", reason);
                        if (targetPlayer != null) ChatUtil.sendListMessage(targetPlayer, "mute.layout", placeholders1);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", targetName);
                        placeholders.put("admin", "CONSOLE");
                        placeholders.put("reason", reason);
                        ChatUtil.broadcastMessage("mute.broadcast", placeholders);
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
            } else {
                ChatUtil.sendMessage(sender, "mute.usage", new HashMap<>());
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
                if (player.hasPermission("lasttools.mute") || player.isOp()) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
            }
        } else {
            if (args.length == 1) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
        }
        return list;
    }
}

