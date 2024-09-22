package ru.lastlord.lasttools.sql;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.postgresql.Driver;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.ban.BanData;
import ru.lastlord.lasttools.manager.StatisticManager;

import java.sql.*;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

@Getter
public class Database {
    private Connection connection;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public final ConcurrentHashMap<String, Integer> hoursCache = new ConcurrentHashMap<>();
    private final long CACHE_EXPIRATION_TIME = 60000;
    private final ConcurrentHashMap<String, Long> cacheTimestamps = new ConcurrentHashMap<>();

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public Database(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public synchronized void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            DriverManager.registerDriver(new Driver());
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Не удалось подключиться к базе данных!", e);
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }

    public synchronized void ensureConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке соединения", e);
        }
    }

    public CompletableFuture<ResultSet> executeQuery(String query, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            ensureConnection();
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                setParameters(statement, params);
                return statement.executeQuery();
            } catch (SQLException e) {
                LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при выполнении запроса", e);
                throw new RuntimeException("Ошибка выполнения запроса", e);
            }
        }, executor);
    }

    public void unban(String playerName) {
        ensureConnection();
        if (!containsPlayer(playerName).join()) addNewPlayer(playerName);
        String query = "UPDATE users SET ban = '0' WHERE player = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerName);
            statement.executeUpdate();
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
        }
    }

    public void unmute(String playerName) {
        ensureConnection();
        if (!containsPlayer(playerName).join()) addNewPlayer(playerName);
        String query = "UPDATE users SET mute = '0' WHERE player = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerName);
            statement.executeUpdate();
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
        }
    }

    public void permanentBan(String playerName, String reason, String admin) {
        ensureConnection();
        if (!containsPlayer(playerName).join()) addNewPlayer(playerName);
        String query = "UPDATE users SET ban = '-1:" + reason + ":" + admin + "' WHERE player = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerName);
            statement.executeUpdate();
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
        }
    }

    public void permanentMute(String playerName, String reason, String admin) {
        ensureConnection();
        if (!containsPlayer(playerName).join()) addNewPlayer(playerName);
        String query = "UPDATE users SET mute = '-1:" + reason + ":" + admin + "' WHERE player = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerName);
            statement.executeUpdate();
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
        }
    }

    public CompletableFuture<Integer> getHours(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            if (hoursCache.containsKey(playerName)) {
                long cachedTime = cacheTimestamps.getOrDefault(playerName, 0L);
                //if (System.currentTimeMillis() - cachedTime < CACHE_EXPIRATION_TIME) {
                    return hoursCache.get(playerName);
                //}
            }
            ensureConnection();
            String query = "SELECT hours FROM users WHERE player = ?;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet != null && resultSet.next()) {
                        hoursCache.put(playerName, resultSet.getInt("hours")/3600);
                        cacheTimestamps.put(playerName, System.currentTimeMillis());
                        return resultSet.getInt("hours")/3600;
                    }
                }
            } catch (SQLException e) {
                LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
                return null;
            }
            return 0;
        }, executor);
    }

    public CompletableFuture<Integer> getDeaths(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            if (StatisticManager.deathsCache.containsKey(playerName)) {
                return StatisticManager.deathsCache.get(playerName);
            }
            ensureConnection();
            String query = "SELECT deaths FROM users WHERE player = ?;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet != null && resultSet.next()) {
                        StatisticManager.deathsCache.put(playerName, resultSet.getInt("deaths"));
                        return resultSet.getInt("deaths");
                    }
                }
            } catch (SQLException e) {
                LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
                return null;
            }
            return 0;
        }, executor);
    }

    public CompletableFuture<Integer> getKills(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            if (StatisticManager.killsCache.containsKey(playerName)) {
                return StatisticManager.killsCache.get(playerName);
            }
            ensureConnection();
            String query = "SELECT kills FROM users WHERE player = ?;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet != null && resultSet.next()) {
                        StatisticManager.killsCache.put(playerName, resultSet.getInt("kills"));
                        return resultSet.getInt("kills");
                    }
                }
            } catch (SQLException e) {
                LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
                return null;
            }
            return 0;
        }, executor);
    }

    public CompletableFuture<LinkedList<String>> getBannedNames() {
        return CompletableFuture.supplyAsync(() -> {
            final LinkedList<String> names = new LinkedList<>();
            ensureConnection();
            String query = "SELECT player, ban, mute FROM users;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet != null) {
                        while (resultSet.next()) {
                            String[] banArray;
                            long banTime = 0;
                            String[] muteArray;
                            long muteTime = 0;
                            final String banData = resultSet.getString("ban");
                            if (banData != null) {
                                banArray = banData.split(":");
                                banTime = Long.parseLong(banArray[0]);
                            }
                            final String muteData = resultSet.getString("mute");
                            if (muteData != null) {
                                muteArray = muteData.split(":");
                                muteTime = Long.parseLong(muteArray[0]);
                            }
                            if (banTime == -1 || banTime != 0 || muteTime == -1 || muteTime != 0) names.add(resultSet.getString("player"));
                        }
                    }
                }
            } catch (SQLException e) {
                LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
                return null;
            }
            return names;
        });
    }

    public void updateBanTimes() {
        final LinkedList<String> names = LastTools.database.getBannedNames().join();
        ensureConnection();
        String query = "SELECT player, ban, mute FROM users;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet != null) {
                    while (resultSet.next()) {
                        final String name = resultSet.getString("player");
                        if (name != null) {
                            if (names.contains(name)) {
                                String[] banArray;
                                long oldBanTime = 0;
                                long banTime = 0;
                                String[] muteArray;
                                long oldMuteTime = 0;
                                long muteTime = 0;
                                final String banData = resultSet.getString("ban");
                                if (banData != null) {
                                    banArray = banData.split(":");
                                    oldBanTime = Long.parseLong(banArray[0]);
                                    banTime = Long.parseLong(banArray[0]);
                                    if (banTime != -1 && banTime > 0) banTime--;
                                    String q = "UPDATE users SET ban = '" + banData.replace(String.valueOf(oldBanTime), String.valueOf(banTime)) + "' WHERE player = ?;";
                                    try (PreparedStatement s = connection.prepareStatement(q)) {
                                        s.setString(1, name);
                                        s.executeUpdate();
                                    } catch (SQLException e) {
                                        LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
                                    }
                                }
                                final String muteData = resultSet.getString("mute");
                                if (muteData != null) {
                                    muteArray = muteData.split(":");
                                    oldMuteTime = Long.parseLong(muteArray[0]);
                                    muteTime = Long.parseLong(muteArray[0]);
                                    if (muteTime != -1 && muteTime > 0) muteTime--;
                                    String q = "UPDATE users SET mute = '" + muteData.replace(String.valueOf(oldMuteTime), String.valueOf(muteTime)) + "' WHERE player = ?;";
                                    try (PreparedStatement s = connection.prepareStatement(q)) {
                                        s.setString(1, name);
                                        s.executeUpdate();
                                    } catch (SQLException e) {
                                        LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
        }
    }

    public void updateHours() {
        final LinkedList<String> names = new LinkedList<>();
        Bukkit.getOnlinePlayers().forEach(p -> names.add(p.getName()));
        ensureConnection();
        String query = "SELECT player, hours FROM users;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet != null) {
                    while (resultSet.next()) {
                        final String name = resultSet.getString("player");
                        if (name != null) {
                            if (names.contains(name)) {
                                int hours = resultSet.getInt("hours");
                                hours++;
                                String q = "UPDATE users SET hours = '" + hours + "' WHERE player = ?;";
                                try (PreparedStatement s = connection.prepareStatement(q)) {
                                    s.setString(1, name);
                                    s.executeUpdate();
                                    hoursCache.put(name, hours/3600);
                                    cacheTimestamps.put(name, System.currentTimeMillis());
                                } catch (SQLException e) {
                                    LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
        }
    }

    public void temporaryBan(String playerName, String reason, String admin, long time) {
        ensureConnection();
        if (!containsPlayer(playerName).join()) addNewPlayer(playerName);
        String query = "UPDATE users SET ban = '" + time + ":" + reason + ":" + admin + "' WHERE player = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerName);
            statement.executeUpdate();
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
        }
    }

    public void temporaryMute(String playerName, String reason, String admin, long time) {
        ensureConnection();
        if (!containsPlayer(playerName).join()) addNewPlayer(playerName);
        String query = "UPDATE users SET mute = '" + time + ":" + reason + ":" + admin + "' WHERE player = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerName);
            statement.executeUpdate();
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
        }
    }

    public BanData getBanData(String playerName) {
        ensureConnection();
        String query = "SELECT ban FROM users WHERE player = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet != null & resultSet.next()) {
                    final String data = resultSet.getString("ban");
                    final String[] array = data.split(":");
                    final Long time = Long.parseLong(array[0]);
                    final String reason = (array[1]);
                    final String admin = (array[2]);
                    return new BanData(admin, time, reason);
                }
            }
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
            return null;
        }
        return null;
    }

    public void setKills(String playerName, double amount) {
        if (!LastTools.database.containsPlayer(playerName).join()) LastTools.database.addNewPlayer(playerName);
        LastTools.database.executeUpdate("UPDATE users SET kills = " + amount + " WHERE player = '" + playerName + "';");
        StatisticManager.killsCache.put(playerName, (int) amount);
    }

    public void setDeaths(String playerName, double amount) {
        if (!LastTools.database.containsPlayer(playerName).join()) LastTools.database.addNewPlayer(playerName);
        LastTools.database.executeUpdate("UPDATE users SET deaths = " + amount + " WHERE player = '" + playerName + "';");
        StatisticManager.deathsCache.put(playerName, (int) amount);
    }

    public BanData getMuteData(String playerName) {
        ensureConnection();
        String query = "SELECT mute FROM users WHERE player = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet != null & resultSet.next()) {
                    final String data = resultSet.getString("mute");
                    final String[] array = data.split(":");
                    final Long time = Long.parseLong(array[0]);
                    final String reason = (array[1]);
                    final String admin = (array[2]);
                    return new BanData(admin, time, reason);
                }
            }
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
            return null;
        }
        return null;
    }

    public boolean isBanned(String playerName) {
        ensureConnection();
        String query = "SELECT ban FROM users WHERE player = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet != null && resultSet.next()) {
                    final String data = resultSet.getString("ban");
                    final String[] array = data.split(":");
                    final Long time = Long.parseLong(array[0]);
                    if (time != 0) return true;
                }
            }
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
            return false;
        }
        return false;
    }

    public boolean isMuted(String playerName) {
        ensureConnection();
        String query = "SELECT mute FROM users WHERE player = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet != null && resultSet.next()) {
                    final String data = resultSet.getString("mute");
                    final String[] array = data.split(":");
                    final Long time = Long.parseLong(array[0]);
                    if (time != 0) return true;
                }
            }
        } catch (SQLException e) {
            LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
            return false;
        }
        return false;
    }

    public void executeUpdate(String query, Object... params) {
        ensureConnection();
        Bukkit.getScheduler().runTaskAsynchronously(LastTools.instance, () -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                setParameters(statement, params);
                statement.executeUpdate();
            } catch (SQLException e) {
                LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при выполнении обновления", e);
            }
        });
    }

    private void setParameters(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }

    public CompletableFuture<Boolean> containsPlayer(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            ensureConnection();
            String query = "SELECT 1 FROM users WHERE player = ?;";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            } catch (SQLException e) {
                LastTools.instance.getLogger().log(Level.SEVERE, "Ошибка при проверке существования игрока в базе данных", e);
                return false;
            }
        }, executor);
    }

    public void addNewPlayer(String playerName) {
        ensureConnection();
        if (!containsPlayer(playerName).join()) {
            executeUpdate("INSERT INTO users (player, coins, tokens, rank, ban, mute, hours, kills, deaths) VALUES (?, 0, 0, 'player', '0:-', '0:-', 0, 0, 0) ON CONFLICT (player) DO NOTHING;", playerName);
        }
    }
}
