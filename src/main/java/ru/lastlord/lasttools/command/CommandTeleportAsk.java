package ru.lastlord.lasttools.command;

import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.configuration.Config;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.teleport.data.TeleportData;
import ru.lastlord.lasttools.teleport.manager.TeleportManager;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.HashMap;

public class CommandTeleportAsk implements CommandExecutor {

    public CommandTeleportAsk() {
        LastTools.instance.getCommand("teleportask").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("lasttools.teleportask") || player.hasPermission("*") || player.isOp()) {
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (!target.getName().equalsIgnoreCase(player.getName())) {
                            if (!TeleportManager.exists(player, target)) {
                                new TeleportData(player, target, 10);
                                ComponentBuilder builder = new ComponentBuilder(ChatUtil.colorize(Config.getConfig().getString("locale.teleportask.message.upper").replace("{player}", player.getName())));
                                builder.append(new TextComponent(ChatUtil.colorize("\n" + Config.getConfig().getString("locale.teleportask.message.lower")) + " "));
                                builder.append(getButtonComponent(ChatUtil.colorize(Config.getConfig().getString("locale.teleportask.message.buttons.accept.text")), ChatUtil.colorize(Config.getConfig().getString("locale.teleportask.message.buttons.accept.hover")), "/teleportaccept " + player.getName()));
                                builder.append(new TextComponent(ChatUtil.colorize(" ")));
                                builder.append(getButtonComponent(ChatUtil.colorize(Config.getConfig().getString("locale.teleportask.message.buttons.deny.text")), ChatUtil.colorize(Config.getConfig().getString("locale.teleportask.message.buttons.deny.hover")), "/teleportdeny " + player.getName()));
                                target.spigot().sendMessage(builder.create());
                                final HashMap<String, String> placeholders = new HashMap<>();
                                placeholders.put("player", args[0]);
                                ChatUtil.sendMessage(player, "teleportask.from", placeholders);
                            }
                        } else {
                            ChatUtil.sendMessage(player, "teleportask.yourself", new HashMap<>());
                        }
                    } else {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("player", args[0]);
                        ChatUtil.sendMessage(player, "offline-player", placeholders);
                    }
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.teleportask") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            } else {
                if (player.hasPermission("lasttools.teleportask") || player.hasPermission("*") || player.isOp()) {
                    ChatUtil.sendMessage(player, "teleportask.usage", new HashMap<>());
                } else {
                    for (Rank rank : RankManager.ranks) {
                        if (rank.getPermissions().contains("lasttools.teleportask") || rank.getPermissions().contains("*")) {
                            final HashMap<String, String> placeholders = new HashMap<>();
                            placeholders.put("rank", rank.getPrefix());
                            ChatUtil.sendMessage(player, "no-perms", placeholders);
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }

    private BaseComponent getButtonComponent(String text, String hover, String command) {
        TextComponent button = new TextComponent(text);
        button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        return button;
    }
}
