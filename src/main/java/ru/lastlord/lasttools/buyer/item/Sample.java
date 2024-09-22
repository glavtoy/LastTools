package ru.lastlord.lasttools.buyer.item;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.lastlord.lasttools.buyer.LastBuyer;
import ru.lastlord.lasttools.buyer.configuration.Items;
import ru.lastlord.lasttools.buyer.economy.impl.EconomyHandler;
import ru.lastlord.lasttools.buyer.manager.GuiManager;
import ru.lastlord.lasttools.buyer.runnable.BuyerUpdateRunnable;
import ru.lastlord.lasttools.buyer.util.ChatUtil;
import ru.lastlord.lasttools.buyer.util.ItemUtil;
import ru.lastlord.lasttools.buyer.util.RandomUtil;

import java.util.*;

@Setter
@Getter
@Builder
public class Sample {
    private String id;
    private String title;
    private int chance;
    private int size;
    private Set<Item> items;
    private LinkedList<SellItem> sellItems;
    private LinkedList<DecorationItem> decorations;
    private Inventory inventory;
    private EconomyHandler economyHandler;

    public void initItems() {
        sellItems = new LinkedList<>();
        decorations = new LinkedList<>();
        items.forEach(item -> {
            SellItem sellItem = new SellItem();
            ItemStack itemStack = null;

            if (LastBuyer.headDatabaseAPI != null && item.isHeadDatabase()) {
                final ItemStack head = LastBuyer.headDatabaseAPI.getItemHead(item.getItemId());
                if (head != null) itemStack = head;
            } else itemStack = new ItemStack(Material.valueOf(item.getItemId().toUpperCase()));

            ItemMeta itemMeta = itemStack.getItemMeta();

            if (item.isCustomName()) itemMeta.setDisplayName(ChatUtil.colorize(item.getName().replace("{time}", BuyerUpdateRunnable.getFormattedTime())));

            if (item.isRandomCost()) sellItem.setCost(RandomUtil.generateRandomNumber(item.getMinCost(), item.getMaxCost()));
            else sellItem.setCost(item.getFixedCost());

            if (item.isRandomAmount()) itemStack.setAmount(RandomUtil.generateRandomNumber(item.getMinAmount(), item.getMaxAmount()));
            else itemStack.setAmount(item.getFixedAmount());

            if (item.isLimit()) {
                sellItem.setLimit(true);
                sellItem.setLimitAmount(item.getLimitAmount());
            }

            if (item.isDecreaseCost()) {
                sellItem.setDecreaseCost(true);
                sellItem.setDecreasePercent(item.getDecreasePercent());
            }

            sellItem.setItem(item);

            final List<String> lore = new ArrayList<>();

            item.getLore().forEach(line->lore.add(ChatUtil.colorize(line
                    .replace("{cost}", String.valueOf(sellItem.getCost()))
                    .replace("{limit}", String.valueOf(sellItem.getLimitAmount()))
                    .replace("{time}", BuyerUpdateRunnable.getFormattedTime()))));

            sellItem.setSlot(item.getSlot());

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(sellItem.getSlot(), itemStack);
            sellItem.setItemStack(itemStack);
            sellItems.add(sellItem);
        });
        Items.getConfig().getConfigurationSection("samples."+id+".decorations").getKeys(false).forEach(decoration -> {

            final String path = "samples."+id+".decorations."+decoration+".";

            DecorationItem decorationItem = new DecorationItem();

            decorationItem.setHeadDatabase(Items.getConfig().getBoolean(path+"headdatabase"));
            decorationItem.setItemId(Items.getConfig().getString(path+"id"));
            decorationItem.setName(Items.getConfig().getString(path+"name"));
            decorationItem.setItemLore(Items.getConfig().getStringList(path+"lore"));
            decorationItem.setSlots(Items.getConfig().getIntegerList(path+"slots"));
            decorationItem.setData(Byte.parseByte(Items.getConfig().getString(path+"data")));

            ItemStack itemStack;


            if (LastBuyer.headDatabaseAPI != null && LastBuyer.headDatabaseAPI.getItemHead(path+"id") != null) itemStack = LastBuyer.headDatabaseAPI.getItemHead(path+"id");
            else {
                final Material material = Material.getMaterial(Items.getConfig().getString(path+"id"));
                itemStack = new ItemStack(material);
                final byte data = Byte.parseByte(Items.getConfig().getString(path+"data"));
                if (data == -1) itemStack = new ItemStack(material);
                else itemStack = new ItemStack(material, 1, data);
            }
            final ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(ChatUtil.colorize(Items.getConfig().getString(path+"name").replace("{time}", BuyerUpdateRunnable.getFormattedTime())));

            final List<String> lore = new ArrayList<>();

            Items.getConfig().getStringList(path+"lore").forEach(line->lore.add(ChatUtil.colorize(line.replace("{time}", BuyerUpdateRunnable.getFormattedTime()))));

            decorationItem.setItemStack(itemStack);

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            decorationItem.getSlots().forEach(slot->inventory.setItem(slot, decorationItem.getItemStack()));

            decorations.add(decorationItem);
        });
    }


    public void openGui(Player player) {
        GuiManager.currentGui.put(player.getUniqueId(), inventory);
        sellItems.forEach(sellItem->{
            if (!sellItem.isOut()) inventory.setItem(sellItem.getSlot(), sellItem.getItemStack());
            else inventory.setItem(sellItem.getSlot(), ItemUtil.getOutedItem());
        });
        player.openInventory(GuiManager.currentGui.get(player.getUniqueId()));
    }

    public void updateGuiOnAllPlayers() {
        for (Map.Entry<UUID, Inventory> entry : GuiManager.currentGui.entrySet()) {
            sellItems.forEach(sellItem -> {
                if (!sellItem.isOut()) entry.getValue().setItem(sellItem.getSlot(), sellItem.getItemStack());
                else entry.getValue().setItem(sellItem.getSlot(), ItemUtil.getOutedItem());
            });
            decorations.forEach(decorationItem->decorationItem.getSlots().forEach(slot->entry.getValue().setItem(slot, decorationItem.getItemStack())));
        }
    }

    public SellItem getSellItem(int slot) {
        for (SellItem sellItem : sellItems) if (sellItem.getSlot() == slot) return sellItem;
        return null;
    }
}
