package ru.lastlord.lasttools.chatguard.listener;

import org.bukkit.event.EventHandler;
import ru.lastlord.lasttools.LastTools;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import ru.lastlord.lasttools.chatguard.configuration.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EventListener implements Listener {

    private Map<UUID, Integer> messagesPerInterval = new HashMap<>();

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();
        final String message = e.message().toString();
        final int maxMessages = Config.getConfig().getInt("max-messages-for-mute");
        final int interval = Config.getConfig().getInt("interval");
        final int messages = messagesPerInterval.getOrDefault(uuid, 0);
        final List<String> banWords = Config.getConfig().getStringList("ban-words");
        banWords.forEach(word -> {
            if (message.contains(word) && !player.hasPermission("lasttools.chatguard.bypass") && !player.hasPermission("&")) {
                e.setCancelled(true);
            }
        });
        if (messages >= maxMessages && !player.hasPermission("lasttools.chatguard.bypass") && !player.hasPermission("&")) {
            System.out.println("SPAM");
            e.setCancelled(true);
            // TODO: Мутить
            return;
        }
        messagesPerInterval.put(uuid, messages + 1);
        Bukkit.getScheduler().runTaskLater(LastTools.instance, () -> {
            messagesPerInterval.put(uuid, 0);
        }, interval * 20L);
    }
}
