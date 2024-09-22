package ru.lastlord.lasttools.manager;

import ru.lastlord.lasttools.configuration.Config;

public class ConfigurationManager {

    public static void loadConfigurations() {
        Config.loadConfig();
    }
}
