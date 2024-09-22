package ru.lastlord.lasttools.buyer.runnable;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.buyer.configuration.Config;
import ru.lastlord.lasttools.buyer.item.DecorationItem;
import ru.lastlord.lasttools.buyer.item.Sample;
import ru.lastlord.lasttools.buyer.item.SellItem;
import ru.lastlord.lasttools.buyer.manager.GuiManager;
import ru.lastlord.lasttools.buyer.manager.SampleManager;
import ru.lastlord.lasttools.buyer.util.ChatUtil;
import ru.lastlord.lasttools.buyer.util.SoundUtil;

import java.util.LinkedList;
import java.util.List;

public class BuyerUpdateRunnable {

    public final static LinkedList<Sample> queue = new LinkedList<>();
    @Getter @Setter private static int count;
    @Getter @Setter private static int lastSample;
    public static String changeSamples;
    public static BukkitTask updateRunnable;
    private static int time;

    public static void loadQueue() {
        if (!queue.isEmpty()) queue.clear();
        changeSamples = Config.getConfig().getString("change-samples");
        final List<String> list = Config.getConfig().getStringList("samples-queue");
        final LinkedList<Sample> samples = new LinkedList<>();
        list.forEach(id->samples.add(SampleManager.getSampleById(id)));
        queue.addAll(samples);
        count = 0;
        lastSample = !queue.isEmpty() ? queue.size()-1 : 0;
    }

    public static String getFormattedTime() {
        final int hours = time/3600;
        final int minutes = (time%3600)/60;
        final int seconds = time%60;
        return Config.getConfig().getString("time-format")
                .replace("{h}", String.valueOf(hours))
                .replace("{m}", String.valueOf(minutes))
                .replace("{s}", String.valueOf(seconds));
    }

    public static void startUpdating() {
        time = Config.getConfig().getInt("update-time");
        if (updateRunnable != null) updateRunnable.cancel();
        updateRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (SampleManager.currentSample != null) {
                    if (changeSamples != null) {
                        if (time == 0) {
                            if (changeSamples.equalsIgnoreCase("queue")) {
                                SampleManager.updateSamples();
                                SampleManager.newSample();
                                GuiManager.updateCurrentSampleGui();
                                ChatUtil.broadcast("update", null);
                                Bukkit.getOnlinePlayers().forEach(p-> SoundUtil.playSound(p, "update"));
                                if (count == lastSample) {
                                    count = 0;
                                } else {
                                    count++;
                                }
                                time--;
                            } else if (changeSamples.equalsIgnoreCase("random")) {
                                SampleManager.updateSamples();
                                SampleManager.newSample();
                                GuiManager.updateCurrentSampleGui();
                                ChatUtil.broadcast("update", null);
                                Bukkit.getOnlinePlayers().forEach(p->SoundUtil.playSound(p, "update"));
                                time--;
                            } else {
                                LastTools.instance.getLogger().severe("<LastBuyer> Неправильно заполненно значение change-samples в config.yml");
                                LastTools.instance.getServer().getPluginManager().disablePlugin(LastTools.instance);
                            }
                            time = Config.getConfig().getInt("update-time");
                        } else {
                            time--;
                        }
                        SampleManager.currentSample.getSellItems().forEach(SellItem::updateItemStack);
                        SampleManager.currentSample.getDecorations().forEach(DecorationItem::updateItemStack);
                        SampleManager.currentSample.updateGuiOnAllPlayers();
                    } else {
                        LastTools.instance.getLogger().severe("<LastBuyer> Неправильно заполненно значение change-samples в config.yml");
                        LastTools.instance.getServer().getPluginManager().disablePlugin(LastTools.instance);
                    }
                } else {
                    LastTools.instance.getLogger().severe("<LastBuyer> Неправильно настроены шаблоны в items.yml или их режим в поле change-samples в config.yml");
                    LastTools.instance.getServer().getPluginManager().disablePlugin(LastTools.instance);
                }
            }
        }.runTaskTimerAsynchronously(LastTools.instance, 0, 20);
    }
}
