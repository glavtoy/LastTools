package ru.lastlord.lasttools.chatguard.manager;

import ru.lastlord.lasttools.chatguard.configuration.Config;

public class ConfigurationManager {

    public static void registerConfigurations() {
        Config.loadConfig();
    }
}
