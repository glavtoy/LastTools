package ru.lastlord.lasttools.buyer.economy.impl;

import org.bukkit.entity.Player;

public interface EconomyHandler {

    void deposit(Player player, int amount);

    void withdraw(Player player, int amount);

    int getBalance(Player player);
}
