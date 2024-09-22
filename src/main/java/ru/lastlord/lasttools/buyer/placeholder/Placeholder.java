package ru.lastlord.lasttools.buyer.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import ru.lastlord.lasttools.buyer.runnable.BuyerUpdateRunnable;

public class Placeholder extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "buyer";
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
        if (params.equals("time")) {
            return BuyerUpdateRunnable.getFormattedTime();
        }
        return "";
    }
}
