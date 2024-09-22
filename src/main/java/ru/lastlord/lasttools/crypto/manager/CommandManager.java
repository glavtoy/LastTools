package ru.lastlord.lasttools.crypto.manager;

import ru.lastlord.lasttools.crypto.command.CommandCrypto;
import ru.lastlord.lasttools.crypto.command.CommandLastCrypto;

public class CommandManager {

    public static void registerCommands() {
        new CommandCrypto();
        new CommandLastCrypto();
    }
}
