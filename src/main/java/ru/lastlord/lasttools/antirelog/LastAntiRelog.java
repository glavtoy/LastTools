package ru.lastlord.lasttools.antirelog;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.antirelog.manager.ConfigurationManager;
import ru.lastlord.lasttools.antirelog.manager.CooldownManager;
import ru.lastlord.lasttools.antirelog.manager.ListenerManager;

public final class LastAntiRelog {

    public static LastAntiRelog instance;

    public void init() {
       instance = this;
       ConfigurationManager.loadConfigurations();
       LastTools.instance.getLogger().info("<LastAntiRelog> Инициализация материалов и значений..");
       CooldownManager.setSecondsForMaterials();
       LastTools.instance.getLogger().info("<LastAntiRelog> Регистрация событий..");
       ListenerManager.registerListeners();
       LastTools.instance.getLogger().info("<LastAntiRelog> Плагин успешно загружен!");
    }
}
