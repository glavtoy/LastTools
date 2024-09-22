package ru.lastlord.lasttools.chatguard.manager;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.chatguard.listener.EventListener;

public class ListenerManager {

    public static void registerListeners() {
        LastTools.instance.getServer().getPluginManager().registerEvents(new EventListener(), LastTools.instance);
    }
}
