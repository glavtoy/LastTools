package ru.lastlord.lasttools.auction.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class Item {
    private ItemStack itemStack;
    private int time;
    private int price;
    private String id;
    private String economy;
}
