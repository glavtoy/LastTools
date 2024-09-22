package ru.lastlord.lasttools;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.event.player.PlayerLoadEvent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import ru.lastlord.lasttools.auction.item.Item;
import ru.lastlord.lasttools.auction.manager.ItemManager;
import ru.lastlord.lasttools.hologram.manager.HologramManager;
import ru.lastlord.lasttools.hologram.runnable.LinesUpdater;
import ru.lastlord.lasttools.home.manager.HomeManager;
import ru.lastlord.lasttools.manager.*;
import ru.lastlord.lasttools.placeholder.PlaceholderHook;
import ru.lastlord.lasttools.runnable.Announcer;
import ru.lastlord.lasttools.runnable.BanCounter;
import ru.lastlord.lasttools.runnable.HoursConter;
import ru.lastlord.lasttools.sql.Database;
import ru.lastlord.lasttools.manager.VanishManager;

public final class LastTools extends JavaPlugin {

    public static LastTools instance;
    public static Database database;

    @Override
    public void onEnable() {
        instance = this;
        ConfigurationManager.loadConfigurations();
        RankManager.loadRanks();
        getLogger().info("Подключение к базе данных..");
        setupDatabase();
        getLogger().info("Регистрация команд..");
        CommandManager.registerCommands();
        getLogger().info("Регистрация слушателей..");
        ListenerManager.registerListeners();
        getLogger().info("Инициализация модулей..");
        ModuleManager.initModules();
        registerTABEvents();
        setupPlaceholders();
        Announcer.start();
        BanCounter.start();
        HoursConter.start();
        HomeManager.loadPlayersHomes();
        ItemManager.loadPlayersItems();
        HologramManager.loadHolograms();
        LinesUpdater.start();
        CacheManager.loadDatabaseCache();
        getLogger().info("Плагин успешно запущен!");
    }

    @Override
    public void onDisable() {
        HologramManager.unloadHolograms();
        VanishManager.vanished.forEach(uuid -> {
            VanishManager.bossBar.removeAll();
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) VanishManager.unVanishPlayer(Bukkit.getPlayer(uuid));
        });
    }

    public void setupDatabase() {
        database.connect();
    }

    private void setupPlaceholders() {
        new PlaceholderHook().register();
    }

    private void registerTABEvents() {
        TabAPI.getInstance().getEventBus().register(PlayerLoadEvent.class, event -> {
            String player = event.getPlayer().getName();
            if (!LastTools.database.containsPlayer(player).join()) LastTools.database.addNewPlayer(player);
            RankManager.setRank(player, RankManager.getRank(player).join());
        });
        for (Player p : Bukkit.getOnlinePlayers()) {
            final String player = p.getName();
            if (!LastTools.database.containsPlayer(player).join()) LastTools.database.addNewPlayer(player);
            RankManager.setRank(player, RankManager.getRank(player).join());
        }
    }
}