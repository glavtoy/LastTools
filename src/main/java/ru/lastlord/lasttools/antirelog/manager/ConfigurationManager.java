package ru.lastlord.lasttools.antirelog.manager;

import ru.lastlord.lasttools.antirelog.configuration.Config;

public class ConfigurationManager {

    public static void loadConfigurations() {
        Config.loadConfig();
    }
}
