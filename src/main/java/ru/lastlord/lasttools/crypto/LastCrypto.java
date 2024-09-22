package ru.lastlord.lasttools.crypto;

import me.arcaniax.hdb.api.HeadDatabaseAPI;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.crypto.configuration.Config;
import ru.lastlord.lasttools.crypto.manager.CoinManager;
import ru.lastlord.lasttools.crypto.manager.CommandManager;
import ru.lastlord.lasttools.crypto.manager.ConfigurationManager;
import ru.lastlord.lasttools.crypto.manager.ListenerManager;
import ru.lastlord.lasttools.crypto.placeholder.Placeholder;
import ru.lastlord.lasttools.crypto.runnable.CryptoPriceUpdateRunnable;

public final class LastCrypto {

    public static HeadDatabaseAPI headDatabaseAPI;
    public static LastCrypto instance;

    public void init() {
        instance = this;
        LastTools.instance.getLogger().info("<LastCrypto> Подключение PlaceholderAPI..");
        setupExpansions();
        ConfigurationManager.loadConfigurations();
        if (setupHeadDataBase()) {
            LastTools.instance.getLogger().info("<LastCrypto> Ожидание загрузки HeadDatabase..");
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (headDatabaseAPI.getItemHead("1") != null) {
                        CoinManager.initCoins();
                        LastTools.instance.getLogger().info("<LastCrypto> Регистрация команд..");
                        CommandManager.registerCommands();
                        LastTools.instance.getLogger().info("<LastCrypto> Регистрация событий..");
                        ListenerManager.registerListeners();
                        LastTools.instance.getLogger().info("<LastCrypto> Запуск потока..");
                        CryptoPriceUpdateRunnable.start();
                        LastTools.instance.getLogger().info("<LastCrypto> Плагин загружен успешно!");
                        cancel();
                    }
                }
            }.runTaskTimer(LastTools.instance, 0, 20);
        } else {
            CoinManager.initCoins();
            LastTools.instance.getLogger().info("<LastCrypto> Регистрация команд..");
            CommandManager.registerCommands();
            LastTools.instance.getLogger().info("<LastCrypto> Регистрация событий..");
            ListenerManager.registerListeners();
            LastTools.instance.getLogger().info("<LastCrypto> Запуск потока..");
            CryptoPriceUpdateRunnable.start();
            LastTools.instance.getLogger().info("<LastCrypto> Плагин загружен успешно!");
        }
    }

    private void setupExpansions() {
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) new Placeholder().register();
    }

    private boolean setupHeadDataBase() {
        if (Config.getConfig().getBoolean("use-headdatabase") && Bukkit.getServer().getPluginManager().getPlugin("HeadDatabase") != null) {
            headDatabaseAPI = new HeadDatabaseAPI();
            return true;
        }
        return false;
    }
}