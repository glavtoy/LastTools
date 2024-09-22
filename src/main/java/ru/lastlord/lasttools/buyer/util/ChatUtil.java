package ru.lastlord.lasttools.buyer.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ru.lastlord.lasttools.buyer.configuration.Config;

import java.util.HashMap;
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

    public static void sendMessage(CommandSender recipient, String configPath, HashMap<String, String> placeholders) {
        final String prefix = colorize(Config.getConfig().getString("locale.prefix"));
        String message = Config.getConfig().getString("locale."+configPath);
        if (prefix != null && !prefix.equalsIgnoreCase("-")) {
            if (message != null && !message.equalsIgnoreCase("-")) {
                if (placeholders != null) {
                    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                        message = message.replace(entry.getKey(), entry.getValue());
                    }
                }
                recipient.sendMessage(prefix + " " + colorize(message));
            }
        } else {
            if (message != null && !message.equalsIgnoreCase("-")) {
                if (placeholders != null) {
                    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                        message = message.replace(entry.getKey(), entry.getValue());
                    }
                }
                recipient.sendMessage(colorize(message));
            }
        }
    }

    public static void broadcast(String configPath, HashMap<String, String> placeholders) {
        Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, configPath, placeholders));
    }
}
