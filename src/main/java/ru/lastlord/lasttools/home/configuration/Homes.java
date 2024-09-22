package ru.lastlord.lasttools.home.configuration;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.home.home.Home;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Homes {

    private @Getter @Setter File playerHomesFile;
    private File folderFile;
    private @Getter @Setter FileConfiguration homesConfig;
    private @Getter @Setter String playerName;
    private @Getter LinkedList<Home> homes = new LinkedList<>();

    public void addNewbie() {
        try {
            folderFile = new File(LastTools.instance.getDataFolder(), "homes");
            playerHomesFile = new File(LastTools.instance.getDataFolder() + "\\homes", playerName + ".yml");
            if (!folderFile.exists()) folderFile.mkdirs();
            playerHomesFile.createNewFile();
            homesConfig = YamlConfiguration.loadConfiguration(playerHomesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            homesConfig.save(playerHomesFile);
        } catch (IOException e) {
            e.printStackTrace();;
        }
    }

    public void load(File file) {
        folderFile = new File(LastTools.instance.getDataFolder(), "homes");
        playerHomesFile = new File(LastTools.instance.getDataFolder() + "\\homes", file.getName());
        if (!folderFile.exists()) folderFile.mkdirs();
        homesConfig = YamlConfiguration.loadConfiguration(playerHomesFile);
        playerName = file.getName().replace(".yml", "");
        for (String home : homesConfig.getConfigurationSection("homes").getKeys(false)) {
            final Home h = new Home(homesConfig.getLocation("homes." + home), home);
            homes.add(h);
        }
    }
}
