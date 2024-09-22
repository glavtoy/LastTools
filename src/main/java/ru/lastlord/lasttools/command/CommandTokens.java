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
import ru.lastlord.lasttools.economy.TokenEconomy;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandTokens implements CommandExecutor, TabCompleter {

    public CommandTokens() {
        LastTools.instance.getCommand("tokens").setExecutor(this);
        LastTools.instance.getCommand("tokens").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("send")) {
                    if (player.hasPermission("lasttools.tokens.send") || player.hasPermission("lasttools.tokens.*") || player.hasPermission("*") || player.isOp()) {
                        try {
                            final double amount = Double.parseDouble(args[2]);
                            final double tokens = TokenEconomy.getBalance(player.getName()).join();
                            final int min = Config.getConfig().getInt("locale.tokens.send-limit.min");
                            final int max = Config.getConfig().getInt("locale.tokens.send-limit.max");
                            if (amount <= tokens && amount <= max && amount > min) {
                                if (!player.getName().equalsIgnoreCase(args[1])) {
                                    TokenEconomy.withdraw(player.getName(), amount);
                                    TokenEconomy.deposit(args[1], amount);
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("player", args[1]);
                                    placeholders.put("amount", String.valueOf(amount));
                                    ChatUtil.sendMessage(player, "tokens.send", placeholders);
                                    final Player target = Bukkit.getPlayer(args[1]);
                                    final HashMap<String, String> placeholders1 = new HashMap<>();
                                    placeholders1.put("player", player.getName());
                                    placeholders1.put("amount", String.valueOf(amount));
                                    if (target != null) ChatUtil.sendMessage(target, "tokens.receive", placeholders1);
                                } else {
                                    ChatUtil.sendMessage(player, "tokens.yourself", new HashMap<>());
                                }
                            } else {
                                ChatUtil.sendMessage(player, "tokens.limit", new HashMap<>());
                            }
                        } catch (NumberFormatException e) {
                            ChatUtil.sendMessage(player, "not-number", new HashMap<>());
                        }
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.tokens.send") || rank.getPermissions().contains("lasttools.tokens.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (player.hasPermission("lasttools.tokens.add") || player.hasPermission("lasttools.tokens.*") || player.hasPermission("*") || player.isOp()) {
                        try {
                            final double amount = Double.parseDouble(args[2]);
                            TokenEconomy.deposit(args[1], amount);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[1]);
                            placeholders.put("amount", String.valueOf(TokenEconomy.getBalance(args[1])));
                            ChatUtil.sendMessage(player, "tokens.edit", placeholders);
                        } catch (NumberFormatException e) {
                            ChatUtil.sendMessage(player, "not-number", new HashMap<>());
                        }
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.tokens.add") || rank.getPermissions().contains("lasttools.tokens.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (player.hasPermission("lasttools.tokens.remove") || player.hasPermission("lasttools.tokens.*") || player.hasPermission("*") || player.isOp()) {
                        try {
                            final double amount = Double.parseDouble(args[2]);
                            TokenEconomy.withdraw(args[1], amount);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[1]);
                            placeholders.put("amount", String.valueOf(TokenEconomy.getBalance(args[1])));
                            ChatUtil.sendMessage(player, "tokens.edit", placeholders);
                        } catch (NumberFormatException e) {
                            ChatUtil.sendMessage(player, "not-number", new HashMap<>());
                        }
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.tokens.remove") || rank.getPermissions().contains("lasttools.tokens.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (player.hasPermission("lasttools.tokens.set") || player.hasPermission("lasttools.tokens.*") || player.hasPermission("*") || player.isOp()) {
                        try {
                            final double amount = Double.parseDouble(args[2]);
                            TokenEconomy.withdraw(args[1], TokenEconomy.getBalance(args[1]).join());
                            TokenEconomy.deposit(args[1], amount);
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("player", args[1]);
                            placeholders.put("amount", String.valueOf(TokenEconomy.getBalance(args[1])));
                            ChatUtil.sendMessage(player, "tokens.edit", placeholders);
                        } catch (NumberFormatException e) {
                            ChatUtil.sendMessage(player, "not-number", new HashMap<>());
                        }
                    } else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.tokens.set") || rank.getPermissions().contains("lasttools.tokens.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                } else {
                    if (player.hasPermission("lasttools.tokens.send") || player.hasPermission("lasttools.tokens.set") || player.hasPermission("lasttools.tokens.add") || player.hasPermission("lasttools.tokens.remove") || player.hasPermission("lasttools.tokens.*") || player.hasPermission("*") || player.isOp()) ChatUtil.sendMessage(player, "tokens.usage", new HashMap<>());
                    else {
                        for (Rank rank : RankManager.ranks) {
                            if (rank.getPermissions().contains("lasttools.tokens.set") || rank.getPermissions().contains("lasttools.tokens.*") || rank.getPermissions().contains("*")) {
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("rank", rank.getPrefix());
                                ChatUtil.sendMessage(player, "no-perms", placeholders);
                                break;
                            }
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.tokens.send") || player.hasPermission("lasttools.tokens.set") || player.hasPermission("lasttools.tokens.add") || player.hasPermission("lasttools.tokens.remove") || player.hasPermission("lasttools.tokens.*") || player.hasPermission("*") || player.isOp()) ChatUtil.sendMessage(player, "tokens.usage", new HashMap<>());
                else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.tokens.set") || rank.getPermissions().contains("lasttools.tokens.*") || rank.getPermissions().contains("*")) {
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
                        TokenEconomy.deposit(args[1], amount);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[1]);
                        placeholders.put("amount", String.valueOf(TokenEconomy.getBalance(args[1])));
                        ChatUtil.sendMessage(sender, "tokens.edit", placeholders);
                    } catch (NumberFormatException e) {
                        ChatUtil.sendMessage(sender, "not-number", new HashMap<>());
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    try {
                        final double amount = Double.parseDouble(args[2]);
                        TokenEconomy.withdraw(args[1], amount);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[1]);
                        placeholders.put("amount", String.valueOf(TokenEconomy.getBalance(args[1])));
                        ChatUtil.sendMessage(sender, "tokens.edit", placeholders);
                    } catch (NumberFormatException e) {
                        ChatUtil.sendMessage(sender, "not-number", new HashMap<>());
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    try {
                        final double amount = Double.parseDouble(args[2]);
                        TokenEconomy.withdraw(args[1], TokenEconomy.getBalance(args[1]).join());
                        TokenEconomy.deposit(args[1], amount);
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[1]);
                        placeholders.put("amount", String.valueOf(TokenEconomy.getBalance(args[1])));
                        ChatUtil.sendMessage(sender, "tokens.edit", placeholders);
                    } catch (NumberFormatException e) {
                        ChatUtil.sendMessage(sender, "not-number", new HashMap<>());
                    }
                } else ChatUtil.sendMessage(sender, "tokens.usage", new HashMap<>());
            } else ChatUtil.sendMessage(sender, "tokens.usage", new HashMap<>());
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("lasttools.tokens.send") || player.hasPermission("lasttools.tokens.*") || player.hasPermission("*") || player.isOp()) list.add("send");
                if (player.hasPermission("lasttools.tokens.set") || player.hasPermission("lasttools.tokens.*") || player.hasPermission("*") || player.isOp()) list.add("set");
                if (player.hasPermission("lasttools.tokens.add") || player.hasPermission("lasttools.tokens.*") || player.hasPermission("*") || player.isOp()) list.add("add");
                if (player.hasPermission("lasttools.tokens.remove") || player.hasPermission("lasttools.tokens.*") || player.hasPermission("*") || player.isOp()) list.add("remove");
            } else if (args.length == 2) Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
            else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("send")) {
                    if (player.hasPermission("lasttools.tokens.send") || player.hasPermission("lasttools.tokens.*") || player.hasPermission("*") || player.isOp()) {
                        list.add(String.valueOf(TokenEconomy.getBalance(player.getName())));
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
