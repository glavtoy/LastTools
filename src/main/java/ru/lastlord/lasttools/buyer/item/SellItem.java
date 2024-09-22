package ru.lastlord.lasttools.buyer.item;

import lombok.Getter;

import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.lastlord.lasttools.buyer.LastBuyer;
import ru.lastlord.lasttools.buyer.runnable.BuyerUpdateRunnable;
import ru.lastlord.lasttools.buyer.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SellItem {
    private ItemStack itemStack;
    private boolean limit;
    private int limitAmount;
    private boolean decreaseCost;
    private int decreasePercent;
    private int cost;
    private int slot;
    private boolean out;
    private Item item;

    public void decreaseCost(int amount) {
        for (int i = amount; i > 0; i--) {
            final double percent = cost * (decreasePercent / 100.0);
            if (decreaseCost) cost -= percent;
            updateItemStack();
        }
    }

    public void updateItemStack() {
        int amount = itemStack.getAmount();

        if (LastBuyer.headDatabaseAPI != null && item.isHeadDatabase()) {
            final ItemStack head = LastBuyer.headDatabaseAPI.getItemHead(item.getItemId());
            if (head != null) itemStack = head;
        } else itemStack = new ItemStack(item.getMaterial());

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.setAmount(amount);
        if (item.isCustomName()) itemMeta.setDisplayName(ChatUtil.colorize(item.getName().replace("{time}", BuyerUpdateRunnable.getFormattedTime())));
        final List<String> lore = new ArrayList<>();
        item.getLore().forEach(line->lore.add(ChatUtil.colorize(line
                .replace("{cost}", String.valueOf(cost))
                .replace("{limit}", String.valueOf(limitAmount))
                .replace("{time}", BuyerUpdateRunnable.getFormattedTime()))));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }
}
