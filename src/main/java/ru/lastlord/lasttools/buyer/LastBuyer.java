package ru.lastlord.lasttools.buyer;

import me.arcaniax.hdb.api.HeadDatabaseAPI;

import org.bukkit.scheduler.BukkitRunnable;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.buyer.configuration.Config;
import ru.lastlord.lasttools.buyer.economy.CoinEconomyHandler;
import ru.lastlord.lasttools.buyer.economy.TokenEconomyHandler;
import ru.lastlord.lasttools.buyer.economy.impl.EconomyHandler;
import ru.lastlord.lasttools.buyer.item.Sample;
import ru.lastlord.lasttools.buyer.manager.CommandManager;
import ru.lastlord.lasttools.buyer.manager.ConfigurationManager;
import ru.lastlord.lasttools.buyer.manager.ListenerManager;
import ru.lastlord.lasttools.buyer.manager.SampleManager;
import ru.lastlord.lasttools.buyer.placeholder.Placeholder;
import ru.lastlord.lasttools.buyer.runnable.BuyerUpdateRunnable;

public final class LastBuyer {

    public static HeadDatabaseAPI headDatabaseAPI;
    public static LastBuyer instance;
    public static EconomyHandler economyHandler;

    public void init() {
        instance = this;
        ConfigurationManager.loadConfigurations();
        LastTools.instance.getLogger().info("<LastBuyer> Подключение PlaceholderAPI..");
        setupExpansions();
        if (setupHeadDataBase()) {
            LastTools.instance.getLogger().info("<LastBuyer> Ожидание загрузки HeadDatabase..");
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (headDatabaseAPI.getItemHead("1") != null) {
                        LastTools.instance.getLogger().info("<LastBuyer> Загрузка шаблонов скупщика..");
                        SampleManager.loadSamples();
                        SampleManager.updateSamples();
                        BuyerUpdateRunnable.loadQueue();
                        SampleManager.newSample();
                        LastTools.instance.getLogger().info("<LastBuyer> Запуск потока..");
                        BuyerUpdateRunnable.startUpdating();
                        LastTools.instance.getLogger().info("<LastBuyer> Регистрация команд..");
                        CommandManager.registerCommands();
                        LastTools.instance.getLogger().info("<LastBuyer> Регистрация событий..");
                        ListenerManager.registerListeners();
                        LastTools.instance.getLogger().info("<LastBuyer> Плагин загружен успешно!");
                        cancel();
                    }
                }
            }.runTaskTimer(LastTools.instance, 0, 20);
        } else {
            LastTools.instance.getLogger().info("<LastBuyer> Загрузка шаблонов скупщика..");
            SampleManager.loadSamples();
            SampleManager.updateSamples();
            BuyerUpdateRunnable.loadQueue();
            SampleManager.newSample();
            LastTools.instance.getLogger().info("<LastBuyer> Запуск потока..");
            BuyerUpdateRunnable.startUpdating();
            LastTools.instance.getLogger().info("<LastBuyer> Регистрация команд..");
            CommandManager.registerCommands();
            LastTools.instance.getLogger().info("<LastBuyer> Регистрация событий..");
            ListenerManager.registerListeners();
            LastTools.instance.getLogger().info("<LastBuyer> Плагин загружен успешно!");
        }
    }

    private void setupExpansions() {
        if (LastTools.instance.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) new Placeholder().register();
    }

    public void setupEconomy(Sample sample, String type) {
        if (type.equalsIgnoreCase("coins")) sample.setEconomyHandler(new CoinEconomyHandler());
        else if (type.equalsIgnoreCase("tokens")) sample.setEconomyHandler(new TokenEconomyHandler());
    }

    private boolean setupHeadDataBase() {
        if (Config.getConfig().getBoolean("use-headdatabase") && LastTools.instance.getServer().getPluginManager().getPlugin("HeadDatabase") != null) {
            headDatabaseAPI = new HeadDatabaseAPI();
            return true;
        }
        return false;
    }
}
