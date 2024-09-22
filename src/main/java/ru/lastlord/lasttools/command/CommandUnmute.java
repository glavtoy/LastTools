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

public class CommandUnmute implements CommandExecutor, TabCompleter {

    public CommandUnmute() {
        LastTools.instance.getCommand("unmute").setExecutor(this);
        LastTools.instance.getCommand("unmute").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("lasttools.unmute") || player.hasPermission("*") || player.isOp()) {
                    final String targetName = args[0];
                    if (LastTools.database.isMuted(targetName)) {
                        LastTools.database.unmute(targetName);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", targetName);
                        ChatUtil.sendMessage(player, "unmute.message", placeholders);
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", targetName);
                        ChatUtil.sendMessage(player, "unmute.already-unmuted", placeholders);
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.unmute") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.unmute") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendMessage(player, "unmute.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.unmute") || rank.getPermissions().contains("*")) {
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
                final String targetName = args[0];
                if (LastTools.database.isMuted(targetName)) {
                    LastTools.database.unmute(targetName);
                    final HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("player", targetName);
                    ChatUtil.sendMessage(sender, "unmute.message", placeholders);
                } else {
                    final HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("player", targetName);
                    ChatUtil.sendMessage(sender, "unmute.already-unmuted", placeholders);
                }
            } else {
                ChatUtil.sendMessage(sender, "unmute.usage", new HashMap<>());
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
                if (player.hasPermission("lasttools.unmute") || player.isOp()) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
            }
        } else {
            if (args.length == 1) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
        }
        return list;
    }
}


