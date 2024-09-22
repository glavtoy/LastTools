package ru.lastlord.lasttools.manager;

import me.neznamy.tab.api.TabAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.configuration.Config;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RankManager {

    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static LinkedHashSet<Rank> ranks;

    public static final ConcurrentHashMap<String, Rank> rankCache = new ConcurrentHashMap<>();
    private static final long CACHE_EXPIRATION_TIME = 60000;
    private static final ConcurrentHashMap<String, Long> cacheTimestamps = new ConcurrentHashMap<>();

    public static Rank getRankByTag(String tag) {
        for (Rank rank : ranks) {
            if (rank.getTag().equalsIgnoreCase(tag)) {
                return rank;
            }
        }
        return null;
    }

    public static void loadRanks() {
        ranks = new LinkedHashSet<>();
        for (String tag : Config.getConfig().getConfigurationSection("ranks").getKeys(false)) {
            final String path = "ranks." + tag + ".";
            final Set<String> permissions = new HashSet<>();

            Config.getConfig().getStringList(path + "permissions").forEach(permissions::add);
            Config.getConfig().getStringList(path + "includes").forEach(includedRank ->
                    Config.getConfig().getStringList("ranks." + includedRank + ".permissions").forEach(permissions::add)
            );

            ranks.add(Rank.builder()
                    .tag(tag)
                    .isDefault(Config.getConfig().getBoolean(path + "default"))
                    .index(Config.getConfig().getInt(path + "index"))
                    .prefix(ChatUtil.colorize(Config.getConfig().getString(path + "prefix")))
                    .permissions(new ArrayList<>(permissions))
                    .build());
        }
    }

    public static String getInlineListedRanks() {
        final StringBuilder builder = new StringBuilder();
        ranks.forEach(rank -> {
            if (!rank.getTag().equalsIgnoreCase("admin")) {
                builder.append(rank.getTag()).append(", ");
            } else {
                builder.append(rank.getTag());
            }
        });

        if (builder.length() > 2 && builder.substring(builder.length() - 2).equals(", ")) {
            builder.setLength(builder.length() - 2);
        }

        return builder.toString();
    }

    public static CompletableFuture<Rank> getRank(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            if (rankCache.containsKey(playerName)) {
                long cachedTime = cacheTimestamps.getOrDefault(playerName, 0L);
                //if (System.currentTimeMillis() - cachedTime < CACHE_EXPIRATION_TIME) {
                    return rankCache.get(playerName);
                //}
            }
            if (!LastTools.database.containsPlayer(playerName).join()) LastTools.database.addNewPlayer(playerName);
            ResultSet resultSet = null;
            try {
                resultSet = LastTools.database.executeQuery("SELECT * FROM users WHERE player = ?;", playerName).join();
                if (resultSet != null && resultSet.next()) {
                    Rank rank = getRankByTag(resultSet.getString("rank"));
                    rankCache.put(playerName, rank);
                    cacheTimestamps.put(playerName, System.currentTimeMillis());
                    return rank;
                }
            } catch (Exception e) {
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
            return null;
        }, executor);
    }

    public static void setRank(String playerName, Rank rank) {
        Bukkit.getScheduler().runTaskAsynchronously(LastTools.instance, () -> {
            final Player player = Bukkit.getPlayer(playerName);
            final Rank playerRank = getRank(playerName).join();
            LastTools.database.executeUpdate("UPDATE users SET rank = ? WHERE player = ?;", rank.getTag(), playerName);
            rankCache.put(playerName, rank);
            cacheTimestamps.put(playerName, System.currentTimeMillis());
            if (player != null && playerRank != null && TabAPI.getInstance().getNameTagManager() != null) {
                PermissionAttachment attachment = player.addAttachment(LastTools.instance);
                attachment.getPermissions().clear();
                playerRank.getPermissions().forEach(permission -> {
                    attachment.unsetPermission(permission);
                });
                player.getEffectivePermissions().forEach(permission -> {
                    if (!rank.getTag().equalsIgnoreCase("admin")) {
                        attachment.setPermission(permission.getPermission(), false);
                    } else {
                        attachment.setPermission(permission.getPermission(), true);
                    }
                });
                rank.getPermissions().forEach(permission -> attachment.setPermission(permission, true));
                player.recalculatePermissions();
                player.setOp(rank.getPermissions().contains("*"));
                Bukkit.getScheduler().runTask(LastTools.instance, () -> {
                    if (TabAPI.getInstance().getPlayer(playerName).isLoaded()) {
                        TabAPI.getInstance().getNameTagManager().setPrefix(TabAPI.getInstance().getPlayer(playerName), rank.getPrefix() + " ");
                        TabAPI.getInstance().getTabListFormatManager().setName(TabAPI.getInstance().getPlayer(playerName), rank.getPrefix() + " " + playerName);
                    }
                    player.setPlayerListName(rank.getPrefix() + " " + player.getName());
                });
            }
        });
    }
}
