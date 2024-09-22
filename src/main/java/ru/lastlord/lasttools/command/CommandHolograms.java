package ru.lastlord.lasttools.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.hologram.hologram.Hologram;
import ru.lastlord.lasttools.hologram.manager.HologramManager;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandHolograms implements CommandExecutor, TabCompleter {

    public CommandHolograms() {
        LastTools.instance.getCommand("holograms").setExecutor(this);
        LastTools.instance.getCommand("holograms").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    final String id = args[1];
                    final Hologram hologram = HologramManager.getHologramById(id);
                    if (hologram == null) {
                        HologramManager.createHologram(id, player.getLocation());
                        HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("id", id);
                        ChatUtil.sendMessage(player, "holograms.create", placeholders);
                    } else {
                        HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("id", id);
                        ChatUtil.sendMessage(player, "holograms.already-exists", placeholders);
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    final String id = args[1];
                    final Hologram hologram = HologramManager.getHologramById(id);
                    if (hologram != null) {
                        HologramManager.removeHologram(id);
                        HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("id", id);
                        ChatUtil.sendMessage(player, "holograms.remove", placeholders);
                    } else {
                        HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("id", id);
                        ChatUtil.sendMessage(player, "holograms.no-exists", placeholders);
                    }
                } else if (args[0].equalsIgnoreCase("teleport")) {
                    final String id = args[1];
                    final Hologram hologram = HologramManager.getHologramById(id);
                    if (hologram != null) {
                        player.teleport(hologram.getLocation());
                        HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("id", id);
                        ChatUtil.sendMessage(player, "holograms.teleport", placeholders);
                    } else {
                        HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("id", id);
                        ChatUtil.sendMessage(player, "holograms.no-exists", placeholders);
                    }
                } else {
                    if (player.hasPermission("lasttools.holograms") || player.hasPermission("*") || player.isOp()) {
                        ChatUtil.sendListMessage(player, "holograms.usage", new HashMap<>());
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.holograms") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    HologramManager.unloadHolograms();
                    HologramManager.loadHolograms();
                }
            } else {
                if (player.hasPermission("lasttools.holograms") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendListMessage(player, "holograms.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.holograms") || rank.getPermissions().contains("*")) {
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
        List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.hasPermission("lasttools.holograms") || player.isOp()) {
                if (args.length == 1) {
                    list.add("create");
                    list.add("remove");
                    list.add("teleport");
                    list.add("reload");
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("teleport")) HologramManager.holograms.forEach(hologram -> list.add(hologram.getId()));
                }
            }
        }
        return list;
    }
}