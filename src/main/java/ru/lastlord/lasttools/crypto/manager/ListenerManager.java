package ru.lastlord.lasttools.crypto.manager;

import ru.lastlord.lasttools.crypto.listener.MainGuiListener;

public class ListenerManager {

    public static void registerListeners() {
        new MainGuiListener();
    }
}
