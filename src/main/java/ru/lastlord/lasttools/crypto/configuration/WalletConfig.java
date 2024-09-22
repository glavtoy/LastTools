package ru.lastlord.lasttools.crypto.configuration;

import lombok.Getter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class WalletConfig {

    private static File file;
    private static @Getter FileConfiguration config;

    public static void loadConfig(Plugin plugin) {
        file = new File(plugin.getDataFolder(), "crypto_wallet.yml");
        if (!file.exists()) plugin.saveResource("crypto_wallet.yml", true);
        config = YamlConfiguration.loadConfiguration(file);
    }
}
