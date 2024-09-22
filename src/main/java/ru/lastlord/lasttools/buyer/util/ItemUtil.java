package ru.lastlord.lasttools.buyer.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.lastlord.lasttools.buyer.LastBuyer;
import ru.lastlord.lasttools.buyer.configuration.Config;
import ru.lastlord.lasttools.buyer.runnable.BuyerUpdateRunnable;

import java.util.*;

public class ItemUtil {

    public static int getItemCount(Player player, Material material) {
        int count = 0;
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
            }
        }
        return count;
    }

    public static void takeItems(Player player, Material material, int amount) {
        int count = 0;
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item != null && item.getType() == material) {
                int take = Math.min(item.getAmount(), amount - count);
                item.setAmount(item.getAmount() - take);
                count += take;
                if (count == amount) break;
            }
        }
    }

    public static ItemStack getOutedItem() {
        final String path = "item-out.";
        final Material material = Material.getMaterial(Config.getConfig().getString(path+"id"));
        ItemStack itemStack;
        if (material != null) {
            if (LastBuyer.headDatabaseAPI != null) {
                final ItemStack head = LastBuyer.headDatabaseAPI.getItemHead(Config.getConfig().getString(path + "id"));
                if (head != null) itemStack = head;
            } else {
                itemStack = new ItemStack(material);
            }
            itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatUtil.colorize(Config.getConfig().getString(path + "name").replace("{time}", BuyerUpdateRunnable.getFormattedTime())));
            final List<String> lore = new ArrayList<>();
            Config.getConfig().getStringList(path + "lore").forEach(line -> lore.add(ChatUtil.colorize(line
                    .replace("{time}", BuyerUpdateRunnable.getFormattedTime()))));
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        return null;
    }
}
