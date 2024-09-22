package ru.lastlord.lasttools.buyer.manager;

import ru.lastlord.lasttools.buyer.command.CommandBuyer;
import ru.lastlord.lasttools.buyer.command.CommandLastBuyer;

public class CommandManager {

    public static void registerCommands() {
        new CommandBuyer();
        new CommandLastBuyer();
    }
}
