package ru.lastlord.lasttools.chatguard;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.chatguard.manager.ConfigurationManager;
import ru.lastlord.lasttools.chatguard.manager.ListenerManager;

public final class LastChatGuard {

    public static LastChatGuard instance;

    public void init() {
        instance = this;
        ConfigurationManager.registerConfigurations();
        LastTools.instance.getLogger().info("<LastChatGuard> Регистрация событий..");
        ListenerManager.registerListeners();
        LastTools.instance.getLogger().info("<LastChatGuard> Плагин успешно загружен!");
    }
}
