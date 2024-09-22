package ru.lastlord.lasttools.home.manager;

import org.bukkit.entity.Player;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.home.configuration.Homes;
import ru.lastlord.lasttools.home.home.Home;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;

import java.io.File;
import java.util.LinkedList;

public class HomeManager {

    public static LinkedList<Homes> homes;

    public static Home getHomeByName(String playerName, String homeName) {
        for (Homes h : homes) if (h.getPlayerName().equalsIgnoreCase(playerName)) for (Home home : h.getHomes()) if (home.getName().equalsIgnoreCase(homeName)) return home;
        return null;
    }

    public static void createHome(Player player, String homeName) {
        if (getHomes(player.getName()) == 0) {
            final Homes h = new Homes();
            h.setPlayerName(player.getName());
            h.addNewbie();
            final Home home = new Home(player.getLocation(), homeName);
            h.getHomes().add(home);
            h.getHomesConfig().set("homes." + homeName, player.getLocation());
            h.save();
            homes.add(h);
        } else {
            final Homes h = getHomesByPlayerName(player.getName());
            if (h != null) {
                final Home home = new Home(player.getLocation(), homeName);
                h.getHomes().add(home);
                h.getHomesConfig().set("homes." + homeName, player.getLocation());
                h.save();
            }
        }
    }

    public static Homes getHomesByPlayerName(String playerName) {
        for (Homes h : homes) if (h.getPlayerName().equalsIgnoreCase(playerName)) return h;
        return null;
    }    public static int getHomes(String playerName) {
        for (Homes h : homes) if (h.getPlayerName().equalsIgnoreCase(playerName)) return h.getHomes().size();
        return 0;
    }

    public static boolean homeExists(String playerName, String homeName) {
        for (Homes h : homes) {
            if (h.getPlayerName().equalsIgnoreCase(playerName)) {
                for (Home home : h.getHomes()) {
                    if (home.getName().equalsIgnoreCase(homeName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getMaxHomes(String playerName) {
        final Rank rank = RankManager.getRank(playerName).join();
        int homes = 0;
        if (rank != null) {
            for (String perm : rank.getPermissions()) {
                if (perm.startsWith("lasttools.homes")) {
                    homes = Integer.parseInt(perm.replace("lasttools.homes.", ""));
                } else if (perm.equalsIgnoreCase("*")) {
                    return Integer.MAX_VALUE;
                }
            }
        }
        return homes;
    }

    public static void loadPlayersHomes() {
        homes = new LinkedList<>();
        final String directoryPath = LastTools.instance.getDataFolder() + "\\homes";
        final File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            final File[] files = directory.listFiles();
            for (File file : files) {
                final Homes h = new Homes();
                h.load(file);
                homes.add(h);
            }
        }
    }
}

