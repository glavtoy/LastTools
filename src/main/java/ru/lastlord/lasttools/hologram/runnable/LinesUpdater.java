package ru.lastlord.lasttools.hologram.runnable;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.hologram.manager.HologramManager;

public class LinesUpdater {

    private static BukkitTask bukkitTask;

    public static void start() {
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(HologramManager::updateHolograms);
            }
        }.runTaskTimerAsynchronously(LastTools.instance, 0, 20);
    }
}
