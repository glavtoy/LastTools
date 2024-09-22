package ru.lastlord.lasttools.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ru.lastlord.lasttools.configuration.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtil {

    public static String colorize(String string) {
        final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

        for (Matcher matcher = pattern.matcher(string); matcher.find(); matcher = pattern.matcher(string)) {
            final String hexCode = string.substring(matcher.start(), matcher.end());
            final String replaceSharp = hexCode.replace('#', 'x');
            final char[] charArray = replaceSharp.toCharArray();
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < charArray.length; ++i) builder.append("&").append(charArray[i]);
            string = string.replace(hexCode, builder.toString());
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void sendMessage(CommandSender recipient, String messagePath, HashMap<String, String> placeholers) {
        if (placeholers == null) placeholers = new HashMap<>();
        placeholers.put("prefix", Config.getConfig().getString("locale.prefix"));
        String message = Config.getConfig().getString("locale."+messagePath);
        if (message != null) {
            for (Map.Entry<String, String> entry : placeholers.entrySet()) {
                message = message.replace("{min}", Config.getConfig().getString("locale.coins.send-limit.min")).replace("{max}", Config.getConfig().getString("locale.coins.send-limit.max"));
                if (Config.getConfig().getConfigurationSection("placeholders").getKeys(false).contains(entry.getKey()))
                    message = message.replace("{" + entry.getKey() + "}", Config.getConfig().getString("placeholders." + entry.getKey() + "." + entry.getValue()));
                else message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }
            recipient.sendMessage(colorize(message));
        }
    }

    public static void sendListMessage(CommandSender recipient, String messagePath, HashMap<String, String> placeholers) {
        if (placeholers == null) placeholers = new HashMap<>();
        placeholers.put("prefix", Config.getConfig().getString("locale.prefix"));
        final StringBuilder message = new StringBuilder();
        List<String> messages = Config.getConfig().getStringList("locale."+messagePath);
        if (messages != null) {
            HashMap<String, String> finalPlaceholers1 = placeholers;
            messages.forEach(msg -> {
                for (Map.Entry<String, String> entry : finalPlaceholers1.entrySet()) {
                    if (Config.getConfig().getConfigurationSection("placeholders").getKeys(false).contains(entry.getKey()))
                        msg = msg.replace("{" + entry.getKey() + "}", Config.getConfig().getString("placeholders." + entry.getKey() + "." + entry.getValue()));
                    else msg = msg.replace("{" + entry.getKey() + "}", entry.getValue());
                }
                message.append(msg + "\n");
            });
            recipient.sendMessage(colorize(message.toString().replace("[", "").replace("]", "").replace(",", "\n")));
        }
    }

    public static void broadcastMessage(String configPath, HashMap<String, String> placeholders) {
        Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, configPath, placeholders));
    }

    public static void broadcastListMessage(String configPath, HashMap<String, String> placeholders) {
        Bukkit.getOnlinePlayers().forEach(player -> sendListMessage(player, configPath, placeholders));
    }
}
