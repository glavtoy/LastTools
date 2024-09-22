package ru.lastlord.lasttools.crypto.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.crypto.manager.GuiManager;

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
            e.setCancelled(true);
        }
    }
}
