package ru.lastlord.lasttools.manager;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.listener.EventListener;

public class ListenerManager {

    public static void registerListeners() {
        new EventListener();
        LastTools.instance.getLogger().info("Слушатели успешно зарегистрированы!");
    }
}
