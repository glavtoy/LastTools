package ru.lastlord.lasttools.buyer.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.buyer.manager.GuiManager;
import ru.lastlord.lasttools.buyer.manager.SampleManager;

public class CommandBuyer implements CommandExecutor {

    public CommandBuyer() {
        LastTools.instance.getCommand("buyer").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (SampleManager.currentSample != null) GuiManager.openCurrentSampleGui(player);
            else {
                LastTools.instance.getLogger().info("<LastBuyer> Неправильно настроены шаблоны в items.yml или их режим в поле change-samples в config.yml");
                LastTools.instance.getServer().getPluginManager().disablePlugin(LastTools.instance);
            }
        }
        return true;
    }
}
