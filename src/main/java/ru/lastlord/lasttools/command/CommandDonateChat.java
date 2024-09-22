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

public class CommandDonateChat implements CommandExecutor {

    public CommandDonateChat() {
        LastTools.instance.getCommand("donatechat").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length >= 1) {
                if (player.hasPermission("lasttools.donatechat") || player.hasPermission("*") || player.isOp()) {
                        final StringJoiner stringJoiner = new StringJoiner(" ");
                        for (int i = 0; i < args.length; i++) {
                            stringJoiner.add(args[i]);
                        }
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", player.getName());
                        placeholders.put("rank", RankManager.getRank(player.getName()).join().getPrefix());
                        placeholders.put("message", stringJoiner.toString());
                        Bukkit.getOnlinePlayers().forEach(p -> {
                            if (p.hasPermission("lasttools.donatechat") || p.hasPermission("*") || p.isOp()) ChatUtil.sendMessage(p, "donatechat.chat-format", placeholders);
                        });
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.donatechat") || rank.getPermissions().contains("lasttools.donatechat.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.donatechat") || player.hasPermission("lasttools.donatechat.*") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendMessage(player, "donatechat.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.donatechat") || rank.getPermissions().contains("lasttools.donatechat.*") || rank.getPermissions().contains("*")) {
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

