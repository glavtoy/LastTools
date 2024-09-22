package ru.lastlord.lasttools.auction.page;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.lastlord.lasttools.auction.configuration.Config;
import ru.lastlord.lasttools.auction.item.Item;
import ru.lastlord.lasttools.auction.manager.AuctionManager;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Page {
    private Inventory inventory;
    private LinkedList<Item> items;
    private int number;

    public Page() {
        items = new LinkedList<>();
        if (AuctionManager.pages.isEmpty()) number = 1;
        else number = AuctionManager.pages.size() + 1;
    }

    public void arrangeItems(int size) {
        for (int i = 0; i <= size; i++) {
            ItemStack itemStack = items.get(i).getItemStack();
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            for (String line : Config.getConfig().getStringList("sell-item.lore")) {
                lore.add(ChatUtil.colorize(line.replace("{price}", String.valueOf(items.get(i).getPrice()))));
            }
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(i, itemStack);
        }
    }
}
