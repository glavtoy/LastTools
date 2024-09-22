package ru.lastlord.lasttools.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.lastlord.lasttools.LastTools;

public class CommandEnderChest implements CommandExecutor {

    public CommandEnderChest() {
        LastTools.instance.getCommand("enderchest").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {

            } else if (args.length == 1) {

            } else {

            }
        }
        return true;
    }
}
