package ru.lastlord.lasttools.crypto.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.crypto.configuration.Config;
import ru.lastlord.lasttools.crypto.economy.coin.Balance;
import ru.lastlord.lasttools.crypto.economy.coin.Coin;
import ru.lastlord.lasttools.crypto.manager.CoinManager;
import ru.lastlord.lasttools.crypto.manager.CryptoManager;
import ru.lastlord.lasttools.crypto.manager.WalletManager;
import ru.lastlord.lasttools.crypto.util.ChatUtil;
import ru.lastlord.lasttools.crypto.util.CryptoPriceChecker;
import ru.lastlord.lasttools.crypto.util.SoundUtil;
import ru.lastlord.lasttools.economy.CoinEconomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CommandCrypto implements CommandExecutor, TabCompleter {

    public CommandCrypto() {
        LastTools.instance.getCommand("crypto").setExecutor(this);
        LastTools.instance.getCommand("crypto").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("send")) {
                    final String name = args[1];
                    final Coin coin = CoinManager.getCoin(args[2].toUpperCase());
                    final double amount = Double.parseDouble(args[3]);
                    if (amount >= Config.getConfig().getDouble("min-send") && amount <= Config.getConfig().getDouble("max-send")) {
                        if (coin != null) {
                            final LinkedList<Balance> playerBalances = CryptoManager.getBalances(player.getName()).join();
                            if (CryptoManager.getBalance(playerBalances, coin) >= amount) {
                                final LinkedList<Balance> targetBalances = CryptoManager.getBalances(name).join();
                                final Player target = Bukkit.getPlayer(name);
                                if (!name.equalsIgnoreCase(player.getName())) {
                                    CryptoManager.setBalance(player.getName(), new Balance(coin, CryptoManager.getBalance(playerBalances, coin) - amount));
                                    CryptoManager.setBalance(name, new Balance(coin, CryptoManager.getBalance(targetBalances, coin) + amount));
                                    final HashMap<String, String> placeholders = new HashMap<>();
                                    placeholders.put("{amount}", String.valueOf(amount));
                                    placeholders.put("{crypto}", coin.getSymbol().toUpperCase());
                                    placeholders.put("{recipient}", name);
                                    placeholders.put("{sender}", player.getName());
                                    ChatUtil.sendMessage(player, "send.sender", placeholders);
                                    SoundUtil.playSound(player, "send");
                                    if (target != null) {
                                        ChatUtil.sendMessage(target, "send.recipient", placeholders);
                                        SoundUtil.playSound(target, "send");
                                    }
                                } else {
                                    ChatUtil.sendMessage(player, "yourself", null);
                                    SoundUtil.playSound(player, "yourself");
                                }
                            } else {
                                ChatUtil.sendMessage(player, "no-coin", null);
                                SoundUtil.playSound(player, "no-coin");
                            }
                        } else {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("{crypto}", coin.getSymbol().toUpperCase());
                            ChatUtil.sendMessage(player, "no-exists-coin", placeholders);
                            SoundUtil.playSound(player, "no-exists-coin");
                        }
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("{min}", String.valueOf(Config.getConfig().getDouble("min-buy")));
                        placeholders.put("{max}", String.valueOf(Config.getConfig().getDouble("max-buy")));
                        ChatUtil.sendMessage(player, "limit", placeholders);
                        SoundUtil.playSound(player, "limit");
                    }
                } else WalletManager.openWalletGui(player);
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("buy")) {
                    final Coin coin = CoinManager.getCoin(args[1].toUpperCase());
                    final double amount = Double.parseDouble(args[2]);
                    if (amount >= Config.getConfig().getDouble("min-buy") && amount <= Config.getConfig().getDouble("max-buy")) {
                        if (coin != null) {
                            final LinkedList<Balance> playerBalances = CryptoManager.getBalances(player.getName()).join();
                            if (CoinEconomy.getBalance(player.getName()).join() >= amount * coin.getCost()) {
                                CoinEconomy.withdraw(player.getName(), amount * coin.getCost());
                                CryptoManager.setBalance(player.getName(), new Balance(coin, CryptoManager.getBalance(playerBalances, coin) + amount));
                                if (WalletManager.wallets.get(player.getUniqueId()) != null) WalletManager.wallets.get(player.getUniqueId()).getDecorations().forEach(decoration -> decoration.updateItemStack());
                                CoinManager.getCoins().forEach(CryptoPriceChecker::updatePrice);
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("{amount}", String.valueOf(amount));
                                placeholders.put("{crypto}", coin.getSymbol().toUpperCase());
                                placeholders.put("{money}", String.valueOf(amount * coin.getCost()));
                                placeholders.put("{currency}", Config.getConfig().getString("currency." + Config.getConfig().getString("economy").toLowerCase()));
                                ChatUtil.sendMessage(player, "buy", placeholders);
                                SoundUtil.playSound(player, "buy");
                            } else {
                                ChatUtil.sendMessage(player, "no-money", null);
                                SoundUtil.playSound(player, "no-money");
                            }
                        } else {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("{crypto}", coin.getSymbol().toUpperCase());
                            ChatUtil.sendMessage(player, "no-exists-coin", placeholders);
                            SoundUtil.playSound(player, "no-exists-coin");
                        }
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("{min}", String.valueOf(Config.getConfig().getDouble("min-buy")));
                        placeholders.put("{max}", String.valueOf(Config.getConfig().getDouble("max-buy")));
                        ChatUtil.sendMessage(player, "limit", placeholders);
                        SoundUtil.playSound(player, "limit");
                    }
                } else if (args[0].equalsIgnoreCase("sell")) {
                    final Coin coin = CoinManager.getCoin(args[1].toUpperCase());
                    final double amount = Double.parseDouble(args[2]);
                    if (amount >= Config.getConfig().getDouble("min-sell") && amount <= Config.getConfig().getDouble("max-sell")) {
                        if (coin != null) {
                            final LinkedList<Balance> playerBalances = CryptoManager.getBalances(player.getName()).join();
                            if (CryptoManager.getBalance(playerBalances, coin) >= amount) {
                                CoinEconomy.deposit(player.getName(), amount * coin.getCost());
                                CryptoManager.setBalance(player.getName(), new Balance(coin, CryptoManager.getBalance(playerBalances, coin) - amount));
                                if (WalletManager.wallets.get(player.getUniqueId()) != null) WalletManager.wallets.get(player.getUniqueId()).getDecorations().forEach(decoration -> decoration.updateItemStack());
                                CoinManager.getCoins().forEach(CryptoPriceChecker::updatePrice);
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("{amount}", String.valueOf(amount));
                                placeholders.put("{crypto}", coin.getSymbol().toUpperCase());
                                placeholders.put("{money}", String.valueOf(amount * coin.getCost()));
                                placeholders.put("{currency}", Config.getConfig().getString("currency." + Config.getConfig().getString("economy").toLowerCase()));
                                ChatUtil.sendMessage(player, "sell", placeholders);
                                SoundUtil.playSound(player, "sell");
                            } else {
                                ChatUtil.sendMessage(player, "no-coin", null);
                                SoundUtil.playSound(player, "no-coin");
                            }
                        } else {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("{crypto}", args[1].toUpperCase());
                            ChatUtil.sendMessage(player, "no-exists-coin", placeholders);
                            SoundUtil.playSound(player, "no-exists-coin");
                        }
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("{min}", String.valueOf(Config.getConfig().getDouble("min-sell")));
                        placeholders.put("{max}", String.valueOf(Config.getConfig().getDouble("max-sell")));
                        ChatUtil.sendMessage(player, "limit", placeholders);
                        SoundUtil.playSound(player, "limit");
                    }
                } else WalletManager.openWalletGui(player);
            } else WalletManager.openWalletGui(player);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        final List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                list.add("buy");
                list.add("sell");
                list.add("send");
            }
            if (args[0].equalsIgnoreCase("send")) {
                if (args.length == 2) Bukkit.getOnlinePlayers().forEach(p->list.add(p.getName()));
                if (args.length == 3) CoinManager.getCoins().forEach(coin->list.add(coin.getSymbol()));
                if (args.length == 4) {
                    final Coin coin = CoinManager.getCoin(args[2].toUpperCase());
                    if (coin != null) {
                        list.add(String.valueOf(CryptoManager.getBalance(CryptoManager.getBalances(player.getName()).join(), coin)));
                    }
                }
            } else if (args[0].equalsIgnoreCase("buy")) {
                if (args.length == 2) CoinManager.getCoins().forEach(coin->list.add(coin.getSymbol()));
            } else if (args[0].equalsIgnoreCase("sell")) {
                if (args.length == 2) CoinManager.getCoins().forEach(coin->list.add(coin.getSymbol()));
                if (args.length == 3) {
                    final Coin coin = CoinManager.getCoin(args[1].toUpperCase());
                    if (coin != null) {
                        list.add(String.valueOf(CryptoManager.getBalance(CryptoManager.getBalances(player.getName()).join(), coin)));
                    }
                }
            }
        }
        return list;
    }
}
