package ru.lastlord.lasttools.buyer.item;

import lombok.Builder;
import lombok.Getter;

import lombok.ToString;
import org.bukkit.Material;

import java.util.List;

@Getter
@Builder
@ToString
public class Item {
    private final String itemId;
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final boolean headDatabase;
    private final boolean customName;
    private final boolean randomCost;
    private final boolean randomAmount;
    private final boolean limit;
    private final boolean decreaseCost;
    private final int slot;
    private final int decreasePercent;
    private final int limitAmount;
    private final int minCost;
    private final int maxCost;
    private final int minAmount;
    private final int maxAmount;
    private final int fixedCost;
    private final int fixedAmount;
}