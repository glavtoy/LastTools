package ru.lastlord.lasttools.command;

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
import java.util.LinkedList;

public class CommandStorm implements CommandExecutor {

    public CommandStorm() {
        LastTools.instance.getCommand("storm").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.hasPermission("lasttools.weather.storm") || player.hasPermission("lasttools.weather.*") || player.hasPermission("*") || player.isOp()) {
                player.getWorld().setStorm(true);
                player.getWorld().setThundering(true);
                final HashMap<String, String> placeholders = new HashMap<>();
                placeholders.put("weather", "storm");
                ChatUtil.sendMessage(player, "weather", placeholders);
            } else {
                for (Rank rank : RankManager.ranks) {
                    if (rank.getPermissions().contains("lasttools.weather.storm") || rank.getPermissions().contains("lasttools.weather.*") || rank.getPermissions().contains("*")) {
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
