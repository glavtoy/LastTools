package ru.lastlord.lasttools.crypto.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import ru.lastlord.lasttools.crypto.economy.coin.Coin;
import ru.lastlord.lasttools.crypto.manager.CoinManager;
import ru.lastlord.lasttools.crypto.manager.CryptoManager;

public class Placeholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "lastcrypto";
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
        if (params.contains("_price")) {
            final Coin coin = CoinManager.getCoin(params.replace("_price", "").toUpperCase());
            if (coin != null) return String.valueOf(coin.getCost());
        } else {
            final Coin coin = CoinManager.getCoin(params.toUpperCase());
            if (coin != null) return String.valueOf(CryptoManager.getBalance(CryptoManager.getBalances(offlinePlayer.getName()).join(), coin));
        }
        return "";
    }
}
