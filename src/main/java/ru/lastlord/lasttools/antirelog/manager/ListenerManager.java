package ru.lastlord.lasttools.antirelog.manager;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.antirelog.listener.EventListener;

public class ListenerManager {

    public static void registerListeners() {
        LastTools.instance.getServer().getPluginManager().registerEvents(new EventListener(), LastTools.instance);
    }
}
