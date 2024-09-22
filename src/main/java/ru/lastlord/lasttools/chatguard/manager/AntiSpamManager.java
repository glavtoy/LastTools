package ru.lastlord.lasttools.chatguard.manager;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AntiSpamManager {

    private static Map<UUID, Long> intervalForResetMap = new HashMap<>();
    private static Map<UUID, Integer> messagesMap = new HashMap<>();

    public static void resetMessagesCount(Player player) {
        UUID uuid = player.getUniqueId();
        messagesMap.compute(uuid, (k, v) -> 0);
    }

    public static int getMessages(Player player) {
        UUID uuid = player.getUniqueId();
        return messagesMap.getOrDefault(uuid, 0);
    }
}
