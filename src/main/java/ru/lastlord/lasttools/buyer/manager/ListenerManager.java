package ru.lastlord.lasttools.buyer.manager;

import ru.lastlord.lasttools.buyer.listener.MainGuiListener;

public class ListenerManager {

    public static void registerListeners() {
        new MainGuiListener();
    }
}
