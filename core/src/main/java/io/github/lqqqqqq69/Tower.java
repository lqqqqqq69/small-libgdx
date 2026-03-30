package io.github.lqqqqqq69;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * Tower beschreibt ein Turmobjekt und dessen Werte
 */
public class Tower {
    public String name;             // Name/ Art des Turms
    public String troopName;        // Name/ Art der Turmtruppe
    public int level;               // Level des Turms
    public int damage;              // Schaden der zugehoerigen Projektile
    public float range;             // Reichweite des Turms
    public float cooldown;          // Cooldown zwischen Angriffen

    public int buyCost;             // Kosten fuer den Kauf
    public int[] upgradeCost;       // Kosten fuer moegliche Upgrades
    public int totalCost;           // Gesamtkosten

    public boolean upgradable;      // Hat der Turm weitere Upgrades
    public String[] upgradeTo;      // Moegliche Upgrades
    public String effect;           // Effekt der zugehoerigen Projektile
    public String projectileType;   // Art der zugehoerigen Projektile 
    public float projectilespeed;   // Geschwindigkeit der Projektile

    private static Map<String, Tower> loadedTowers = new HashMap<>();

    /**
     * loadTowers laedt die Turmtypen aus einer JSON-Datei in eine Map
     * 
     * @param path Name/Pfad der JSON-Datei
     */
    public static void loadTowers(String path) {
        Json json = new Json();
        FileHandle file = Gdx.files.internal(path);

        loadedTowers = json.fromJson(HashMap.class, Tower.class, file);

    }

    /**
     * getType gibt einen Turmtyp anhand seines Namens zurueck
     * 
     * @param name Name des Turmtyps
     * @return Turmobjekt 
     */
    public static Tower getType(String name) {
        return loadedTowers.get(name);
    }

    /**
     * createInstance erstellt eine neue Tower-Instanz
     * 
     * @return neue Turminstanz
     */
    public Tower createInstance() {
        Tower instance = new Tower();
        instance.name = this.name;
        instance.troopName = this.troopName;
        instance.projectileType = this.projectileType;
        instance.level = this.level;
        instance.damage = this.damage;
        instance.range = this.range;
        instance.cooldown = this.cooldown;
        instance.buyCost = this.buyCost;
        instance.upgradeCost = this.upgradeCost;
        instance.totalCost = this.totalCost;
        instance.upgradable = this.upgradable;
        instance.upgradeTo = this.upgradeTo;
        instance.effect = this.effect;
        instance.projectilespeed = this.projectilespeed;
        return instance;
    }
}
