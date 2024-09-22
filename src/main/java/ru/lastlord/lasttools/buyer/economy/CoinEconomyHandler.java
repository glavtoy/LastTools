package ru.lastlord.lasttools.buyer.economy;

import org.bukkit.entity.Player;

import ru.lastlord.lasttools.buyer.economy.impl.EconomyHandler;
import ru.lastlord.lasttools.economy.CoinEconomy;

public class CoinEconomyHandler implements EconomyHandler {

    @Override
    public void deposit(Player player, int amount) {
        CoinEconomy.deposit(player.getName(), amount);
    }

    @Override
    public void withdraw(Player player, int amount) {
        CoinEconomy.withdraw(player.getName(), amount);
    }

    @Override
    public int getBalance(Player player) {
        return CoinEconomy.getBalance(player.getName()).join();
    }
}
