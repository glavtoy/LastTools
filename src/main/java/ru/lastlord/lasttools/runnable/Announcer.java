package ru.lastlord.lasttools.runnable;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.configuration.Config;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.LinkedList;

public class Announcer {

    private static BukkitTask bukkitTask;
    private static int messageIndex = 1;
    private static LinkedList<String> messages;

    public static void start() {
        messages = new LinkedList<>();
        Config.getConfig().getConfigurationSection("announcements.messages").getKeys(false).forEach(message -> {
            final StringBuilder stringBuilder = new StringBuilder();
            Config.getConfig().getStringList("announcements.messages." + message).forEach(msg -> stringBuilder.append(ChatUtil.colorize(msg) + "\n"));
            messages.add(stringBuilder.toString());
        });
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (!messages.isEmpty()) {
                        if (messageIndex == messages.size()) {
                            messageIndex = 0;
                            p.sendMessage(messages.get(messageIndex));
                        } else {
                            p.sendMessage(messages.get(messageIndex));
                            messageIndex++;
                        }
                    }
                });
            }
        }.runTaskTimer(LastTools.instance, 0, 20L * Config.getConfig().getInt("announcements.interval"));
    }
}
