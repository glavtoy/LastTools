package ru.lastlord.lasttools.buyer.economy;

import org.bukkit.entity.Player;

import ru.lastlord.lasttools.buyer.economy.impl.EconomyHandler;
import ru.lastlord.lasttools.economy.TokenEconomy;

public class TokenEconomyHandler implements EconomyHandler {

    @Override
    public void deposit(Player player, int amount) {
        TokenEconomy.deposit(player.getName(), amount);
    }

    @Override
    public void withdraw(Player player, int amount) {
        TokenEconomy.withdraw(player.getName(), amount);
    }

    @Override
    public int getBalance(Player player) {
        return TokenEconomy.getBalance(player.getName()).join();
    }
}
