package ru.lastlord.lasttools.hologram.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.lastlord.lasttools.hologram.configuration.Holograms;
import ru.lastlord.lasttools.hologram.hologram.Hologram;
import ru.lastlord.lasttools.util.ChatUtil;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

public class HologramManager {

    public static LinkedList<Hologram> holograms;

    public static void loadHolograms() {
        holograms = new LinkedList<>();
        Holograms.loadConfig();
        for (String id : Holograms.getConfig().getConfigurationSection("holograms").getKeys(false)) {
            final LinkedList<String> lines = new LinkedList<>(Holograms.getConfig().getStringList("holograms." + id + ".lines"));
            final LinkedList<String> newList = new LinkedList<>();
            lines.forEach(line -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    line = ChatUtil.colorize(PlaceholderAPI.setPlaceholders(p, line));
                }
                newList.add(line);
            });
            final Hologram hologram = new Hologram(id, newList, Holograms.getConfig().getLocation("holograms." + id + ".location"));
            holograms.add(hologram);
            hologram.create();
        }
    }

    public static void updateHolograms(Player player) {
        for (String id : Holograms.getConfig().getConfigurationSection("holograms").getKeys(false)) {
            final LinkedList<String> lines = new LinkedList<>(Holograms.getConfig().getStringList("holograms." + id + ".lines"));
            final LinkedList<String> newList = new LinkedList<>();
            final Hologram hologram = getHologramById(id);
            lines.forEach(line -> {
                line = ChatUtil.colorize(PlaceholderAPI.setPlaceholders(player, line));
                newList.add(line);
            });
            updateHologram(id, newList, player);
        }
    }

    private static void updateHologram(String id, LinkedList<String> lines, Player player) {
        for (Hologram hologram : holograms) {
            if (hologram.getId().equalsIgnoreCase(id)) {
                int index = 0;
                ArmorStand[] armorStands = hologram.getArmorStands().toArray(new ArmorStand[0]);
                for (String line : lines) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        line = ChatUtil.colorize(PlaceholderAPI.setPlaceholders(p, line));
                    }
                    //armorStands[index].setCustomName(line);
                    makeArmorStandVisible(armorStands[index], player, line);
                    index++;
                }
            }
        }
    }

    public static void makeArmorStandVisible(ArmorStand armorStand, Player player, String line) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, armorStand.getEntityId());

        WrappedDataWatcher dataWatcher = new WrappedDataWatcher(armorStand);
        WrappedDataWatcher.WrappedDataWatcherObject customNameObject = new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)); // Индекс 2 - CustomName

        dataWatcher.setObject(customNameObject, Optional.of(WrappedChatComponent.fromText(line).getHandle()));

        packet.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Hologram getHologramById(String id) {
        for (Hologram hologram : holograms) if (hologram.getId().equalsIgnoreCase(id)) return hologram;
        return null;
    }

    public static void unloadHolograms() {
        Iterator<Hologram> hologramIterator = holograms.iterator();
        while (hologramIterator.hasNext()) {
            Hologram hologram = hologramIterator.next();
            hologram.getArmorStands().forEach(Entity::remove);
            hologramIterator.remove();
        }
    }

    public static void removeHologram(String id) {
        unloadHolograms();
        for (String i : Holograms.getConfig().getConfigurationSection("holograms").getKeys(false)) {
            if (i.equalsIgnoreCase(id)) {
                Holograms.getConfig().set("holograms." + i, null);
                Holograms.save();
                break;
            }
        }
        loadHolograms();
    }

    public static void createHologram(String id, Location location) {
        final Hologram hologram = new Hologram(id, new LinkedList<>(), location);
        Holograms.getConfig().set("holograms."+id+".lines", new LinkedList<>());
        Holograms.getConfig().set("holograms."+id+".location", location);
        Holograms.save();
        hologram.create();
    }
}
