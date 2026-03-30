package io.github.lqqqqqq69;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * Defend beschreibt ein zu verteidigendes Objekt (im Fall des Spiels eine Tuer)
 */
public class Defend {
    public String name; // Name des Objekts (Tuer)
    public int health; // HP des Objekts
    
    // speichert die Daten der Verteidigungsobjekte in einer Map
    private static Map<String, Defend> loadedDefends = new HashMap<>();


    /**
     * loadDefends laedt die JSON-Datei und speichert sie in einer Map
     *  
     * @param path Name (Pfad) der JSON-Datei
     */
    public static void loadDefends(String path) {
        Json json = new Json();
        FileHandle file = Gdx.files.internal(path);

        loadedDefends = json.fromJson(HashMap.class, Defend.class, file);
        
    }

    public static Defend getType(String name) {
        return loadedDefends.get(name);
    }

    /**
     * createInstance erstelt Instanzen derzu verteidigende Objekte
     * 
     * @return neue Verteidifungsinstanz
     */
    public Defend createInstance() {
        Defend instance = new Defend();
        instance.name = this.name;
        instance.health = this.health;

        return instance;
    }
}
    