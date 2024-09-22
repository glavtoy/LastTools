package ru.lastlord.lasttools.auction.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.lastlord.lasttools.auction.configuration.Config;
import ru.lastlord.lasttools.auction.page.Page;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class AuctionManager {

    public static LinkedList<Page> pages = new LinkedList<>();
    public static LinkedHashMap<Player, Integer> currentPage = new LinkedHashMap<>();

    public static Page getPageByNumber(int number) {
        for (Page page : pages) {
            if (page.getNumber() == number) return page;
        }
        return null;
    }

    public static void openGui(Player player, Page page) {
        if (page != null) {
            currentPage.put(player, page.getNumber());
            page.setInventory(Bukkit.createInventory(null, 54, "Аукцион : Страница " + page.getNumber() + "/" + ItemManager.pagesAmount));
            if (page.getItems().size() < 45) page.arrangeItems(page.getItems().size() - 1);
            else page.arrangeItems(44);
            ItemStack previousPageItem = new ItemStack(Material.valueOf(Config.getConfig().getString("previous-page-item.item")));
            ItemStack nextPageItem = new ItemStack(Material.valueOf(Config.getConfig().getString("next-page-item.item")));
            ItemStack updatePageItem = new ItemStack(Material.valueOf(Config.getConfig().getString("update-page-item.item")));
            ItemStack myItemsItem = new ItemStack(Material.valueOf(Config.getConfig().getString("my-items-item.item")));
            ItemStack categoriesItem = new ItemStack(Material.valueOf(Config.getConfig().getString("categories-item.item")));
            ItemMeta m1 = previousPageItem.getItemMeta();
            ItemMeta m2 = nextPageItem.getItemMeta();
            ItemMeta m3 = updatePageItem.getItemMeta();
            ItemMeta m4 = myItemsItem.getItemMeta();
            ItemMeta m5 = categoriesItem.getItemMeta();
            m1.setDisplayName(ChatUtil.colorize(Config.getConfig().getString("previous-page-item.name")));
            m2.setDisplayName(ChatUtil.colorize(Config.getConfig().getString("next-page-item.name")));
            m3.setDisplayName(ChatUtil.colorize(Config.getConfig().getString("update-page-item.name")));
            m4.setDisplayName(ChatUtil.colorize(Config.getConfig().getString("my-items-item.name")));
            m5.setDisplayName(ChatUtil.colorize(Config.getConfig().getString("categories-item.name")));
            previousPageItem.setItemMeta(m1);
            nextPageItem.setItemMeta(m2);
            updatePageItem.setItemMeta(m3);
            myItemsItem.setItemMeta(m4);
            categoriesItem.setItemMeta(m5);
            if (page.getNumber() > 1) page.getInventory().setItem(45, previousPageItem);
            if (page.getNumber() < ItemManager.pagesAmount) page.getInventory().setItem(53, nextPageItem);
            page.getInventory().setItem(47, myItemsItem);
            page.getInventory().setItem(49, updatePageItem);
            page.getInventory().setItem(51, categoriesItem);
            player.openInventory(page.getInventory());
            if (!GuiManager.inGui.contains(player)) GuiManager.inGui.add(player);
        }
    }
}
