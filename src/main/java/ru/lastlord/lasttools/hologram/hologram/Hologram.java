package ru.lastlord.lasttools.hologram.hologram;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.LinkedList;

@Getter
public class Hologram {

    private LinkedList<ArmorStand> armorStands;
    private LinkedList<String> lines;
    private Location location;
    private String id;

    public Hologram(String id, LinkedList<String> lines, Location location) {
        this.lines = lines;
        this.location = location;
        this.id = id;
        armorStands = new LinkedList<>();
    }

    public void create() {
        final Location loc = location.clone().add(0, lines.size() * 0.25, 0);
        for (String line : lines) {
            final ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            armorStand.setMarker(true);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setCustomName(line);
            armorStand.setCustomNameVisible(true);
            loc.subtract(0, 0.25, 0);
            armorStands.add(armorStand);
        }
    }

    public void remove() {
        armorStands.forEach(Entity::remove);
    }
}
