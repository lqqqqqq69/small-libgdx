package io.github.lqqqqqq69;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * Enemy beschreibt ein Gegnerobjekt und dessen Werte
 */
public class Enemy {
    public String name; // Name des Gegner bzw Gegnertyp
    public int hp;      // HP für Gegnertyp
    public float speed; // Bewegungsgeschwindigkeit
    public int damage;  // Shcaden
    public int reward;  // Belohnung bei besiegtem Gegner
    public int armor;   // Armor = ab welchem Schadenselevel nimmt ein Gegner den vollständigen Schade
    public boolean magic_resistance; // Ist ein Gegner resistent gegen Magieschaden
    //public List<String> allowed_modifiers;

    private static Map<String, Enemy> loadedEnemies = new HashMap<>();

    /**
     * loadEnemies lädt die Gegnertypen aus einer JSON-Datei in eine Map 
     * 
     * @param path Name/Pfad der JSON Datei
     */
    public static void loadEnemies(String path) {
        Json json = new Json();
        FileHandle file = Gdx.files.internal(path);

        loadedEnemies = json.fromJson(HashMap.class, Enemy.class, file);

    }

    public static Enemy getType(String name) {
        return loadedEnemies.get(name);
    }

    
    /**
     * createInstance erstelt Instanzen der Gegnertypen aus den Daten der JSON
     * 
     * @return neue Gegnerinstanz
     */
    public Enemy createInstance() {
        Enemy instance = new Enemy();
        instance.name = this.name;
        instance.hp = this.hp;
        instance.speed = this.speed;
        instance.damage = this.damage;
        instance.reward = this.reward;
        instance.armor = this.armor;
        instance.magic_resistance = this.magic_resistance;

        /* 
        // verworfene Logik zur Anwendung von Modifikatoren
        // Grund: die Erstellung von Assets für mehrere Varianten des gleichen Gegners wäre zu aufwändig gewesen

        if (!allowed_modifiers.isEmpty() && Math.random() < 0.25) {
            String chosenMod = allowed_modifiers.get((int) (Math.random() * allowed_modifiers.size()));
            applyModifier(instance, chosenMod);
        }
        */

        return instance;
    }

    /*
    // verworfene Logik zur Anwendung von Modifikatoren
    private void applyModifier(Enemy enemy, String modifier) {
        switch (modifier) {
            case "speed":
                enemy.speed *= 1.25f; // z.B. 25% schneller
                break;
            case "armor":
                enemy.armor = true;
                break;
            case "magic_resistance":
                enemy.magic_resistance = true;
                break;
        }
    }
    */

}
