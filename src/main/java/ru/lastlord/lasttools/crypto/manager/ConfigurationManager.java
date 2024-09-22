package ru.lastlord.lasttools.crypto.manager;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.crypto.configuration.Config;
import ru.lastlord.lasttools.crypto.configuration.WalletConfig;

public class ConfigurationManager {

    public static void loadConfigurations() {
        Config.loadConfig(LastTools.instance);
        WalletConfig.loadConfig(LastTools.instance);
    }
}
