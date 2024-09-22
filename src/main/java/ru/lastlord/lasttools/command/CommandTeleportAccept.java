package ru.lastlord.lasttools.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.teleport.data.TeleportData;
import ru.lastlord.lasttools.teleport.manager.TeleportManager;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.HashMap;

public class CommandTeleportAccept implements CommandExecutor {

    public CommandTeleportAccept() {
        LastTools.instance.getCommand("teleportaccept").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("lasttools.teleportaccept") || player.hasPermission("*") || player.isOp()) {
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        final TeleportData teleportData = TeleportManager.getTeleportData(target, player);
                        if (teleportData != null) {
                            final Location location = teleportData.accept();
                            target.teleport(location);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", player.getName());
                            ChatUtil.sendMessage(target, "teleportaccept.from.accept", placeholders);
                            final HashMap<String, String> placeholders1 = new HashMap<>();
                            placeholders1.put("player", target.getName());
                            ChatUtil.sendMessage(player, "teleportaccept.to.accept", placeholders);
                        } else {
                            ChatUtil.sendMessage(player, "teleportaccept.not-found", new HashMap<>());
                        }
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.teleportaccept") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.teleportaccept") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendMessage(player, "teleportaccept.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.teleportaccept") || rank.getPermissions().contains("*")) {
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
