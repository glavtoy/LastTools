package ru.lastlord.lasttools.buyer.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.buyer.configuration.Config;
import ru.lastlord.lasttools.buyer.configuration.Items;
import ru.lastlord.lasttools.buyer.item.Sample;
import ru.lastlord.lasttools.buyer.item.SellItem;
import ru.lastlord.lasttools.buyer.manager.GuiManager;
import ru.lastlord.lasttools.buyer.manager.SampleManager;
import ru.lastlord.lasttools.buyer.util.ChatUtil;
import ru.lastlord.lasttools.buyer.util.ItemUtil;
import ru.lastlord.lasttools.buyer.util.SoundUtil;

import java.util.HashMap;

public class MainGuiListener implements Listener {

    public MainGuiListener() {
        LastTools.instance.getServer().getPluginManager().registerEvents(this, LastTools.instance);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        final Player player = (Player) e.getPlayer();
        if (GuiManager.currentGui.containsKey(player.getUniqueId())) GuiManager.currentGui.remove(player.getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        final Player player = (Player) e.getWhoClicked();
        final Inventory inventory = e.getInventory();
        if (GuiManager.currentGui.get(player.getUniqueId()) == inventory) {
            final ItemStack item = e.getCurrentItem();
            if (item != null) {
                final ItemMeta itemMeta = item.getItemMeta();
                final Sample sample = SampleManager.getSample(inventory);
                if (itemMeta != null && sample != null) {
                    final SellItem sellItem = sample.getSellItem(e.getSlot());
                    final ClickType singleSellClick = ClickType.valueOf(Config.getConfig().getString("single-sell"));
                    final ClickType allSellClick = ClickType.valueOf(Config.getConfig().getString("all-sell"));
                    if (singleSellClick != null && allSellClick != null && sellItem != null && sellItem.getItemStack().getType() == item.getType()) {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("{item}", itemMeta.getDisplayName().isEmpty() ? String.valueOf(item.getType()) : itemMeta.getDisplayName());
                        placeholders.put("{currency}", Config.getConfig().getString("currency."+ Items.getConfig().getString("samples."+sample.getId()+".economy").toLowerCase()));
                        final int itemCount = ItemUtil.getItemCount(player, sellItem.getItemStack().getType());
                        if (e.getClick() == singleSellClick) {
                            if (sellItem.isLimit()) {
                                if (itemCount >= item.getAmount()) {
                                    if (sellItem.getLimitAmount() == 1) {
                                        inventory.setItem(e.getSlot(), ItemUtil.getOutedItem());
                                        sellItem.setLimitAmount(0);
                                        sellItem.setOut(true);
                                    } else sellItem.setLimitAmount(sellItem.getLimitAmount() - 1);
                                    placeholders.put("{amount}", String.valueOf(item.getAmount()));
                                    placeholders.put("{money}", String.valueOf(sellItem.getCost()));
                                    ItemUtil.takeItems(player, item.getType(), item.getAmount());
                                    sample.getEconomyHandler().deposit(player, sellItem.getCost());
                                    ChatUtil.sendMessage(player, "sell", placeholders);
                                    SoundUtil.playSound(player, "sell");
                                    sellItem.decreaseCost(1);
                                    if (sellItem.getCost() == 0) sellItem.setOut(true);
                                    sample.updateGuiOnAllPlayers();
                                } else {
                                    ChatUtil.sendMessage(player, "no-item", placeholders);
                                    SoundUtil.playSound(player, "no-item");
                                }
                            } else {
                                if (itemCount >= item.getAmount()) {
                                    placeholders.put("{amount}", String.valueOf(item.getAmount()));
                                    placeholders.put("{money}", String.valueOf(sellItem.getCost()));
                                    ItemUtil.takeItems(player, item.getType(), item.getAmount());
                                    sample.getEconomyHandler().deposit(player, sellItem.getCost());
                                    ChatUtil.sendMessage(player, "sell", placeholders);
                                    SoundUtil.playSound(player, "sell");
                                    sellItem.decreaseCost(1);
                                    if (sellItem.getCost() == 0) sellItem.setOut(true);
                                    sample.updateGuiOnAllPlayers();
                                } else {
                                    ChatUtil.sendMessage(player, "no-item", placeholders);
                                    SoundUtil.playSound(player, "no-item");
                                }
                            }
                        } else if (e.getClick() == allSellClick) {
                            final int amount = itemCount/item.getAmount();
                            final int limitAmount = sellItem.getLimitAmount();
                            placeholders.put("{amount}", String.valueOf(item.getAmount()*amount));
                            placeholders.put("{money}", String.valueOf(sellItem.getCost()*amount));
                            if (sellItem.isLimit()) {
                                if (amount >= limitAmount) {
                                    if (itemCount >= item.getAmount()) {
                                        if (sellItem.getLimitAmount() == 1) {
                                            sellItem.setLimitAmount(0);
                                            sellItem.setOut(true);
                                        } else sellItem.setLimitAmount(sellItem.getLimitAmount() - limitAmount);
                                        placeholders.put("{amount}", String.valueOf(item.getAmount() * limitAmount));
                                        placeholders.put("{money}", String.valueOf(sellItem.getCost() * limitAmount));
                                        ItemUtil.takeItems(player, item.getType(), item.getAmount() * limitAmount);
                                        sample.getEconomyHandler().deposit(player, sellItem.getCost() * limitAmount);
                                        ChatUtil.sendMessage(player, "sell", placeholders);
                                        SoundUtil.playSound(player, "sell");
                                        sellItem.decreaseCost(limitAmount);
                                        sellItem.setLimitAmount(0);
                                        sellItem.setOut(true);
                                        sample.updateGuiOnAllPlayers();
                                    } else {
                                        ChatUtil.sendMessage(player, "no-item", placeholders);
                                        SoundUtil.playSound(player, "no-item");
                                    }
                                } else {
                                    if (itemCount >= item.getAmount()) {
                                        if (sellItem.getLimitAmount() == 1) {
                                            inventory.setItem(e.getSlot(), ItemUtil.getOutedItem());
                                            sellItem.setLimitAmount(0);
                                            sellItem.setOut(true);
                                        } else sellItem.setLimitAmount(sellItem.getLimitAmount() - amount);
                                        placeholders.put("{amount}", String.valueOf(item.getAmount()*amount));
                                        placeholders.put("{money}", String.valueOf(sellItem.getCost() * amount));
                                        ItemUtil.takeItems(player, item.getType(), item.getAmount() * amount);
                                        sample.getEconomyHandler().deposit(player, sellItem.getCost() * amount);
                                        ChatUtil.sendMessage(player, "sell", placeholders);
                                        SoundUtil.playSound(player, "sell");
                                        sellItem.decreaseCost(amount);
                                        if (sellItem.getCost() == 0) sellItem.setOut(true);
                                        sample.updateGuiOnAllPlayers();
                                    } else {
                                        ChatUtil.sendMessage(player, "no-item", placeholders);
                                        SoundUtil.playSound(player, "no-item");
                                    }
                                }
                            } else {
                                if (itemCount >= item.getAmount()) {
                                    placeholders.put("{amount}", String.valueOf(item.getAmount()*amount));
                                    placeholders.put("{money}", String.valueOf(sellItem.getCost()*amount));
                                    ItemUtil.takeItems(player, item.getType(), item.getAmount()*amount);
                                    sample.getEconomyHandler().deposit(player, sellItem.getCost()*amount);
                                    ChatUtil.sendMessage(player, "sell", placeholders);
                                    SoundUtil.playSound(player, "sell");
                                    sellItem.decreaseCost(amount);
                                    if (sellItem.getCost() == 0) sellItem.setOut(true);
                                    sample.updateGuiOnAllPlayers();
                                } else {
                                    ChatUtil.sendMessage(player, "no-item", placeholders);
                                    SoundUtil.playSound(player, "no-item");
                                }
                            }
                        }
                    }
                }
                e.setCancelled(true);
            }
        }
    }
}
