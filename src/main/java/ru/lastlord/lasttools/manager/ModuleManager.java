package ru.lastlord.lasttools.manager;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.antirelog.LastAntiRelog;
import ru.lastlord.lasttools.buyer.LastBuyer;
import ru.lastlord.lasttools.chatguard.LastChatGuard;
import ru.lastlord.lasttools.crypto.LastCrypto;

public class ModuleManager {

    public static void initModules() {
        new LastBuyer().init();
        new LastAntiRelog().init();
        new LastChatGuard().init();
        new LastCrypto().init();
        LastTools.instance.getLogger().info("Модули успешно зарегистрированы!");
    }
}
