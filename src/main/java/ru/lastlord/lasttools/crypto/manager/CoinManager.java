package ru.lastlord.lasttools.crypto.manager;

import lombok.Getter;
import lombok.Setter;

import ru.lastlord.lasttools.crypto.configuration.Config;
import ru.lastlord.lasttools.crypto.economy.coin.Coin;
import ru.lastlord.lasttools.crypto.util.CryptoPriceChecker;

import java.util.LinkedList;

public class CoinManager {

    @Getter @Setter private static LinkedList<Coin> coins;

    public static Coin getCoin(String symbol) {
        for (Coin coin : coins) if (coin.getSymbol().equalsIgnoreCase(symbol)) return coin;
        return null;
    }

    public static void initCoins() {
        coins = new LinkedList<>();
        Config.getConfig().getStringList("coins").forEach(coin -> {
            final Coin c = new Coin();
            c.setSymbol(coin);
            CryptoPriceChecker.updatePrice(c);
            CoinManager.getCoins().add(c);
        });
    }
}
