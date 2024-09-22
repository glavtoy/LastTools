package ru.lastlord.lasttools.crypto.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.crypto.manager.CoinManager;
import ru.lastlord.lasttools.crypto.manager.ConfigurationManager;
import ru.lastlord.lasttools.crypto.manager.WalletManager;
import ru.lastlord.lasttools.crypto.runnable.CryptoPriceUpdateRunnable;
import ru.lastlord.lasttools.crypto.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class CommandLastCrypto implements CommandExecutor, TabCompleter {

    public CommandLastCrypto() {
        LastTools.instance.getCommand("lastcrypto").setExecutor(this);
        LastTools.instance.getCommand("lastcrypto").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.isOp() || player.hasPermission("glavcrypto.admin")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        WalletManager.clearGUI();
                        ConfigurationManager.loadConfigurations();
                        CoinManager.initCoins();
                        CryptoPriceUpdateRunnable.start();
                        ChatUtil.sendMessage(sender, "reload", null);
                    }
                }
            }
        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    WalletManager.clearGUI();
                    ConfigurationManager.loadConfigurations();
                    CoinManager.initCoins();
                    CryptoPriceUpdateRunnable.start();
                    ChatUtil.sendMessage(sender, "reload", null);
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        final List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.isOp() || player.hasPermission("glavcrypto.admin")) list.add("reload");
        }
        return list;
    }
}
