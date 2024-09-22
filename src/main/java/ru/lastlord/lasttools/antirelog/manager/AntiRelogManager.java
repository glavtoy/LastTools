package ru.lastlord.lasttools.antirelog.manager;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.antirelog.configuration.Config;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AntiRelogManager {

    private static final String IN_SECTION = "in.";
    private static final String OUT_SECTION = "out.";

    private static final Map<UUID, BossBar> bossBarMap = new HashMap<>();
    private static final Map<UUID, BukkitRunnable> taskMap = new HashMap<>();

    public static boolean isCooldown(Player player) {
        return bossBarMap.containsKey(player.getUniqueId());
    }

    public static void setCooldown(Player playerOne, Player playerTwo) {
        int time = Config.getConfig().getInt(IN_SECTION + "time");
        String bossbarTitleIn = ChatUtil.colorize(Config.getConfig().getString(IN_SECTION + "bossbar"));
        String[] settings = ChatUtil.colorize(Config.getConfig().getString(IN_SECTION + "bossbar-settings")).split(":");
        String messageIn = ChatUtil.colorize(Config.getConfig().getString(IN_SECTION + "message"));
        String titleIn = ChatUtil.colorize(Config.getConfig().getString(IN_SECTION + "title"));
        String subTitleIn = ChatUtil.colorize(Config.getConfig().getString(IN_SECTION + "sub-title"));

        for (Player player : Arrays.asList(playerOne, playerTwo)) {
            if (isCooldown(player)) {
                resetCooldown(player, time, bossbarTitleIn);
                continue;
            }

            BossBar bossBarIn = Bukkit.createBossBar(bossbarTitleIn.replace("{sec}", String.valueOf(time)), BarColor.valueOf(settings[0]), BarStyle.valueOf(settings[1]));
            bossBarIn.setProgress(1.0);
            bossBarIn.addPlayer(player);
            bossBarMap.put(player.getUniqueId(), bossBarIn);
            startCooldownTask(player, time, bossbarTitleIn, bossBarIn);

            player.setFlying(false);
            player.setAllowFlight(false);
            sendTitleAndActionBar(player, messageIn, titleIn, subTitleIn);
        }
    }

    private static void resetCooldown(Player player, int time, String bossbarTitleIn) {
        BossBar currentBossBar = bossBarMap.get(player.getUniqueId());
        currentBossBar.setProgress(1.0);
        currentBossBar.setTitle(bossbarTitleIn.replace("{sec}", String.valueOf(time)));
        BukkitRunnable task = taskMap.get(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
        startCooldownTask(player, time, bossbarTitleIn, currentBossBar);
    }

    private static void startCooldownTask(Player player, int time, String bossbarTitleIn, BossBar bossBarIn) {
        BukkitRunnable task = new BukkitRunnable() {
            int remainingTime = time;
            @Override
            public void run() {
                if (remainingTime != 0) {
                    remainingTime--;
                    bossBarIn.setProgress((double) remainingTime / time);
                    bossBarIn.setTitle(bossbarTitleIn.replace("{sec}", String.valueOf(remainingTime)));
                    String actionBar = ChatUtil.colorize(Config.getConfig().getString(IN_SECTION + "action-bar"))
                            .replace("{sec}", String.valueOf(remainingTime));
                    player.sendActionBar(actionBar);
                } else {
                    String messageOut = ChatUtil.colorize(Config.getConfig().getString(OUT_SECTION + "message"));
                    String actionBarOut = ChatUtil.colorize(Config.getConfig().getString(OUT_SECTION + "action-bar"));
                    String titleOut = ChatUtil.colorize(Config.getConfig().getString(OUT_SECTION + "title"));
                    String subTitleOut = ChatUtil.colorize(Config.getConfig().getString(OUT_SECTION + "sub-title"));
                    player.sendMessage(messageOut);
                    player.sendActionBar(actionBarOut);
                    player.sendTitle(titleOut, subTitleOut);
                    removeCooldown(player);
                    cancel();
                }
            }
        };
        task.runTaskTimer(LastTools.instance, 0, 20);
        taskMap.put(player.getUniqueId(), task);
    }

    public static void removeCooldown(Player player) {
        BossBar bossBar = bossBarMap.remove(player.getUniqueId());
        if (bossBar != null) {
            bossBar.removeAll();
        }
        BukkitRunnable task = taskMap.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }

    private static void sendTitleAndActionBar(Player player, String message, String title, String subTitle) {
        player.sendMessage(message);
        player.sendTitle(title, subTitle);
    }
}