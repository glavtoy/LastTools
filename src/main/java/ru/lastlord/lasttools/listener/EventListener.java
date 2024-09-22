package ru.lastlord.lasttools.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.ban.BanData;
import ru.lastlord.lasttools.configuration.Config;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.manager.StatisticManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;
import ru.lastlord.lasttools.manager.VanishManager;

import java.util.HashMap;
import java.util.UUID;

public class EventListener implements Listener {

    public EventListener() {
        LastTools.instance.getServer().getPluginManager().registerEvents(this, LastTools.instance);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        final Player player = e.getPlayer();
        final Rank rank = RankManager.getRank(player.getName()).join();
        if (rank != null && !rank.getTag().equalsIgnoreCase("admin")) {
            for (String cmd : Config.getConfig().getStringList("blocked-commands")) {
                if (e.getMessage().startsWith("/" + cmd)) {
                    ChatUtil.sendMessage(player, "not-found-command", null);
                    e.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (e.getReason().equals("Kicked for spamming")) e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null) LastTools.database.setKills(e.getEntity().getKiller().getName(), LastTools.database.getKills(e.getEntity().getKiller().getName()).join() + 1);
        LastTools.database.setDeaths(e.getEntity().getName(), LastTools.database.getDeaths(e.getEntity().getName()).join() + 1);
        e.setDeathMessage(null);
    }

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        final Player player = e.getPlayer();
        final String playerName = player.getName();
        if (LastTools.database.isMuted(playerName)) {
            final BanData banData = LastTools.database.getMuteData(playerName);
            final String admin = banData.getAdmin();
            final long time = banData.getTime();
            final String reason = banData.getReason();
            if (time == -1) {
                final HashMap<String, String> placeholders = new HashMap<>();
                placeholders.put("admin", admin);
                placeholders.put("reason", reason);
                ChatUtil.sendListMessage(player, "mute.layout", placeholders);
                e.setCancelled(true);
            } else if (time != 0 && time != -1) {
                player.sendMessage(Config.getTemporaryMuteLayout(time, admin, reason));
                e.setCancelled(true);
            }
        } else {
            e.getRecipients().clear();
            e.getRecipients().add(player);
            if (e.getMessage().startsWith("!")) {
                Bukkit.getOnlinePlayers().forEach(p -> e.getRecipients().add(p));
                e.setFormat(ChatUtil.colorize("&6[G] " + RankManager.getRank(player.getName()).join().getPrefix() + " " + player.getName() + "&f: " + e.getMessage().replace("!", "")));
            } else {
                player.getLocation().getNearbyPlayers(50).forEach(x -> {
                    if (x instanceof Player) e.getRecipients().add(x);
                });
                e.setFormat(ChatUtil.colorize("&b[L] " + RankManager.getRank(player.getName()).join().getPrefix() + " " + player.getName() + "&f: " + e.getMessage().replace("!", "")));
            }
        }
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        final String playerName = e.getName();
        if (LastTools.database.isBanned(playerName)) {
            final BanData banData = LastTools.database.getBanData(playerName);
            final String admin = banData.getAdmin();
            final long time = banData.getTime();
            final String reason = banData.getReason();
            if (time == -1) e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Config.getPermanentBanLayout(admin, reason));
            else if (time != 0 && time != -1) e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Config.getTemporaryBanLayout(time, admin, reason));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final String player = e.getPlayer().getName();
        if (!LastTools.database.containsPlayer(player).join()) LastTools.database.addNewPlayer(player);
        final Rank rank = RankManager.getRank(player).join();
        if (rank != null) {
            RankManager.setRank(player, rank);
            e.setJoinMessage(ChatUtil.colorize(rank.getPrefix() + " " + player + " &fзашел на сервер"));
        }
        for (UUID uuid : VanishManager.vanished) {
            final Player p = Bukkit.getPlayer(uuid);
            if (p != null) Bukkit.getPlayer(player).hidePlayer(LastTools.instance, p);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }
}
