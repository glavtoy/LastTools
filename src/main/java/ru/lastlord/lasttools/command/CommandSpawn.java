package ru.lastlord.lasttools.command;

import org.bukkit.Location;
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

public class CommandSpawn implements CommandExecutor {

    public CommandSpawn() {
        LastTools.instance.getCommand("spawn").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.hasPermission("lasttools.spawn") || player.hasPermission("*") || player.isOp()) {
                final Location spawn = Config.getConfig().getLocation("spawn-location");
                if (spawn != null) {
                    player.teleport(spawn);
                    ChatUtil.sendMessage(player, "spawn", new HashMap<>());
                } else ChatUtil.sendMessage(player, "not-found-spawn", new HashMap<>());
            } else {
                for (Rank rank : RankManager.ranks) {
                    if (rank.getPermissions().contains("lasttools.spawn") || rank.getPermissions().contains("*")) {
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
