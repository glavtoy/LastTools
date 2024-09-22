package ru.lastlord.lasttools.manager;

import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.command.*;

public class CommandManager {

    public static void registerCommands() {
        new CommandGamemode();
        new CommandWash();
        new CommandRank();
        new CommandCoins();
        new CommandTokens();
        new CommandBalance();
        new CommandDay();
        new CommandNight();
        new CommandSetSpawn();
        new CommandSpawn();
        new CommandFeed();
        new CommandHeal();
        new CommandSun();
        new CommandStorm();
        new CommandClear();
        new CommandInvsee();
        new CommandBan();
        new CommandUnban();
        new CommandTempBan();
        new CommandMute();
        new CommandUnmute();
        new CommandTempMute();
        new CommandFly();
        new CommandMsg();
        new CommandWorkbench();
        new CommandLoom();
        new CommandStoneCutter();
        new CommandGrindStone();
        new CommandCartographyTable();
        new CommandAnvil();
        new CommandSmithingTable();
        new CommandExt();
        new CommandDonateChat();
        new CommandStaffChat();
        new CommandRepair();
        new CommandSetHome();
        new CommandHome();
        new CommandHolograms();
        new CommandTeleport();
        new CommandVanish();
        new CommandSpeed();
        new CommandTeleportAsk();
        new CommandTeleportAccept();
        new CommandTeleportDeny();
        new CommandAuction();
        new CommandEnderChest();
        LastTools.instance.getLogger().info("Команды успешно зарегистрированы!");
    }
}
