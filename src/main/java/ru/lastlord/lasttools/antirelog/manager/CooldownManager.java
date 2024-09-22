package ru.lastlord.lasttools.antirelog.manager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.lastlord.lasttools.antirelog.configuration.Config;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {
    private static Map<Player, Map<Material, Long>> cooldowns = new HashMap<>();
    private static Map<Material, Long> secondsForMateral = new HashMap<>();

    public static void setSecondsForMaterials() {
        final String section = "cooldowns.";
        secondsForMateral.put(Material.GOLDEN_APPLE, Config.getConfig().getInt(section + "golden-apple")*1000L);
        secondsForMateral.put(Material.CHORUS_FRUIT, Config.getConfig().getInt(section + "chorus")*1000L);
        secondsForMateral.put(Material.ENCHANTED_GOLDEN_APPLE, Config.getConfig().getInt(section + "enchanted-golden-apple")*1000L);
        secondsForMateral.put(Material.ENDER_PEARL, Config.getConfig().getInt(section + "ender-pearl")*1000L);
    }

    public static void setCooldown(Player player, Material type) {
        final long duration = secondsForMateral.get(type);
        if (duration > 0) {
            final Map<Material, Long> playerCooldowns = cooldowns.computeIfAbsent(player, k -> new HashMap<>());
            final long time = System.currentTimeMillis() + duration;
            playerCooldowns.put(type, time);
        }
    }

    public static boolean canPlayerUseItem(Player player, Material type) {
        final Map<Material, Long> playerCooldowns = cooldowns.get(player);
        if (playerCooldowns == null) return true;
        final Long time = playerCooldowns.get(type);
        if (time == null) return true;
        return System.currentTimeMillis() >= time;
    }
}