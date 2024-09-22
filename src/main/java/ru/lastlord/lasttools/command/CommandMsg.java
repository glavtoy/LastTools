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
import java.util.StringJoiner;

public class CommandMsg implements CommandExecutor {

    public CommandMsg() {
        LastTools.instance.getCommand("msg").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length >= 2) {
                if (player.hasPermission("lasttools.msg") || player.hasPermission("lasttools.msg.*") || player.hasPermission("*") || player.isOp()) {
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        final StringJoiner stringJoiner = new StringJoiner(" ");
                        for (int i = 1; i < args.length; i++) {
                            stringJoiner.add(args[i]);
                        }
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", player.getName());
                        placeholders.put("message", stringJoiner.toString());
                        ChatUtil.sendMessage(target, "chat-format.personal.to", placeholders);
                        final HashMap<String, String> placeholders1 = new HashMap<>();
                        placeholders1.put("player", target.getName());
                        placeholders1.put("message", stringJoiner.toString());
                        ChatUtil.sendMessage(player, "chat-format.personal.from", placeholders1);
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[0]);
                        ChatUtil.sendMessage(player, "offline-player", placeholders);
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.msg") || rank.getPermissions().contains("lasttools.msg.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.msg") || player.hasPermission("lasttools.msg.*") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendMessage(player, "msg.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.msg") || rank.getPermissions().contains("lasttools.msg.*") || rank.getPermissions().contains("*")) {
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
