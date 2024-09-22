package ru.lastlord.lasttools.crypto.manager;

import org.bukkit.Bukkit;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.crypto.economy.coin.Balance;
import ru.lastlord.lasttools.crypto.economy.coin.Coin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.LinkedList;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CryptoManager {

    private static final ConcurrentHashMap<String, LinkedList<Balance>> cryptoCache = new ConcurrentHashMap<>();
    private final long CACHE_EXPIRATION_TIME = 60000;
    private final ConcurrentHashMap<String, Long> cacheTimestamps = new ConcurrentHashMap<>();

    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static CompletableFuture<LinkedList<Balance>> getBalances(String name) {
        return CompletableFuture.supplyAsync(() -> {
            if (cryptoCache.containsKey(name)) {
                //if (System.currentTimeMillis() - cachedTime < CACHE_EXPIRATION_TIME) {
                return cryptoCache.get(name);
                //}
            }
            LinkedList<Balance> balances = new LinkedList<>();
            try (PreparedStatement statement = LastTools.database.getConnection().prepareStatement("SELECT coins FROM crypto WHERE name = ?")) {
                statement.setString(1, name);
                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        String unformattedCoins = result.getString(1);
                        if (unformattedCoins.contains(",")) {
                            String[] mapArray = unformattedCoins.split(",");
                            for (String map : mapArray) {
                                String[] coinAndValueArray = map.split(":");
                                String coin = coinAndValueArray[0];
                                double value = Double.parseDouble(coinAndValueArray[1]);
                                balances.add(new Balance(CoinManager.getCoin(coin), value));
                            }
                        } else {
                            String[] coinAndValueArray = unformattedCoins.split(":");
                            String coin = coinAndValueArray[0];
                            double value = Double.parseDouble(coinAndValueArray[1]);
                            balances.add(new Balance(CoinManager.getCoin(coin), value));
                        }
                    } else {
                        StringBuilder coins = new StringBuilder();
                        CoinManager.getCoins().forEach(coin -> {
                            String symbol = coin.getSymbol();
                            coins.append(symbol + ":0.0,");
                        });
                        LastTools.database.executeUpdate("INSERT INTO crypto (name, coins) VALUES ('" + name + "', '" + coins + "')  ON CONFLICT (name) DO NOTHING;");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return balances;
        }, executor);
    }

    public static double getBalance(LinkedList<Balance> balances, Coin coin) {
        for (Balance balance : balances) {
            if (coin != null && coin.getSymbol() != null && balance.getCoin() != null && balance.getCoin().getSymbol().equalsIgnoreCase(coin.getSymbol())) {
                return balance.getAmount();
            }
        }
        return 0;
    }

    public static void setBalance(String name, Balance balance) {
        LinkedList<Balance> balances = getBalances(name).join();
        boolean balanceUpdated = false;
        for (Balance bal : balances) {
            if (bal.getCoin() != null && bal.getCoin().getSymbol().equals(balance.getCoin().getSymbol())) {
                bal.setAmount(balance.getAmount());
                updateUserCoins(name, balances);
                balanceUpdated = true;
                break;
            }
        }

        if (!balanceUpdated) {
            balances.add(balance);
            updateUserCoins(name, balances);
        }
    }

    private static void updateUserCoins(String name, LinkedList<Balance> balances) {
        Bukkit.getScheduler().runTaskAsynchronously(LastTools.instance, () -> {
            cryptoCache.put(name, balances);
            StringJoiner coinsJoiner = new StringJoiner(",");
            for (Balance bal : balances) {
                if (bal.getCoin() != null) coinsJoiner.add(bal.getCoin().getSymbol() + ":" + bal.getAmount());
            }
            try (PreparedStatement statement = LastTools.database.getConnection().prepareStatement("UPDATE crypto SET coins = ? WHERE name = ?")) {
                statement.setString(1, coinsJoiner.toString());
                statement.setString(2, name);
                statement.executeUpdate();
            } catch (SQLException ignored) {}
        });
    }
}
