package ru.lastlord.lasttools.buyer.manager;

import ru.lastlord.lasttools.buyer.configuration.Config;
import ru.lastlord.lasttools.buyer.configuration.Items;

public class ConfigurationManager {

    public static void loadConfigurations() {
        Config.loadConfig();
        Items.loadConfig();
    }
}
