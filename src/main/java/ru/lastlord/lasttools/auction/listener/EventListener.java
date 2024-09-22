package ru.lastlord.lasttools.auction.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import ru.lastlord.lasttools.auction.manager.AuctionManager;
import ru.lastlord.lasttools.auction.manager.GuiManager;

public class EventListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (GuiManager.inGui.contains(player)) {
            if (AuctionManager.currentPage.get(player) != null) {
                if (e.getSlot() == 45 && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    AuctionManager.openGui(player, AuctionManager.getPageByNumber(AuctionManager.currentPage.get(player) - 1));
                } else if (e.getSlot() == 53 && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                    AuctionManager.openGui(player, AuctionManager.getPageByNumber(AuctionManager.currentPage.get(player) + 1));
                }
            }
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (GuiManager.inGui.contains(player)) GuiManager.inGui.remove(player);
    }
}
