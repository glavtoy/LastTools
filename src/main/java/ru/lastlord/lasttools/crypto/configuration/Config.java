package ru.lastlord.lasttools.crypto.configuration;

import lombok.Getter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Config {

    private static File file;
    private static @Getter FileConfiguration config;

    public static void loadConfig(Plugin plugin) {
        file = new File(plugin.getDataFolder(), "crypto_config.yml");
        if (!file.exists()) plugin.saveResource("crypto_config.yml", true);
        config = YamlConfiguration.loadConfiguration(file);
    }
}
