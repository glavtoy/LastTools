package ru.lastlord.lasttools.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.jetbrains.annotations.NotNull;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.HashMap;

public class CommandDay implements CommandExecutor {

    public CommandDay() {
        LastTools.instance.getCommand("day").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.hasPermission("lasttools.time.day") || player.hasPermission("lasttools.time.*") || player.hasPermission("*") || player.isOp()) {
                player.getWorld().setTime(6000);
                final HashMap<String, String> placeholders = new HashMap<>();
                placeholders.put("time", "day");
                ChatUtil.sendMessage(player, "day", placeholders);
            } else {
                for (Rank rank : RankManager.ranks) {
                    if (rank.getPermissions().contains("lasttools.time.day") || rank.getPermissions().contains("lasttools.time.*") || rank.getPermissions().contains("*")) {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("rank", rank.getPrefix());
                        ChatUtil.sendMessage(player, "no-perms", placeholders);
                        break;
                    }
                }
            }
        }
        return true;
    }
}