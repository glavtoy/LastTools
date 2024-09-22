package ru.lastlord.lasttools.economy;

import ru.lastlord.lasttools.LastTools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TokenEconomy {

    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static final ConcurrentHashMap<String, Integer> tokenCache = new ConcurrentHashMap<>();
    private static final long CACHE_EXPIRATION_TIME = 60000;
    private static final ConcurrentHashMap<String, Long> cacheTimestamps = new ConcurrentHashMap<>();

    public static void deposit(String playerName, double amount) {
        setBalance(playerName, getBalance(playerName).join() + amount);
    }

    public static void withdraw(String playerName, double amount) {
        setBalance(playerName, getBalance(playerName).join() - amount);
    }

    public static void setBalance(String playerName, double amount) {
        if (!LastTools.database.containsPlayer(playerName).join()) LastTools.database.addNewPlayer(playerName);
        LastTools.database.executeUpdate("UPDATE users SET tokens = " + amount + " WHERE player = '" + playerName + "';");
        tokenCache.put(playerName, (int) amount);
        cacheTimestamps.put(playerName, System.currentTimeMillis());
    }

    public static CompletableFuture<Integer> getBalance(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            if (tokenCache.containsKey(playerName)) {
                long cachedTime = cacheTimestamps.getOrDefault(playerName, 0L);
                //if (System.currentTimeMillis() - cachedTime < CACHE_EXPIRATION_TIME) {
                    return tokenCache.get(playerName);
                //}
            }
            ResultSet resultSet = null;
            try {
                if (!LastTools.database.containsPlayer(playerName).join()) LastTools.database.addNewPlayer(playerName);
                resultSet = LastTools.database.executeQuery("SELECT * FROM users WHERE player = '" + playerName + "';").join();
                if (resultSet != null && resultSet.next()) {
                    tokenCache.put(playerName, Integer.valueOf(resultSet.getString("tokens")));
                    cacheTimestamps.put(playerName, System.currentTimeMillis());
                    return Integer.parseInt(resultSet.getString("tokens"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return 0;
        }, executor);
    }
}
