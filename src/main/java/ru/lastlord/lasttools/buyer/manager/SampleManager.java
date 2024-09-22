package ru.lastlord.lasttools.buyer.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import ru.lastlord.lasttools.buyer.LastBuyer;
import ru.lastlord.lasttools.buyer.configuration.Items;
import ru.lastlord.lasttools.buyer.item.Item;
import ru.lastlord.lasttools.buyer.item.Sample;
import ru.lastlord.lasttools.buyer.runnable.BuyerUpdateRunnable;

import java.util.*;

public class SampleManager {
    public final static LinkedList<Sample> samples = new LinkedList<>();

    public static Sample currentSample;

    public static void loadSamples() {
        if (!samples.isEmpty()) samples.clear();
        final FileConfiguration items = Items.getConfig();
        items.getConfigurationSection("samples").getKeys(false).forEach(sampleId -> {
            final Set<Item> sampleItems = new HashSet<>();
            items.getConfigurationSection("samples."+sampleId+".items").getKeys(false).forEach(itemId -> {
                final String path = "samples."+sampleId+".items."+itemId + ".";
                Item item = Item.builder()
                        .itemId(items.getString(path+"id"))
                        .material(Material.getMaterial(items.getString(path+"id").toUpperCase()))
                        .slot(items.getInt(path+"slot"))
                        .name(items.getString(path+"name"))
                        .lore(items.getStringList(path+"lore"))
                        .headDatabase(items.getBoolean(path+"headdatabase"))
                        .customName(items.getBoolean(path+"custom-name"))
                        .randomCost(items.getBoolean(path+"random-cost"))
                        .randomAmount(items.getBoolean(path+"random-amount"))
                        .limit(items.getBoolean(path+"limit"))
                        .decreaseCost(items.getBoolean(path+"decrease-cost"))
                        .limitAmount(items.getInt(path+"limit-amount"))
                        .decreasePercent(items.getInt(path+"decrease-percent"))
                        .minCost(items.getInt(path+"min-cost"))
                        .maxCost(items.getInt(path+"max-cost"))
                        .minAmount(items.getInt(path+"min-amount"))
                        .maxAmount(items.getInt(path+"max-amount"))
                        .fixedCost(items.getInt(path+"fixed-cost"))
                        .fixedAmount(items.getInt(path+"fixed-amount"))
                        .build();
                sampleItems.add(item);
            });
            final String path = "samples."+sampleId+".";
            Sample sample = Sample.builder()
                    .id(sampleId)
                    .title(items.getString(path+"title"))
                    .chance(items.getInt(path+"chance"))
                    .size(items.getInt(path+"size"))
                    .items(sampleItems).build();
            LastBuyer.instance.setupEconomy(sample, items.getString(path+"economy"));
            samples.add(sample);
        });
    }

    public static Sample getSample(Inventory inventory) {
        for (Sample sample : samples) if (sample.getInventory() == inventory) return sample;
        return null;
    }

    public static Sample getSampleById(String id) {
        for (Sample sample : samples) if (sample.getId().equalsIgnoreCase(id)) return sample;
        return null;
    }

    public static void updateSamples() {
        for (Sample sample : samples) {
            sample.setInventory(Bukkit.createInventory(null, sample.getSize(), sample.getTitle()));
            sample.initItems();
        }
    }

    public static Sample newSample() {
        if (BuyerUpdateRunnable.changeSamples.equalsIgnoreCase("random")) {
            final List<Sample> samples = new ArrayList<>(SampleManager.samples);
            final Iterator<Sample> iterator = samples.iterator();
            while (iterator.hasNext()) {
                Sample next = iterator.next();
                if (next == currentSample) {
                    samples.remove(next);
                    break;
                }
            }
            int totalChance = 0;
            for (Sample sample : samples) totalChance += sample.getChance();
            final int randomValue = new Random().nextInt(totalChance);
            int cumulativeChance = 0;
            for (Sample sample : samples) {
                int userChance = sample.getChance();
                cumulativeChance += userChance;
                if (randomValue < cumulativeChance) {
                    currentSample = sample;
                    return sample;
                }
            }
            return null;
        } else if (BuyerUpdateRunnable.changeSamples.equalsIgnoreCase("queue")) {
            currentSample = BuyerUpdateRunnable.queue.get(BuyerUpdateRunnable.getCount());
            return BuyerUpdateRunnable.queue.get(BuyerUpdateRunnable.getCount());
        }
        return null;
    }
}
