package ru.lastlord.lasttools.crypto.util;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.lastlord.lasttools.crypto.configuration.Config;

public class SoundUtil {

    public static void playSound(Player player, String action) {
        final FileConfiguration config = Config.getConfig();
        final String path = "sounds.";
        if (action != null && !action.equalsIgnoreCase("-")) {
            final Sound sound = Sound.valueOf(Config.getConfig().getString(path+action).toUpperCase());
            if (sound != null) player.playSound(player.getLocation(), sound, 1, 0);
        }
    }
}
