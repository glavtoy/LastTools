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
import ru.lastlord.lasttools.configuration.Config;
import ru.lastlord.lasttools.economy.CoinEconomy;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandCoins implements CommandExecutor, TabCompleter {

    public CommandCoins() {
        LastTools.instance.getCommand("coins").setExecutor(this);
        LastTools.instance.getCommand("coins").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("send")) {
                    if (player.hasPermission("lasttools.coins.send") || player.hasPermission("lasttools.coins.*") || player.hasPermission("*") || player.isOp()) {
                        try {
                            final double amount = Double.parseDouble(args[2]);
                            final double coins = CoinEconomy.getBalance(player.getName()).join();
                            final int min = Config.getConfig().getInt("locale.coins.send-limit.min");
                            final int max = Config.getConfig().getInt("locale.coins.send-limit.max");
                            if (amount <= coins && amount <= max && amount > min) {
                                if (!player.getName().equalsIgnoreCase(args[1])) {
                                    CoinEconomy.withdraw(player.getName(), amount);
                                    CoinEconomy.deposit(args[1], amount);
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("player", args[1]);
                                    placeholders.put("amount", String.valueOf(amount));
                                    ChatUtil.sendMessage(player, "coins.send", placeholders);
                                    final Player target = Bukkit.getPlayer(args[1]);
                                    final HashMap<String, String> placeholders1 = new HashMap<>();
                                    placeholders1.put("player", player.getName());
                                    placeholders1.put("amount", String.valueOf(amount));
                                    if (target != null) ChatUtil.sendMessage(target, "coins.receive", placeholders1);
                                } else {
                                    ChatUtil.sendMessage(player, "coins.yourself", new HashMap<>());
                                }
                            } else {
                                ChatUtil.sendMessage(player, "coins.limit", new HashMap<>());
                            }
                        } catch (NumberFormatException e) {
                            ChatUtil.sendMessage(player, "not-number", new HashMap<>());
                        }
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.coins.send") || rank.getPermissions().contains("lasttools.coins.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (player.hasPermission("lasttools.coins.add") || player.hasPermission("lasttools.coins.*") || player.hasPermission("*") || player.isOp()) {
                        try {
                            final double amount = Double.parseDouble(args[2]);
                            CoinEconomy.deposit(args[1], amount);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[1]);
                            placeholders.put("amount", String.valueOf(CoinEconomy.getBalance(args[1])));
                            ChatUtil.sendMessage(player, "coins.edit", placeholders);
                        } catch (NumberFormatException e) {
                            ChatUtil.sendMessage(player, "not-number", new HashMap<>());
                        }
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.coins.add") || rank.getPermissions().contains("lasttools.coins.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (player.hasPermission("lasttools.coins.remove") || player.hasPermission("lasttools.coins.*") || player.hasPermission("*") || player.isOp()) {
                        try {
                            final double amount = Double.parseDouble(args[2]);
                            CoinEconomy.withdraw(args[1], amount);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[1]);
                            placeholders.put("amount", String.valueOf(CoinEconomy.getBalance(args[1])));
                            ChatUtil.sendMessage(player, "coins.edit", placeholders);
                        } catch (NumberFormatException e) {
                            ChatUtil.sendMessage(player, "not-number", new HashMap<>());
                        }
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.coins.remove") || rank.getPermissions().contains("lasttools.coins.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (player.hasPermission("lasttools.coins.set") || player.hasPermission("lasttools.coins.*") || player.hasPermission("*") || player.isOp()) {
                        try {
                            final double amount = Double.parseDouble(args[2]);
                            CoinEconomy.withdraw(args[1], CoinEconomy.getBalance(args[1]).join());
                            CoinEconomy.deposit(args[1], amount);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[1]);
                            placeholders.put("amount", String.valueOf(CoinEconomy.getBalance(args[1])));
                            ChatUtil.sendMessage(player, "coins.edit", placeholders);
                        } catch (NumberFormatException e) {
                            ChatUtil.sendMessage(player, "not-number", new HashMap<>());
                        }
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.coins.set") || rank.getPermissions().contains("lasttools.coins.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else {
                    if (player.hasPermission("lasttools.coins.send") || player.hasPermission("lasttools.coins.set") || player.hasPermission("lasttools.coins.add") || player.hasPermission("lasttools.coins.remove") || player.hasPermission("lasttools.coins.*") || player.hasPermission("*") || player.isOp()) ChatUtil.sendMessage(player, "coins.usage", new HashMap<>());
                    else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.coins.set") || rank.getPermissions().contains("lasttools.coins.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.coins.send") || player.hasPermission("lasttools.coins.set") || player.hasPermission("lasttools.coins.add") || player.hasPermission("lasttools.coins.remove") || player.hasPermission("lasttools.coins.*") || player.hasPermission("*") || player.isOp()) ChatUtil.sendMessage(player, "coins.usage", new HashMap<>());
                else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.coins.set") || rank.getPermissions().contains("lasttools.coins.*") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            }
        } else {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    try {
                        final double amount = Double.parseDouble(args[2]);
                        CoinEconomy.deposit(args[1], amount);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[1]);
                        placeholders.put("amount", String.valueOf(CoinEconomy.getBalance(args[1])));
                        ChatUtil.sendMessage(sender, "coins.edit", placeholders);
                    } catch (NumberFormatException e) {
                        ChatUtil.sendMessage(sender, "not-number", new HashMap<>());
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    try {
                        final double amount = Double.parseDouble(args[2]);
                        CoinEconomy.withdraw(args[1], amount);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[1]);
                        placeholders.put("amount", String.valueOf(CoinEconomy.getBalance(args[1])));
                        ChatUtil.sendMessage(sender, "coins.edit", placeholders);
                    } catch (NumberFormatException e) {
                        ChatUtil.sendMessage(sender, "not-number", new HashMap<>());
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    try {
                        final double amount = Double.parseDouble(args[2]);
                        CoinEconomy.withdraw(args[1], CoinEconomy.getBalance(args[1]).join());
                        CoinEconomy.deposit(args[1], amount);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[1]);
                        placeholders.put("amount", String.valueOf(CoinEconomy.getBalance(args[1])));
                        ChatUtil.sendMessage(sender, "coins.edit", placeholders);
                    } catch (NumberFormatException e) {
                        ChatUtil.sendMessage(sender, "not-number", new HashMap<>());
                    }
                } else ChatUtil.sendMessage(sender, "coins.usage", new HashMap<>());
            } else ChatUtil.sendMessage(sender, "coins.usage", new HashMap<>());
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("lasttools.coins.send") || player.hasPermission("lasttools.coins.*") || player.hasPermission("*") || player.isOp()) list.add("send");
                if (player.hasPermission("lasttools.coins.set") || player.hasPermission("lasttools.coins.*") || player.hasPermission("*") || player.isOp()) list.add("set");
                if (player.hasPermission("lasttools.coins.add") || player.hasPermission("lasttools.coins.*") || player.hasPermission("*") || player.isOp()) list.add("add");
                if (player.hasPermission("lasttools.coins.remove") || player.hasPermission("lasttools.coins.*") || player.hasPermission("*") || player.isOp()) list.add("remove");
            } else if (args.length == 2) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
            else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("send")) {
                    if (player.hasPermission("lasttools.coins.send") || player.hasPermission("lasttools.coins.*") || player.hasPermission("*") || player.isOp()) {
                        list.add(String.valueOf(CoinEconomy.getBalance(player.getName())));
                    }
                }
            }
        } else {
            if (args.length == 1) {
                list.add("set");
                list.add("add");
                list.add("remove");
            } else if (args.length == 2) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
        }
        return list;
    }
}
