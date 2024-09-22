package ru.lastlord.lasttools.buyer.configuration;

import lombok.Getter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import ru.lastlord.lasttools.LastTools;

import java.io.File;

public class Config {

    private static File file;
    private static @Getter FileConfiguration config;

    public static void loadConfig() {
        file = new File(LastTools.instance.getDataFolder(), "buyer_config.yml");
        if (!file.exists()) LastTools.instance.saveResource("buyer_config.yml", true);
        config = YamlConfiguration.loadConfiguration(file);
    }
}
