package ru.lastlord.lasttools.crypto.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.scheduler.BukkitRunnable;
import ru.lastlord.lasttools.LastTools;
import ru.lastlord.lasttools.crypto.LastCrypto;
import ru.lastlord.lasttools.crypto.configuration.WalletConfig;
import ru.lastlord.lasttools.crypto.economy.coin.Coin;
import ru.lastlord.lasttools.crypto.item.DecorationItem;
import ru.lastlord.lasttools.crypto.util.ChatUtil;
import ru.lastlord.lasttools.crypto.util.SoundUtil;
import ru.lastlord.lasttools.crypto.wallet.Wallet;

import java.util.*;

public class WalletManager {

    public static HashMap<UUID, Wallet> wallets = new HashMap<>();

    public static void clearGUI() {
        for (Map.Entry<UUID, Wallet> wallet : wallets.entrySet()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.getPlayer(wallet.getKey()).closeInventory();
                    }
                }.runTask(LastTools.instance);
                wallets.remove(wallet.getKey());
        }
    }

    public static void openWalletGui(Player player) {
        if (wallets.containsKey(player.getUniqueId())) {
            player.openInventory(wallets.get(player.getUniqueId()).getInventory());
            GuiManager.currentGui.put(player.getUniqueId(), wallets.get(player.getUniqueId()).getInventory());
        } else {
            final FileConfiguration config = WalletConfig.getConfig();
            LinkedList<DecorationItem> decorations = new LinkedList<>();
            Inventory inventory = Bukkit.createInventory(null, config.getInt("size"), config.getString("title"));
            config.getConfigurationSection("items").getKeys(false).forEach(item -> {
                ItemStack itemStack = null;
                final String path = "items." + item + ".";
                ItemStack head = null;
                if (LastCrypto.headDatabaseAPI != null) head = LastCrypto.headDatabaseAPI.getItemHead(config.getString(path + "id"));
                if (head != null) itemStack = head;
                else {
                    final Material material = Material.getMaterial(config.getString(path+"id"));
                    itemStack = new ItemStack(material);
                    final byte data = Byte.parseByte(config.getString(path+"data"));
                    if (data == -1) itemStack = new ItemStack(material);
                    else itemStack = new ItemStack(material, 1, data);
                }

                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatUtil.colorize(config.getString(path+"name")));

                final List<String> lore = new ArrayList<>();

                config.getStringList(path + "lore").forEach(line -> {
                    String replaced = line;
                    for (Coin coin : CoinManager.getCoins()) {
                        final String replaceAmount = "{" + coin.getSymbol().toLowerCase() + "}";
                        final String replacePrice = "{" + coin.getSymbol().toLowerCase() + "_price}";
                        replaced = replaced
                                .replace(replaceAmount, String.valueOf(CryptoManager.getBalance(CryptoManager.getBalances(player.getName()).join(), coin)))
                                .replace(replacePrice, String.valueOf(coin.getCost()));
                    }
                    lore.add(ChatUtil.colorize(replaced));
                });

                itemMeta.setLore(lore);

                itemStack.setItemMeta(itemMeta);

                Wallet wallet = Wallet.builder()
                        .title(config.getString("title"))
                        .size(config.getInt("size"))
                        .decorations(decorations)
                        .inventory(inventory)
                        .name(player.getName()).build();
                wallets.put(player.getUniqueId(), wallet);

                final DecorationItem decorationItem = DecorationItem.builder()
                        .id(item)
                        .itemStack(itemStack)
                        .slots(config.getIntegerList(path+"slots"))
                        .itemId(config.getString(path+"id"))
                        .name(config.getString(path+"name"))
                        .itemLore(config.getStringList(path+"lore"))
                        .data(Byte.parseByte(config.getString(path+"data")))
                        .headDatabase(config.getBoolean(path+"headdatabase"))
                        .wallet(wallet).build();

                decorations.add(decorationItem);

                for (int slot : decorationItem.getSlots()) inventory.setItem(slot, itemStack);
            });
            GuiManager.currentGui.put(player.getUniqueId(), wallets.get(player.getUniqueId()).getInventory());
            player.openInventory(wallets.get(player.getUniqueId()).getInventory());
        }
        SoundUtil.playSound(player, "open-gui");
    }
}
