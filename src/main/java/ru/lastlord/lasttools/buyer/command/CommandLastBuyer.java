package ru.lastlord.lasttools.buyer.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.buyer.manager.ConfigurationManager;
import ru.lastlord.lasttools.buyer.manager.GuiManager;
import ru.lastlord.lasttools.buyer.manager.SampleManager;
import ru.lastlord.lasttools.buyer.runnable.BuyerUpdateRunnable;
import ru.lastlord.lasttools.buyer.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class CommandLastBuyer implements CommandExecutor, TabCompleter {

    public CommandLastBuyer() {
        LastTools.instance.getCommand("lastbuyer").setExecutor(this);
        LastTools.instance.getCommand("lastbuyer").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.isOp() || player.hasPermission("glavbuyer.admin")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        ConfigurationManager.loadConfigurations();
                        SampleManager.loadSamples();
                        SampleManager.updateSamples();
                        BuyerUpdateRunnable.loadQueue();
                        SampleManager.newSample();
                        BuyerUpdateRunnable.startUpdating();
                        ChatUtil.sendMessage(sender, "reload", null);
                    } else if (args[0].equalsIgnoreCase("update")) {
                        SampleManager.updateSamples();
                        SampleManager.newSample();
                        GuiManager.updateCurrentSampleGui();
                        ChatUtil.broadcast("update", null);
                    } else {
                        ChatUtil.sendMessage(player, "glavbuyer-use", null);
                    }
                } else {
                    ChatUtil.sendMessage(player, "glavbuyer-use", null);
                }
            }
        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    ConfigurationManager.loadConfigurations();
                    SampleManager.loadSamples();
                    SampleManager.updateSamples();
                    ChatUtil.sendMessage(sender, "reload", null);
                } else if (args[0].equalsIgnoreCase("update")) {
                    SampleManager.updateSamples();
                    SampleManager.newSample();
                    GuiManager.updateCurrentSampleGui();
                    ChatUtil.broadcast("update", null);
                } else {
                    ChatUtil.sendMessage(sender, "glavbuyer-use", null);
                }
            } else {
                ChatUtil.sendMessage(sender, "glavbuyer-use", null);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        final List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.isOp() || player.hasPermission("glavbuyer.admin")) {
                if (args.length == 1) {
                    list.add("reload");
                    list.add("update");
                }
            }
        } else {
            if (args.length == 1) {
                list.add("reload");
                list.add("update");
            }
        }
        return list;
    }
}
