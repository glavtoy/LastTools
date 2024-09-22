package ru.lastlord.lasttools.antirelog.listener;

import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import ru.lastlord.lasttools.antirelog.configuration.Config;
import ru.lastlord.lasttools.antirelog.manager.AntiRelogManager;
import ru.lastlord.lasttools.antirelog.manager.CooldownManager;
import ru.lastlord.lasttools.auction.manager.AuctionManager;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

public class EventListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (AntiRelogManager.isCooldown(player)) {
            player.sendMessage(ChatUtil.colorize(Config.getConfig().getString("in.command")));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (AntiRelogManager.isCooldown(player)) {
            player.setHealth(0.0);
        }
    }

    @EventHandler
    public void onPlayerDamageAnotherPlayer(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player playerOne = (Player) e.getDamager();
            Rank rankOne = RankManager.getRank(playerOne.getName()).join();
            Player playerTwo = (Player) e.getEntity();
            Rank rankTwo = RankManager.getRank(playerOne.getName()).join();
            if (!rankOne.getTag().equalsIgnoreCase("admin") && !rankOne.getPermissions().contains("lasttools.antirelog.bypass") && !rankOne.getPermissions().contains("*") && !rankTwo.getTag().equalsIgnoreCase("admin") && !rankTwo.getPermissions().contains("lasttools.antirelog.bypass") && !rankTwo.getPermissions().contains("*")) AntiRelogManager.setCooldown(playerOne, playerTwo);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (AntiRelogManager.isCooldown(player)) {
            AntiRelogManager.removeCooldown(player);
        }
    }

    @EventHandler
    public void onPlayerUse(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (AntiRelogManager.isCooldown(player)) {
            String message = ChatUtil.colorize(Config.getConfig().getString("in.use-item"));
            ItemStack item = e.getItem();
            Material material = item.getType();
            if (!CooldownManager.canPlayerUseItem(player, material)) {
                e.setCancelled(true);
                player.sendMessage(message);
                return;
            }
            CooldownManager.setCooldown(player, material);
        }
    }

    @EventHandler
    public void onPlayerThrowEnderPearl(ProjectileLaunchEvent e) {
        ProjectileSource shooter = e.getEntity().getShooter();
        if (shooter instanceof Player) {
            Player player = (Player) shooter;
            if (AntiRelogManager.isCooldown(player) && e.getEntity() instanceof EnderPearl)  {
                String message = ChatUtil.colorize(Config.getConfig().getString("in.use-item"));
                Material type = Material.ENDER_PEARL;
                if (!CooldownManager.canPlayerUseItem(player, type)) {
                    e.setCancelled(true);
                    player.sendMessage(message);
                    return;
                }
                CooldownManager.setCooldown(player, type);
            }
        }
    }
}
