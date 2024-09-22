package ru.lastlord.lasttools.auction.configuration;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.auction.item.Item;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Items {
    private @Getter
    @Setter File playerItemsFile;
    private File folderFile;
    private @Getter @Setter FileConfiguration itemsConfig;
    private @Getter @Setter String playerName;
    private @Getter LinkedList<Item> items = new LinkedList<>();

    public void addNewbie() {
        try {
            folderFile = new File(LastTools.instance.getDataFolder(), "auction");
            playerItemsFile = new File(LastTools.instance.getDataFolder() + "\\auction", playerName + ".yml");
            if (!folderFile.exists()) folderFile.mkdirs();
            playerItemsFile.createNewFile();
            itemsConfig = YamlConfiguration.loadConfiguration(playerItemsFile);
            itemsConfig.createSection("items");
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            itemsConfig.save(playerItemsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(File file) {
        folderFile = new File(LastTools.instance.getDataFolder(), "auction");
        playerItemsFile = new File(LastTools.instance.getDataFolder() + "\\auction", file.getName());
        if (!folderFile.exists()) folderFile.mkdirs();
        itemsConfig = YamlConfiguration.loadConfiguration(playerItemsFile);
        playerName = file.getName().replace(".yml", "");
        for (String item : itemsConfig.getConfigurationSection("items").getKeys(false)) {
            final Item i = new Item(itemsConfig.getItemStack("items." + item + ".itemstack"), itemsConfig.getInt("items." + item + ".time"), itemsConfig.getInt("items." + item + ".price"), item, "test");
            items.add(i);
        }
    }
}
