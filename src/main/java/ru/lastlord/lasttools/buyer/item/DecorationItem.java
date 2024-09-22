package ru.lastlord.lasttools.buyer.item;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.lastlord.lasttools.buyer.LastBuyer;
import ru.lastlord.lasttools.buyer.runnable.BuyerUpdateRunnable;
import ru.lastlord.lasttools.buyer.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DecorationItem {

    private ItemStack itemStack;
    private List<Integer> slots;
    private String itemId;
    private String name;
    private List<String> itemLore;
    private byte data;
    private boolean headDatabase;

    public void updateItemStack() {
        if (LastBuyer.headDatabaseAPI != null && headDatabase) {
            final ItemStack head = LastBuyer.headDatabaseAPI.getItemHead(itemId);
            if (head != null) itemStack = head;
        } else {
            if (data == -1) itemStack = new ItemStack(Material.getMaterial(itemId));
            else itemStack = new ItemStack(Material.getMaterial(itemId), 1, data);
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatUtil.colorize(name.replace("{time}", BuyerUpdateRunnable.getFormattedTime())));
        final List<String> lore = new ArrayList<>();
        itemLore.forEach(line->lore.add(ChatUtil.colorize(line.replace("{time}", BuyerUpdateRunnable.getFormattedTime()))));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }
}
