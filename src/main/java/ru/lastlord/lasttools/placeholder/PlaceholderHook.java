package ru.lastlord.lasttools.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.economy.CoinEconomy;
import ru.lastlord.lasttools.economy.TokenEconomy;
import ru.lastlord.lasttools.manager.RankManager;
import ru.lastlord.lasttools.manager.StatisticManager;
import ru.lastlord.lasttools.util.ChatUtil;

public class PlaceholderHook extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "lasttools";
    }

    @Override
    public @NotNull String getAuthor() {
        return "lastlord";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @NotNull String onRequest(@NotNull final OfflinePlayer offlinePlayer, @NotNull final String params) {
        switch (params) {
            case "coins": return String.valueOf(CoinEconomy.getBalance(offlinePlayer.getName()).join());
            case "tokens": return String.valueOf(TokenEconomy.getBalance(offlinePlayer.getName()).join());
            case "rank": return RankManager.getRank(offlinePlayer.getName()).join().getPrefix();
            case "clan": return ChatUtil.colorize("&cНет");
            case "title": return ChatUtil.colorize("&cНет");
            case "hours": return String.valueOf(LastTools.database.getHours(offlinePlayer.getName()).join());
            case "kills": return String.valueOf(LastTools.database.getKills(offlinePlayer.getName()).join());
            case "deaths": return String.valueOf(LastTools.database.getDeaths(offlinePlayer.getName()).join());
        }
        return "";
    }
}

