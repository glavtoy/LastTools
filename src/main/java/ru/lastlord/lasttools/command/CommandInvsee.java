package ru.lastlord.lasttools.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandInvsee implements CommandExecutor, TabCompleter {

    public CommandInvsee() {
        LastTools.instance.getCommand("invsee").setExecutor(this);
        LastTools.instance.getCommand("invsee").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("lasttools.invsee") || player.hasPermission("*") || player.isOp()) {
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        player.openInventory(target.getInventory());
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[0]);
                        ChatUtil.sendMessage(player, "invsee.open", placeholders);
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[0]);
                        ChatUtil.sendMessage(player, "offline-player", placeholders);
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.invsee") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.invsee") || player.hasPermission("lasttools.invsee") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendMessage(player, "invsee.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.invsee") || rank.getPermissions().contains("*")) {
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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("lasttools.invsee.other") || player.hasPermission("lasttools.invsee.*") || player.isOp()) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
            }
        }
        return list;
    }
}
