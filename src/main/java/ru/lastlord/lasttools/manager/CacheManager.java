package ru.lastlord.lasttools.manager;

import org.bukkit.scheduler.BukkitRunnable;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.economy.CoinEconomy;
import ru.lastlord.lasttools.economy.TokenEconomy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class CacheManager {

    public static void loadDatabaseCache() {
        new BukkitRunnable() {
            @Override
            public void run() {
                LastTools.database.ensureConnection();
                String query = "SELECT player, coins, tokens, hours, rank, kills, deaths FROM users;";
                try (PreparedStatement statement = LastTools.database.getConnection().prepareStatement(query)) {
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet != null) {
                            while (resultSet.next()) {
                                final String playerData = resultSet.getString("player");
                                final String coinsData = resultSet.getString("coins");
                                final String tokensData = resultSet.getString("tokens");
                                final String hoursData = resultSet.getString("hours");
                                final String rankData = resultSet.getString("rank");
                                final String killsData = resultSet.getString("kills");
                                final String deathsData = resultSet.getString("deaths");
                                if (coinsData != null && tokensData != null && hoursData != null && rankData != null && killsData != null && deathsData != null) {
                                    CoinEconomy.coinCache.put(playerData, Integer.parseInt(coinsData));
                                    TokenEconomy.tokenCache.put(playerData, Integer.parseInt(tokensData));
                                    LastTools.database.hoursCache.put(playerData, Integer.parseInt(hoursData)/3600);
                                    RankManager.rankCache.put(playerData, RankManager.getRankByTag(rankData));
                                    StatisticManager.killsCache.put(playerData, Integer.parseInt(killsData));
                                    StatisticManager.deathsCache.put(playerData, Integer.parseInt(deathsData));
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
                }
            }
        }.runTaskAsynchronously(LastTools.instance);
    }
}
