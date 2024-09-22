package ru.lastlord.lasttools.runnable;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.lastlord.lasttools.LastTools;

public class BanCounter {

    private static BukkitTask bukkitTask;

    public static void start() {
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    Bukkit.getScheduler().runTaskAsynchronously(LastTools.instance, () -> LastTools.database.updateBanTimes());
                });
            }
        }.runTaskTimerAsynchronously(LastTools.instance, 0, 20);
    }
}
