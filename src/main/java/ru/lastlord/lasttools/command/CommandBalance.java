package ru.lastlord.lasttools.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.economy.CoinEconomy;
import ru.lastlord.lasttools.economy.TokenEconomy;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.HashMap;

public class CommandBalance implements CommandExecutor {

    public CommandBalance() {
        LastTools.instance.getCommand("balance").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final HashMap<String, String> placeholders = new HashMap<>();
            placeholders.put("coins", String.valueOf(CoinEconomy.getBalance(player.getName()).join()));
            placeholders.put("tokens", String.valueOf(TokenEconomy.getBalance(player.getName()).join()));
            ChatUtil.sendListMessage(player, "balance", placeholders);
        }
        return true;
    }
}
