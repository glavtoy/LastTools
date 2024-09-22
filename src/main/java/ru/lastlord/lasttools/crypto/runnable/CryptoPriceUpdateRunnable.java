package ru.lastlord.lasttools.crypto.runnable;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.crypto.configuration.Config;
import ru.lastlord.lasttools.crypto.item.DecorationItem;
import ru.lastlord.lasttools.crypto.manager.CoinManager;
import ru.lastlord.lasttools.crypto.manager.WalletManager;
import ru.lastlord.lasttools.crypto.util.CryptoPriceChecker;

public class CryptoPriceUpdateRunnable {

    private static BukkitTask runnable;

    public static void start() {
        runnable = new BukkitRunnable() {

            @Override
            public void run() {
                WalletManager.wallets.forEach((uuid, wallet) -> {
                    wallet.getDecorations().forEach(DecorationItem::updateItemStack);
                });
                CoinManager.getCoins().forEach(CryptoPriceChecker::updatePrice);
            }
        }.runTaskTimer(LastTools.instance, 0, 20L * Config.getConfig().getInt("update-time"));
    }
}
