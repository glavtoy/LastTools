package ru.lastlord.lasttools.auction.manager;

import org.bukkit.entity.Player;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.auction.configuration.Config;
import ru.lastlord.lasttools.auction.configuration.Items;
import ru.lastlord.lasttools.auction.item.Item;
import ru.lastlord.lasttools.auction.listener.EventListener;
import ru.lastlord.lasttools.auction.page.Page;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;

import java.io.File;
import java.util.LinkedList;

public class ItemManager {

    public static LinkedList<Items> items;

    public static int pagesAmount = 0;

    public static Item getItemById(String playerName, String id) {
        for (Items items1 : items) if (items1.getPlayerName().equalsIgnoreCase(playerName)) for (Item item : items1.getItems()) if (item.getId().equalsIgnoreCase(id)) return item;
        return null;
    }

    public static void addItem(Player player, Item item2) {
        if (getItemsByName(player.getName()) == 0) {
            final Items items1 = new Items();
            items1.setPlayerName(player.getName());
            items1.addNewbie();
            final Item item = new Item(item2.getItemStack(), item2.getTime(), item2.getPrice(), item2.getId(), item2.getEconomy());
            items1.getItems().add(item);
            items1.getItemsConfig().set("items." + item2.getId() + ".itemstack", item2.getItemStack());
            items1.getItemsConfig().set("items." + item2.getId() + ".time", item2.getTime());
            items1.getItemsConfig().set("items." + item2.getId() + ".price", item2.getPrice());
            items1.getItemsConfig().set("items." + item2.getId() + ".economy", item2.getEconomy());
            items1.save();
            items.add(items1);
        } else {
            final Items i = getItemsByPlayerName(player.getName());
            if (i != null) {
                final Item item = new Item(item2.getItemStack(), item2.getTime(), item2.getPrice(), item2.getId(), item2.getEconomy());
                i.getItems().add(item);
                i.getItemsConfig().set("items." + item2.getId() + ".itemstack", item2.getItemStack());
                i.getItemsConfig().set("items." + item2.getId() + ".time", item2.getTime());
                i.getItemsConfig().set("items." + item2.getId() + ".price", item2.getPrice());
                i.getItemsConfig().set("items." + item2.getId() + ".economy", item2.getEconomy());
                i.save();
                items.add(i);
            }
        }
    }

    public static Items getItemsByPlayerName(String playerName) {
        for (Items items1 : items) if (items1.getPlayerName().equalsIgnoreCase(playerName)) return items1;
        return null;
    }

    public static int getItemsByName(String playerName) {
        for (Items items1 : items) if (items1.getPlayerName().equalsIgnoreCase(playerName)) return items1.getItems().size();
        return 0;
    }

    public static int getMaxSlots(String playerName) {
        final Rank rank = RankManager.getRank(playerName).join();
        int slots = 0;
        if (rank != null) {
            for (String perm : rank.getPermissions()) {
                if (perm.startsWith("lasttools.auction")) {
                    slots = Integer.parseInt(perm.replace("lasttools.auction.", ""));
                } else if (perm.equalsIgnoreCase("*")) {
                    return Integer.MAX_VALUE;
                }
            }
        }
        return slots;
    }

    public static void loadPlayersItems() {
        Config.loadConfig();
        LastTools.instance.getServer().getPluginManager().registerEvents(new EventListener(), LastTools.instance);
        items = new LinkedList<>();
        final String directoryPath = LastTools.instance.getDataFolder() + "\\auction";
        final File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            final File[] files = directory.listFiles();
            for (File file : files) {
                final Items i = new Items();
                i.load(file);
                items.add(i);
            }
        }
        LinkedList<Item> items2 = new LinkedList<>();
        items.forEach(x -> x.getItems().forEach(y -> {
            items2.add(y);
        }));
        while (!items2.isEmpty()) {
            LinkedList<Item> chunk = new LinkedList<>();
            for (int i = 0; i < 45 && !items2.isEmpty(); i++) {
                chunk.add(items2.removeFirst());
            }
            if (chunk.size() < 45) {
                final Page lastPage = new Page();
                lastPage.setItems(chunk);
                AuctionManager.pages.add(lastPage);
                pagesAmount++;
            } else {
                final Page page = new Page();
                page.setItems(chunk);
                AuctionManager.pages.add(page);
                pagesAmount++;
            }
        }
    }
}
