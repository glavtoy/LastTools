package ru.lastlord.lasttools.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.HashMap;

public class CommandTeleport implements CommandExecutor {

    public CommandTeleport() {
        LastTools.instance.getCommand("teleport").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("lasttools.teleport") || player.hasPermission("lasttools.teleport.*") || player.hasPermission("*") || player.isOp()) {
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        player.teleport(target.getLocation());
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[0]);
                        ChatUtil.sendMessage(player, "teleport.player", placeholders);
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[0]);
                        ChatUtil.sendMessage(player, "offline-player", placeholders);
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.teleport") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else if (args.length == 2) {
                if (player.hasPermission("lasttools.teleport.other") || player.hasPermission("lasttools.teleport.*") || player.hasPermission("*") || player.isOp()) {
                    final Player target1 = Bukkit.getPlayer(args[0]);
                    final Player target2 = Bukkit.getPlayer(args[1]);
                    if (target1 != null) {
                        if (target2 != null) {
                            target1.teleport(target2.getLocation());
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[0]);
                            placeholders.put("target", args[1]);
                            ChatUtil.sendMessage(player, "teleport.other", placeholders);
                        } else {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[1]);
                            ChatUtil.sendMessage(player, "offline-player", placeholders);
                        }
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[0]);
                        ChatUtil.sendMessage(player, "offline-player", placeholders);
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.teleport.other") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.teleport") || player.hasPermission("lasttools.teleport.other") || player.hasPermission("lasttools.teleport.*") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendListMessage(player, "teleport.multiply-use", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.teleport") || rank.getPermissions().contains("lasttools.teleport.other") || rank.getPermissions().contains("lasttools.teleport.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }
}
