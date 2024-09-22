package ru.lastlord.lasttools.buyer.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.buyer.configuration.Config;
import ru.lastlord.lasttools.buyer.util.SoundUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager {

    public final static HashMap<UUID, Inventory> currentGui = new HashMap<>();

    public static void openCurrentSampleGui(Player player) {
        SampleManager.currentSample.openGui(player);
        SoundUtil.playSound(player, "open-gui");
    }

    public static void updateCurrentSampleGui() {
        if (Config.getConfig().getBoolean("auto-close-gui")) {
            for (Map.Entry<UUID, Inventory> entry : currentGui.entrySet()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.getPlayer(entry.getKey()).closeInventory();
                    }
                }.runTask(LastTools.instance);
            }
        }
    }
}
