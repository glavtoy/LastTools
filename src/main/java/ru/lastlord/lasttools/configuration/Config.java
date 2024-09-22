package ru.lastlord.lasttools.configuration;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.util.ChatUtil;

import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;

public class Config {

    private static File file;
    private static @Getter FileConfiguration config;

    public static void loadConfig() {
        file = new File(LastTools.instance.getDataFolder(), "config.yml");
        if (!file.exists()) LastTools.instance.saveResource("config.yml", true);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static String getPermanentBanLayout(String admin, String reason) {
        final StringJoiner joiner = new StringJoiner("\n");
        config.getStringList("locale.ban.layout").forEach(line -> joiner.add(ChatUtil.colorize(line.replace("{admin}", admin).replace("{reason}", reason))));
        return joiner.toString();
    }

    public static String getTemporaryBanLayout(long time, String admin, String reason) {
        final String path = "locale.tempban.time-format.";
        final String days = (time / 86400) + Config.getConfig().getString(path+"days");
        final String hours = (time % 86400 / 3600) + Config.getConfig().getString(path+"hours");
        final String minutes = (time % 3600 / 60) + Config.getConfig().getString(path+"minutes");
        final String seconds = (time % 60) + Config.getConfig().getString(path+"seconds");
        final String totalTime = String.format("%s %s %s %s", days, hours, minutes, seconds);
        final StringJoiner joiner = new StringJoiner("\n");
        config.getStringList("locale.tempban.layout").forEach(line -> joiner.add(ChatUtil.colorize(line.replace("{admin}", admin).replace("{reason}", reason).replace("{time}", totalTime))));
        return joiner.toString();
    }

    public static String getTemporaryMuteLayout(long time, String admin, String reason) {
        final String path = "locale.tempmute.time-format.";
        final String days = (time / 86400) + Config.getConfig().getString(path+"days");
        final String hours = (time % 86400 / 3600) + Config.getConfig().getString(path+"hours");
        final String minutes = (time % 3600 / 60) + Config.getConfig().getString(path+"minutes");
        final String seconds = (time % 60) + Config.getConfig().getString(path+"seconds");
        final String totalTime = String.format("%s %s %s %s", days, hours, minutes, seconds);
        final StringJoiner joiner = new StringJoiner("\n");
        config.getStringList("locale.tempmute.layout").forEach(line -> joiner.add(ChatUtil.colorize(line.replace("{admin}", admin).replace("{reason}", reason).replace("{time}", totalTime))));
        return joiner.toString();
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {}
    }
}
