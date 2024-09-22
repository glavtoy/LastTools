package ru.lastlord.lasttools.hologram.configuration;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.lastlord.lasttools.LastTools;

import java.io.File;
import java.io.IOException;

public class Holograms {

    private static File file;
    private static @Getter FileConfiguration config;

    public static void loadConfig() {
        file = new File(LastTools.instance.getDataFolder(), "holograms.yml");
        if (!file.exists()) LastTools.instance.saveResource("holograms.yml", true);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
