package ru.lastlord.lasttools.teleport.data;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.teleport.manager.TeleportManager;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.HashMap;

@Getter
public class TeleportData {
    private Player from;
    private Player to;
    private int time;
    private BukkitTask bukkitTask;

    public TeleportData(Player from, Player to, int time) {
        this.from = from;
        this.to = to;
        this.time = time;
        TeleportManager.queries.add(this);
        runWaiting();
    }

    public Location accept() {
        bukkitTask.cancel();
        TeleportManager.queries.remove(this);
        return to.getLocation();
    }

    public void deny() {
        TeleportManager.queries.remove(this);
        bukkitTask.cancel();
    }

    public void runWaiting() {
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (time > 0) time--;
                else {
                    if (from != null) {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", to.getName());
                        ChatUtil.sendMessage(from, "teleportask.timeout", placeholders);
                    }
                    TeleportManager.queries.remove(this);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(LastTools.instance, 0, 20);
    }
}
