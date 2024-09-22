package ru.lastlord.lasttools.command;

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

public class CommandSpeed implements CommandExecutor, TabCompleter {

    public CommandSpeed() {
        LastTools.instance.getCommand("speed").setExecutor(this);
        LastTools.instance.getCommand("speed").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("lasttools.speed") || player.hasPermission("*") || player.isOp()) {
                    try {
                        int speed = Integer.parseInt(args[0]);
                        if (speed > 0 && speed < 11) {
                            player.setWalkSpeed(speed / 10.0f);
                            player.setFlySpeed(speed / 10.0f);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("speed", String.valueOf(speed));
                            ChatUtil.sendMessage(player, "speed.player", placeholders);
                        } else {
                            ChatUtil.sendMessage(player, "speed.limit", new HashMap<>());
                        }
                    } catch (NumberFormatException e) {
                        ChatUtil.sendMessage(player, "speed.not-number", new HashMap<>());
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.speed") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.speed") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendMessage(player, "speed.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.speed") || rank.getPermissions().contains("*")) {
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
                if (player.hasPermission("lasttools.speed") || player.isOp()) for (int i = 0; i <= 10; i++) list.add(String.valueOf(i));
            }
        }
        return list;
    }
}
