package ru.lastlord.lasttools.buyer.configuration;

import lombok.Getter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.lastlord.lasttools.LastTools;

import java.io.File;

public class Items {

    private static File file;
    private static @Getter FileConfiguration config;

    public static void loadConfig() {
        file = new File(LastTools.instance.getDataFolder(), "buyer_items.yml");
        if (!file.exists()) LastTools.instance.saveResource("buyer_items.yml", true);
        config = YamlConfiguration.loadConfiguration(file);
    }
}
