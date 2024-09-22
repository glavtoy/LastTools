package ru.lastlord.lasttools.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.configuration.Config;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.HashMap;
import java.util.UUID;

public class CommandRepair implements CommandExecutor {

    final HashMap<UUID, Integer> cooldowns = new HashMap<>();

    public CommandRepair() {
        LastTools.instance.getCommand("repair").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.hasPermission("lasttools.repair") || player.hasPermission("*") || player.isOp()) {
                if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
                    if (!cooldowns.containsKey(player.getUniqueId())) cooldowns.put(player.getUniqueId(), 0);
                    if (cooldowns.get(player.getUniqueId()) == 0) {
                        if (!player.hasPermission("lasttools.repair.*") && !player.hasPermission("lasttools.repair.bypass") && !player.hasPermission("*") && !player.isOp()) {
                            cooldowns.put(player.getUniqueId(), Config.getConfig().getInt("locale.repair.cooldown.time"));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (cooldowns.get(player.getUniqueId()) > 0)
                                        cooldowns.put(player.getUniqueId(), cooldowns.get(player.getUniqueId()) - 1);
                                    else cancel();
                                }
                            }.runTaskTimer(LastTools.instance, 0, 20);
                        }
                        player.getItemInHand().setDurability((short) 0);
                        ChatUtil.sendMessage(player, "repair.player", new HashMap<>());
                    } else {
                        final int totalSecs = cooldowns.get(player.getUniqueId());
                        final int hours = totalSecs / 3600;
                        final int minutes = (totalSecs % 3600) / 60;
                        final int seconds = totalSecs % 60;
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("h", String.valueOf(hours));
                        placeholders.put("m", String.valueOf(minutes));
                        placeholders.put("s", String.valueOf(seconds));
                        ChatUtil.sendMessage(player, "repair.cooldown.message", placeholders);
                    }
                } else {
                    ChatUtil.sendMessage(player, "repair.no-item", new HashMap<>());
                }
            } else {
                for (Rank rank : RankManager.ranks) {
                    if (rank.getPermissions().contains("lasttools.repair") || rank.getPermissions().contains("*")) {
                        final HashMap<String, String> placeholders = new HashMap<>();
                        placeholders.put("rank", rank.getPrefix());
                        ChatUtil.sendMessage(player, "no-perms", placeholders);
                        break;
                    }
                }
            }
        }
        return true;
    }
}
