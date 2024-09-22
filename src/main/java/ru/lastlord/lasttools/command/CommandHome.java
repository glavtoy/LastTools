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
import ru.lastlord.lasttools.home.configuration.Homes;
import ru.lastlord.lasttools.home.home.Home;
import ru.lastlord.lasttools.home.manager.HomeManager;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandHome implements CommandExecutor, TabCompleter {

    public CommandHome() {
        LastTools.instance.getCommand("home").setExecutor(this);
        LastTools.instance.getCommand("home").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("lasttools.home") || player.hasPermission("*") || player.isOp()) {
                    final String home = args[0];
                    if (HomeManager.homeExists(player.getName(), home)) {
                        final Home h = HomeManager.getHomeByName(player.getName(), home);
                        if (h != null) {
                            player.teleport(h.getLocation());
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("home", args[0]);
                            ChatUtil.sendMessage(player, "home.teleport", placeholders);
                        }
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("home", args[0]);
                        ChatUtil.sendMessage(player, "home.no-exists", placeholders);
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.home") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.home") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendMessage(player, "home.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.home") || rank.getPermissions().contains("*")) {
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
                if (player.hasPermission("lasttools.home") || player.isOp()) {
                    for (Homes homes : HomeManager.homes) {
                        if (homes.getPlayerName().equalsIgnoreCase(player.getName())) {
                            for (Home home : homes.getHomes()) list.add(home.getName());
                        }
                    }
                }
            }
        }
        return list;
    }
}
