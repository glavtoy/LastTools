package ru.lastlord.lasttools.teleport.manager;

import org.bukkit.entity.Player;
import ru.lastlord.lasttools.teleport.data.TeleportData;

import java.util.LinkedList;

public class TeleportManager {

    public static LinkedList<TeleportData> queries = new LinkedList<>();

    public static TeleportData getTeleportData(Player from, Player to) {
        for (TeleportData teleportData : queries) {
            if (teleportData.getFrom() == from && teleportData.getTo() == to) return teleportData;
        }
        return null;
    }

    public static boolean exists(Player from, Player to) {
        for (TeleportData teleportData : queries) {
            if (teleportData.getFrom() == from && teleportData.getTo() == to) return true;
        }
        return false;
    }
}
