package ru.lastlord.lasttools.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.rank.Rank;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.HashMap;

public class CommandLoom implements CommandExecutor {

    public CommandLoom() {
        LastTools.instance.getCommand("loom").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.hasPermission("lasttools.loom") || player.hasPermission("*") || player.isOp()) {
                player.openLoom(player.getLocation(), true);
            } else {
                for (Rank rank : RankManager.ranks) {
                    if (rank.getPermissions().contains("lasttools.loom") || rank.getPermissions().contains("*")) {
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
