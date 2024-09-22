package ru.lastlord.lasttools.manager;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.configuration.Config;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.LinkedList;
import java.util.UUID;

public class VanishManager {

    public static LinkedList<UUID> vanished = new LinkedList<>();
    public static BossBar bossBar = Bukkit.createBossBar(ChatUtil.colorize(Config.getConfig().getString("locale.vanish.bossbar.message")), BarColor.valueOf(Config.getConfig().getString("locale.vanish.bossbar.color")), BarStyle.SOLID);

    public static void vanishPlayer(Player player) {
        vanished.add(player.getUniqueId());
        bossBar.addPlayer(player);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(LastTools.instance, player);
        }
    }

    public static void unVanishPlayer(Player player) {
        vanished.remove(player.getUniqueId());
        bossBar.removePlayer(player);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(LastTools.instance, player);
        }
    }
}
