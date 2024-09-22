package ru.lastlord.lasttools.crypto.item;

import lombok.Builder;
import lombok.Getter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.lastlord.lasttools.crypto.LastCrypto;
import ru.lastlord.lasttools.crypto.configuration.WalletConfig;
import ru.lastlord.lasttools.crypto.economy.coin.Coin;
import ru.lastlord.lasttools.crypto.manager.CoinManager;
import ru.lastlord.lasttools.crypto.manager.CryptoManager;
import ru.lastlord.lasttools.crypto.util.ChatUtil;
import ru.lastlord.lasttools.crypto.wallet.Wallet;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class DecorationItem {
    private String id;
    private ItemStack itemStack;
    private List<Integer> slots;
    private String itemId;
    private String name;
    private List<String> itemLore;
    private byte data;
    private boolean headDatabase;
    private Wallet wallet;

    public void updateItemStack() {
        if (headDatabase) {
            final ItemStack head = LastCrypto.headDatabaseAPI.getItemHead(itemId);
            if (head != null) itemStack = head;
        } else {
            if (data == -1) itemStack = new ItemStack(Material.getMaterial(itemId));
            else itemStack = new ItemStack(Material.getMaterial(itemId), 1, data);
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatUtil.colorize(name));
        final List<String> lore = new ArrayList<>();

        WalletConfig.getConfig().getStringList("items." + id + ".lore").forEach(line -> {
            String replaced = line;
            for (Coin coin : CoinManager.getCoins()) {
                final String replaceAmount = "{" + coin.getSymbol().toLowerCase() + "}";
                final String replacePrice = "{" + coin.getSymbol().toLowerCase() + "_price}";
                replaced = replaced
                        .replace(replaceAmount, String.valueOf(CryptoManager.getBalance(CryptoManager.getBalances(wallet.getName()).join(), coin)))
                        .replace(replacePrice, String.valueOf(coin.getCost()));
            }
            lore.add(ChatUtil.colorize(replaced));
        });

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        slots.forEach(slot->wallet.getInventory().setItem(slot, itemStack));
    }
}
