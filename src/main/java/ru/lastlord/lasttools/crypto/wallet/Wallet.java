package ru.lastlord.lasttools.crypto.wallet;

import lombok.Builder;
import lombok.Getter;

import org.bukkit.inventory.Inventory;

import ru.lastlord.lasttools.crypto.item.DecorationItem;

import java.util.LinkedList;

@Getter
@Builder
public class Wallet {
    private String title;
    private int size;
    private LinkedList<DecorationItem> decorations;
    private Inventory inventory;
    private String name;
}
