package io.github.lqqqqqq69.System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.github.lqqqqqq69.AudioService.AudioService;
import io.github.lqqqqqq69.Enemy;
import io.github.lqqqqqq69.InputService.InputService;
import io.github.lqqqqqq69.Main;
import io.github.lqqqqqq69.asset.AssetService;
import io.github.lqqqqqq69.asset.AtlasAsset;
import io.github.lqqqqqq69.asset.MusicAsset;
import io.github.lqqqqqq69.asset.SoundAsset;
import io.github.lqqqqqq69.component.Animation2D;
import io.github.lqqqqqq69.component.Damage;
import io.github.lqqqqqq69.component.Facing;
import io.github.lqqqqqq69.component.Graphic;
import io.github.lqqqqqq69.component.Health;
import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Move;
import io.github.lqqqqqq69.component.Transform;
import io.github.lqqqqqq69.component.additionalEnemyData;

/**
 * Das WaveSystem erstellt Wellen und verwaltet diese
 */

public class WaveSystem extends EntitySystem {
    private float spawntime;                    // Zeit zwischen Gegner-Spawns
    private int waveNr;                         // aktuelle Wellennummer
    private final Engine engine; 
    private final AssetService assetService;    // AssetService des Spiels
    private final InputService inputService;    // InputService des Spiels
    private final Main game;
    private final AudioService audioService;    // AudioSevice des Spiels
    private int counter;                        // Zaehler fuer die Anzahl der gespawnten Gegner eines Spawn-Entrys
    private boolean isWaveActive = false;       // ist ein Welle aktiv
    private final ArrayList<Entity> activeEnemies = new ArrayList<>(); // aktuelle lebende Gegner
    private final Map<Integer, ArrayList<SpawnEntry>> levelMap = new HashMap<>(); // Level und deren Wellen
    private ArrayList<Vector2> spawnPosition = new ArrayList<>(); // moegliche Spawn-Positionen
    private boolean delayActive = true;         // ist das Delay zwischen den Spawn-Entries aktiv
    private float random;                       // zufaelliges Spawn-Intervall
    private int maxWaveNr;                      // maximale Wellenanzahl eines Levels
    private boolean levelFinished = false;      // ist das Level fertig 


    public WaveSystem(Engine engine, AssetService assetService, InputService inputService, int level, Main game, AudioService audioService) {
        this.spawntime = 0;
        this.waveNr = 1;
        this.counter = 0;
        this.assetService = assetService;
        this.inputService = inputService;
        this.engine = engine;
        this.game = game;
        this.audioService = audioService;
    

        waveSystemLevelSetup(level);
    }

    /**
     * update startet die Wellen und spawnt die Gegner
     * 
     * - Wenn Leertaste gedrueckt -> Test: ist die Welle aktiv?
     *  - Falls nein:
     *      - Start der Welle + Ausgabe "Wave started"
     *  - Falls ja: 
     *      - Ausgabe "Wave already active"
     * 
     * - Wenn Welle aktiv:
     *  - Ist der Welleneintrag leer und existieren keine Gegner mehr?
     *  - Falls nein:
     *      - Wenn Delay aktiv und Spawntime groesser gleich Delay des Spawn-Entrys
     *          - Erstellung eines neuen Gegners (des aktuellen Spawn-Eintrags) an einer der moeglichen Spawn-Positionen
     *          - Hinzufuegen des Gegners zur Engine
     *          - Hinzufuegen des Gegners zur Liste der aktiven Gegner
     *          - Zuruecksetzung Spawntime
     *          - Delay inaktiv setzen
     *          - Gegnerzaehler des Eintrags + 1
     *          - zufaellige Auswahl in welchem Abstand der naechste Gegner
     * 
     *      - Sonst, wenn Delay inaktiv und Spawntime groesser gleich zufaellig generiertes Spawn-Intervall und noch nicht alle Gegner des Eintrags gespawnt
     *          - Erstellung eines neuen Gegners (des aktuellen Spawn-Eintrags) an einer der moeglichen Spawn-Positionen
     *          - Hinzufuegen des Gegners zur Engine
     *          - Hinzufuegen des Gegners zur Liste der aktiven Gegner
     *          - zufaellige Auswahl in welchem Abstand der naechste Gegner
     *          - Zuruecksetzung Spawntime
     *          - Gegnerzaehler fuer den Spawn-Eintrag + 1
     * 
     *      - Sonst, wenn alle Gegner des Spawn-Eintrags gespawnt
     *          - Loeschen des aktuellen Spawn-Eintrags (fuer den aktuellen Versuch) in der aktuellen Welle
     *          - Zuruecksetzen Gegnerzaehler
     *          - Aktivieren Delay
     *  
     *  - Falls ja: 
     *      - Zuruecksetzen Gegnerzaehler, Spawntime, random, Waveactive, Delayactive
     *      - Wellenzaehler + 1
     *      - Ausgabe "Wave ended"
     *      
     *      - Falls maximale Anzahl an Wellen ueberschritten
     *          - Abspielen "Win-Sound"
     *          - isLevel Finishe true
     * 
     * @param deltaTime Zeit seit dem letzten Frame
     */

    @Override
    public void update(float deltaTime) {

        // Start der Welle falls nicht schon aktiv
        if (inputService.KEY_SPACE) {
            if (!isWaveActive) {
                isWaveActive = true;
                System.out.println("Wave started");
            } else {
                System.out.println("Wave already active");
            }
        }

        if (isWaveActive) {
            if (!levelMap.get(waveNr).isEmpty()) {
                // Welle aktiv und noch nicht alle Gegner des Spawn-Eintrags gespawnt

                if (delayActive && spawntime >= levelMap.get(waveNr).get(0).getDelay()) {

                    // Spawn Gegner
                    Entity newEnemy = createEnemy(spawnPosition.get((int)(Math.random() * spawnPosition.size())), levelMap.get(waveNr).get(0).getEnemyType());
                    engine.addEntity(newEnemy);
                    activeEnemies.add(newEnemy);

                    spawntime = 0;
                    delayActive = false;
                    counter++;
                    random = MathUtils.random(
                        levelMap.get(waveNr).get(0).getIntervalMin(),
                        levelMap.get(waveNr).get(0).getIntervalMax()
                    );

                } else if (!delayActive && spawntime >= random && counter < levelMap.get(waveNr).get(0).getCount()) {

                    // Spawn Gegner
                    Entity newEnemy = createEnemy(spawnPosition.get((int)(Math.random() * spawnPosition.size())), levelMap.get(waveNr).get(0).getEnemyType());
                    engine.addEntity(newEnemy);
                    activeEnemies.add(newEnemy);

                    random = MathUtils.random(
                        levelMap.get(waveNr).get(0).getIntervalMin(),
                        levelMap.get(waveNr).get(0).getIntervalMax()
                    );

                    spawntime = 0;
                    counter++;

                } else if (counter >= levelMap.get(waveNr).get(0).getCount()) {
                    // Wechsel zum naechsten Spawn-Eintrag der Welle
                    levelMap.get(waveNr).remove(0);
                    counter = 0;
                    delayActive = true;
                }

            } else if (activeEnemies.isEmpty() && levelMap.get(waveNr).isEmpty()) {
                // Wellenwechsel
                spawntime = 0;
                counter = 0;
                waveNr++;
                random = 0;

                delayActive = true;
                isWaveActive = false;
                System.out.println("Wave ended");
                if (waveNr > maxWaveNr){
                    levelFinished = true;
                    audioService.playSound(SoundAsset.LEVEL_WIN);

                }
            }

            spawntime += deltaTime;
        }
    }

    /**
     * createEnemy erstellt eine Entity und fuegt dieser die noetigen Gegnerkomponenten hinzu.
     * 
     * - Erzeugen einer neuen Entity
     * - Laden der Gegnerdaten aus der JSON-Datei
     * - Hinzufuegen Komponenten: Graphic, Move (inklusive Direction), Damage, Health, additionalEnemyData, Facing
     * - Bestimmen gegnerspezifischer Werte im Hinblick auf Erscheinung des zu Ladenden Gegners (Hitbox, Spritegroeße, Animationsgeschwindigkeit)
     * - Hinzufuegen gegnerspezifischer Werte
     * 
     * @param position  Spawn-Position des Gegners
     * @param enemyType Art des Gegners
     * @return          Gegnerentitaet mit angehaengten Komponenten
     * 
     */

    public Entity createEnemy(Vector2 position, String enemyType) {
        Entity entity = this.engine.createEntity();

        Enemy enemyData = Enemy.getType(enemyType).createInstance();
        TextureRegion region = this.assetService.get(AtlasAsset.OBJECTS).findRegion(enemyType + "/w_right");

        entity.add(new Graphic(region, Color.WHITE.cpy()));

        Move move = new Move(enemyData.speed);
        move.setDirection(new Vector2(1, 0));
        entity.add(move);

        entity.add(new Damage(enemyData.damage));

        entity.add(new Health(enemyData.hp, enemyData.armor, enemyData.magic_resistance));
        
        entity.add(new additionalEnemyData(enemyData.name, enemyData.reward));
        entity.add(new Facing(Facing.FacingDirection.RIGHT));  
        
        Vector2 size;
        float hitBoxWidth;
        float hitBoxHeight;
        float speed;

        // Anpassung der Groeße des Sprites der Gegner, deren Hitbox und deren Geschwindigkeit
        switch (enemyType){
            case "Lizard": 
                size = new Vector2(64, 64);
                hitBoxWidth = 1.25f;
                hitBoxHeight = 1.25f;
                speed = 1f;
                break;

            case "GoblinRider":
                size = new Vector2(64, 64);
                hitBoxWidth = 1.20f;
                hitBoxHeight = 1.20f;
                speed = 1f;
                break;

            case "Golem":
                size = new Vector2(96, 64);
                hitBoxWidth = 2f;
                hitBoxHeight = 2f;
                game.getAudioService().playMusic(MusicAsset.FINALBOSSMUSIC); // Abspielen spezieller Spawn-Sound + Musik fuer den Gegner
                audioService.playSound(SoundAsset.GOLEM_SPAWN);
                speed = 0.75f;
                break;

            case "Seagull":
                size = new Vector2(48, 48);
                hitBoxWidth = 1.2f;
                hitBoxHeight = 1.2f;
                speed = 1f;
                break;

            default:
                size = new Vector2(48, 48);
                hitBoxWidth = 1f;
                hitBoxHeight = 1f;
                speed = 1f;
                break;

        }

        entity.add(new Transform(
            position.cpy().scl(Main.UNIT_SCALE),
            1,
            size.scl(Main.UNIT_SCALE),
            new Vector2(1, 1),
            0f,
            0
        ));
        
        entity.add(new Animation2D(
            AtlasAsset.OBJECTS,
            enemyType,
            Animation2D.AnimationType.WALK,
            Animation.PlayMode.LOOP,
            speed
        ));

        
        entity.add(new Hitbox(
            position.cpy().scl(Main.UNIT_SCALE),
            hitBoxWidth,
            hitBoxHeight,
            Hitbox.BoxType.ENEMY
        ));

        return entity;
    }

    /**
     * waveSystemLevelSetup erstellt die einzelnen Wellen pro Level
     * 
     * - Bestimmen des Levels
     * - Anzahl der Wellen
     * - Festlegen der moeglichen Spawnpositionen
     * - Hinzufuegen der Wellen und deren Inhalt
     * 
     * @param level ausgewaehltes Level
     */

    public void waveSystemLevelSetup(int level) {
        if (level == 1) {
            maxWaveNr = 5;

            spawnPosition.add(new Vector2(-0.5f / Main.UNIT_SCALE, 10.7f / Main.UNIT_SCALE));
        
            levelMap.put(1, new ArrayList<>());
            levelMap.get(1).add(new SpawnEntry("Slime", 3, 4, 0.5f, 1f, 1.3f));

            levelMap.put(2, new ArrayList<>());
            levelMap.get(2).add(new SpawnEntry("Slime", 5, 5, 0.5f, 0.7f, 0.9f));

            levelMap.put(3, new ArrayList<>());
            levelMap.get(3).add(new SpawnEntry("Slime", 4, 4, 0.5f, 0.5f, 1f));
            levelMap.get(3).add(new SpawnEntry("Goblin", 1, 1, 0f, 1.1f, 1.3f));
            levelMap.get(3).add(new SpawnEntry("Slime", 2, 2, 0.2f, 0.5f, 1f));
            
            levelMap.put(4, new ArrayList<>());
            levelMap.get(4).add(new SpawnEntry("Slime", 2, 2, 0.5f, 0.3f, 0.6f));
            levelMap.get(4).add(new SpawnEntry("Goblin", 1, 1, 1f, 0.7f, 1.2f));
            levelMap.get(4).add(new SpawnEntry("Slime", 4, 4, 1f, 0.4f, 0.6f));
            levelMap.get(4).add(new SpawnEntry("Goblin", 1, 1, 1f, 0, 0));

            levelMap.put(5, new ArrayList<>());
            levelMap.get(5).add(new SpawnEntry("Slime", 4, 4, 0.5f, 0.2f, 0.6f));
            levelMap.get(5).add(new SpawnEntry("Goblin", 2, 2, 1f, 1.1f, 1.3f));
            levelMap.get(5).add(new SpawnEntry("Slime", 2, 2, 0.5f, 0.2f, 0.6f));
            levelMap.get(5).add(new SpawnEntry("Wolf", 1, 1, 3f, 0, 0));

        } 
        if (level == 2){
            maxWaveNr = 10;

            spawnPosition.add(new Vector2(-0.5f / Main.UNIT_SCALE, 10.7f / Main.UNIT_SCALE));
            spawnPosition.add(new Vector2(10.5f / Main.UNIT_SCALE, 20.5f / Main.UNIT_SCALE));

            levelMap.put(1, new ArrayList<>());
            levelMap.get(1).add(new SpawnEntry("Slime", 7, 7, 0.5f, 0.5f, 1f));

            levelMap.put(2, new ArrayList<>());
            levelMap.get(2).add(new SpawnEntry("Slime", 5, 5, 0.5f, 0.5f, 1f));
            levelMap.get(2).add(new SpawnEntry("Goblin", 1, 1, 0f, 1.1f, 1.3f));
            levelMap.get(2).add(new SpawnEntry("Slime", 2, 2, 0.2f, 0.5f, 1f));
            
            levelMap.put(3, new ArrayList<>());
            levelMap.get(3).add(new SpawnEntry("Slime", 2, 2, 0.5f, 0.3f, 0.6f));
            levelMap.get(3).add(new SpawnEntry("Goblin", 1, 1, 1f, 0.7f, 1.2f));
            levelMap.get(3).add(new SpawnEntry("Slime", 4, 4, 1f, 0.4f, 0.6f));
            levelMap.get(3).add(new SpawnEntry("Goblin", 1, 1, 1f, 0, 0));
            levelMap.get(3).add(new SpawnEntry("Wolf", 1, 1, 2f, 0, 0));
            levelMap.get(3).add(new SpawnEntry("Slime", 6, 8, 0.2f, 0.2f, 0.3f));
            levelMap.get(3).add(new SpawnEntry("Wolf", 1, 2, 1.8f, 0.4f, 0.6f));

            levelMap.put(4, new ArrayList<>());
            levelMap.get(4).add(new SpawnEntry("Goblin", 5, 6, 0.5f, 0.3f, 0.5f));
            levelMap.get(4).add(new SpawnEntry("Wolf", 1, 1, 1.5f, 0f, 0f));
            levelMap.get(4).add(new SpawnEntry("Slime", 2, 2, 0.5f, 0.2f, 0.6f));
            levelMap.get(4).add(new SpawnEntry("Wolf", 2, 2, 3f, 1.2f, 1.5f));
            levelMap.get(4).add(new SpawnEntry("Goblin", 8, 10, 0.5f, 0.3f, 0.5f));
            levelMap.get(4).add(new SpawnEntry("Slime", 4, 6, 1.2f, 0.2f, 0.4f));
            levelMap.get(4).add(new SpawnEntry("Wolf", 2, 2, 2.0f, 0.6f, 0.9f));
            

            levelMap.put(5, new ArrayList<>());
            levelMap.get(5).add(new SpawnEntry("Slime", 10, 10, 0.5f, 0.2f, 0.6f));

            levelMap.put(6, new ArrayList<>());
            levelMap.get(6).add(new SpawnEntry("Slime", 1, 2, 0.5f, 0.2f, 0.6f));
            levelMap.get(6).add(new SpawnEntry("Goblin", 5, 5, 1f, 0.5f, 0.7f));
            levelMap.get(6).add(new SpawnEntry("Wolf", 3, 4, 1.5f, 0.1f, 0.4f));
            levelMap.get(6).add(new SpawnEntry("Goblin", 4, 5, 1f, 0.6f, 0.9f));
            levelMap.get(6).add(new SpawnEntry("Wolf", 2, 2, 0.5f, 0.6f, 1.3f));
            levelMap.get(6).add(new SpawnEntry("Slime", 2, 2, 1f, 1.1f, 1.3f));
            levelMap.get(6).add(new SpawnEntry("Goblin", 7, 9, 0.2f, 0.2f, 0.3f));
            levelMap.get(6).add(new SpawnEntry("Wolf", 3, 4, 0.8f, 0.3f, 0.4f));
            levelMap.get(6).add(new SpawnEntry("Slime", 6, 8, 1.6f, 0.2f, 0.3f));

            levelMap.put(7, new ArrayList<>());
            levelMap.get(7).add(new SpawnEntry("Slime", 3, 4, 0.5f, 0.2f, 0.6f));
            levelMap.get(7).add(new SpawnEntry("Lizard", 2, 2, 0.5f, 0.8f, 1.2f));
            levelMap.get(7).add(new SpawnEntry("Goblin", 2, 2, 1.5f, 0.7f, 1.1f));
            levelMap.get(7).add(new SpawnEntry("Goblin", 5, 6, 0.5f, 0.2f, 0.3f));
            levelMap.get(7).add(new SpawnEntry("Lizard", 2, 2, 1.6f, 0.3f, 0.5f)); 
            levelMap.get(7).add(new SpawnEntry("Slime", 2, 2, 1f, 1.1f, 1.3f));
            levelMap.get(7).add(new SpawnEntry("Goblin", 7, 9, 0.2f, 0.2f, 0.3f));
            levelMap.get(7).add(new SpawnEntry("Wolf", 3, 4, 0.8f, 0.3f, 0.4f));
            levelMap.get(7).add(new SpawnEntry("Slime", 3, 4, 1.6f, 0.2f, 0.3f));
            levelMap.get(7).add(new SpawnEntry("Goblin", 4, 4, 1f, 0.5f, 0.7f));
            levelMap.get(7).add(new SpawnEntry("Wolf", 3, 4, 1.5f, 0.1f, 0.4f));
            levelMap.get(7).add(new SpawnEntry("Goblin", 4, 5, 1f, 0.8f, 1.3f));

            
            levelMap.put(8, new ArrayList<>());
            levelMap.get(8).add(new SpawnEntry("Lizard", 5, 5, 0.5f, 0.4f, 0.7f));
            levelMap.get(8).add(new SpawnEntry("Wolf", 3, 5, 1.5f, 0.5f, 1.0f));
            levelMap.get(8).add(new SpawnEntry("Goblin", 5, 6, 0.5f, 0.6f, 0.9f));
            levelMap.get(8).add(new SpawnEntry("Slime", 2, 2, 1.0f, 0.3f, 0.6f));
            levelMap.get(8).add(new SpawnEntry("Lizard", 5, 6, 0.2f, 0.2f, 0.4f));
            levelMap.get(8).add(new SpawnEntry("Wolf", 4, 5, 0.9f, 0.2f, 0.5f));
            levelMap.get(8).add(new SpawnEntry("Goblin", 6, 7, 1.6f, 0.2f, 0.3f));


            levelMap.put(9, new ArrayList<>());
            levelMap.get(9).add(new SpawnEntry("Goblin", 3, 3, 0.5f, 0.5f, 0.9f));
            levelMap.get(9).add(new SpawnEntry("Lizard", 6, 6, 1.0f, 0.6f, 1.0f));
            levelMap.get(9).add(new SpawnEntry("Wolf", 3, 3, 2.0f, 0.6f, 0.9f));
            levelMap.get(9).add(new SpawnEntry("Goblin", 8, 9, 0.2f, 0.18f, 0.3f));
            levelMap.get(9).add(new SpawnEntry("Lizard", 6, 6, 1.0f, 0.3f, 0.45f));
            levelMap.get(9).add(new SpawnEntry("Wolf", 4, 4, 0.5f, 0.35f, 0.5f));
            levelMap.get(9).add(new SpawnEntry("GoblinRider", 2, 2, 3.0f, 1f, 1f));

            levelMap.put(10, new ArrayList<>());
            levelMap.get(10).add(new SpawnEntry("Slime", 6, 6, 0.5f, 0.2f, 0.6f));
            levelMap.get(10).add(new SpawnEntry("Goblin", 5, 5, 1.0f, 0.6f, 1.0f));
            levelMap.get(10).add(new SpawnEntry("Lizard", 6, 6, 1.2f, 0.8f, 1.2f));
            levelMap.get(10).add(new SpawnEntry("Wolf", 2, 2, 2.0f, 0.6f, 1.1f));
            levelMap.get(10).add(new SpawnEntry("GoblinRider", 2, 2, 3.0f, 1f, 1f));
            levelMap.get(10).add(new SpawnEntry("Slime", 10, 12, 0.15f, 0.1f, 0.2f));
            levelMap.get(10).add(new SpawnEntry("Goblin", 8, 9, 0.9f, 0.2f, 0.3f));
            levelMap.get(10).add(new SpawnEntry("Lizard", 6, 7, 1.8f, 0.2f, 0.4f));
            levelMap.get(10).add(new SpawnEntry("Wolf", 3, 4, 2.5f, 0.3f, 0.5f));
            levelMap.get(10).add(new SpawnEntry("GoblinRider", 2, 3, 3f, 0.4f, 0.6f));
        }

        if (level == 3) {
            maxWaveNr = 13;

            spawnPosition.add(new Vector2(-0.5f / Main.UNIT_SCALE, 3.7f / Main.UNIT_SCALE));
            spawnPosition.add(new Vector2(-0.5f / Main.UNIT_SCALE, 6.7f / Main.UNIT_SCALE));
        

            levelMap.put(1, new ArrayList<>());
            levelMap.get(1).add(new SpawnEntry("Goblin", 1, 2, 0.5f, 0.9f, 1.2f));
            levelMap.get(1).add(new SpawnEntry("Slime", 3, 4, 0.5f, 0.2f, 0.5f));
            levelMap.get(1).add(new SpawnEntry("Goblin", 2, 2, 2.0f, 0.8f, 1.1f));


            levelMap.put(2, new ArrayList<>());
            levelMap.get(2).add(new SpawnEntry("Slime", 3, 3, 0.5f, 0.5f, 0.8f));
            levelMap.get(2).add(new SpawnEntry("Lizard", 1, 1, 0f, 1.2f, 1.5f));
            levelMap.get(2).add(new SpawnEntry("Goblin", 2, 2, 2.5f, 0.7f, 1.0f));
            levelMap.get(2).add(new SpawnEntry("Wolf", 1, 1, 5f, 0.8f, 1.0f));

            levelMap.put(3, new ArrayList<>());
            levelMap.get(3).add(new SpawnEntry("Goblin", 1, 2, 0.5f, 0.8f, 1.0f));
            levelMap.get(3).add(new SpawnEntry("Goblin", 2, 2, 1.5f, 0.7f, 0.9f));
            levelMap.get(3).add(new SpawnEntry("Lizard", 2, 2, 2.0f, 1.1f, 1.4f));
            levelMap.get(3).add(new SpawnEntry("Slime", 6, 6, 0.5f, 0.25f, 0.4f));

            levelMap.put(4, new ArrayList<>());
            levelMap.get(4).add(new SpawnEntry("Goblin", 1, 2, 0.5f, 0.7f, 0.9f));
            levelMap.get(4).add(new SpawnEntry("Lizard", 1, 1, 1.6f, 1.1f, 1.3f));
            levelMap.get(4).add(new SpawnEntry("Goblin", 2, 2, 0.8f, 0.7f, 0.9f));
            levelMap.get(4).add(new SpawnEntry("Lizard", 2, 2, 2.0f, 1f, 1.4f));
            levelMap.get(4).add(new SpawnEntry("Wolf", 3, 3, 8.5f, 0.8f, 1.0f));

            levelMap.put(5, new ArrayList<>());
            levelMap.get(5).add(new SpawnEntry("Wolf", 1, 1, 0.5f, 0.7f, 0.9f));
            levelMap.get(5).add(new SpawnEntry("Goblin", 3, 5, 0.5f, 0.5f, 0.7f));
            levelMap.get(5).add(new SpawnEntry("Lizard", 1, 2, 1.2f, 1.2f, 1.5f));
            levelMap.get(5).add(new SpawnEntry("Goblin", 5, 5, 0f, 0.2f, 0.3f));
            levelMap.get(5).add(new SpawnEntry("Slime", 3, 4, 0.5f, 0.2f, 0.4f));
            levelMap.get(5).add(new SpawnEntry("Lizard", 2, 2, 2f, 0.8f, 1.2f));
            levelMap.get(5).add(new SpawnEntry("GoblinRider", 1, 1, 1f, 1.0f, 1.2f));

            levelMap.put(6, new ArrayList<>());
            levelMap.get(6).add(new SpawnEntry("Slime", 5, 7, 0.5f, 0.2f, 0.4f));
            levelMap.get(6).add(new SpawnEntry("Goblin", 2, 2, 0.5f, 0.7f, 0.9f));
            levelMap.get(6).add(new SpawnEntry("Lizard", 2, 2, 1.8f, 1.2f, 1.4f));
            levelMap.get(6).add(new SpawnEntry("Goblin", 2, 2, 1f, 0.6f, 0.9f));
            levelMap.get(6).add(new SpawnEntry("Lizard", 3, 3, 2f, 1.1f, 1.4f));
            levelMap.get(6).add(new SpawnEntry("GoblinRider", 2, 2, 1f, 1.0f, 1.2f));
            levelMap.get(6).add(new SpawnEntry("Wolf", 3, 3, 2f, 0.8f, 1.0f));
            levelMap.get(6).add(new SpawnEntry("Seagull", 1, 1, 3f, 1.0f, 1.2f));

            levelMap.put(7, new ArrayList<>());
            levelMap.get(7).add(new SpawnEntry("Lizard", 7, 8, 0.5f, 0.3f, 0.7f));
            levelMap.get(7).add(new SpawnEntry("Seagull", 3, 4, 1.5f, 1.0f, 1.2f));
            

            levelMap.put(8, new ArrayList<>());
            levelMap.get(8).add(new SpawnEntry("Slime", 7, 8, 0.5f, 0.3f, 0.5f));
            levelMap.get(8).add(new SpawnEntry("Lizard", 3, 3, 0.8f, 1.2f, 1.4f));
            levelMap.get(8).add(new SpawnEntry("GoblinRider", 2, 2, 1f, 1.0f, 1.2f));
            levelMap.get(8).add(new SpawnEntry("Seagull", 2, 2, 3f, 0.5f, 1.2f));
            levelMap.get(8).add(new SpawnEntry("Goblin", 2, 2, 2f, 0.6f, 0.9f));
            levelMap.get(8).add(new SpawnEntry("Lizard", 3, 3, 1f, 0.9f, 1.4f));
            levelMap.get(8).add(new SpawnEntry("Seagull", 1, 1, 3f, 0.5f, 1.2f));

            levelMap.put(9, new ArrayList<>());
            levelMap.get(9).add(new SpawnEntry("Goblin", 3, 5, 0.5f, 0.6f, 0.9f));

            levelMap.put(10, new ArrayList<>());
            levelMap.get(10).add(new SpawnEntry("Lizard", 5, 6, 0.5f, 0.7f, 1.0f));
            levelMap.get(10).add(new SpawnEntry("GoblinRider", 3, 3, 1.0f, 0.6f, 0.8f));
            levelMap.get(10).add(new SpawnEntry("Goblin", 7, 8, 1.5f, 0.2f, 0.5f));
            levelMap.get(10).add(new SpawnEntry("Lizard", 2, 3, 0f, 0.7f, 1.0f));
            levelMap.get(10).add(new SpawnEntry("Seagull", 2, 2, 1f, 0.1f, 0.5f));
            levelMap.get(10).add(new SpawnEntry("GoblinRider", 3, 3, 1.0f, 0.6f, 0.8f));
            levelMap.get(10).add(new SpawnEntry("Goblin", 3, 3, 1.5f, 0.2f, 0.5f));
            levelMap.get(10).add(new SpawnEntry("GoblinRider", 3, 3, 1.0f, 0.1f, 0.3f));

            levelMap.put(11, new ArrayList<>());
            levelMap.get(11).add(new SpawnEntry("Seagull", 6, 8, 0.5f, 0.2f, 0.4f));
            levelMap.get(11).add(new SpawnEntry("GoblinRider", 3, 3, 1.0f, 0.1f, 0.3f));
            levelMap.get(11).add(new SpawnEntry("Seagull", 3, 5, 0.5f, 0.3f, 0.5f));
            levelMap.get(11).add(new SpawnEntry("GoblinRider", 5, 5, 1.0f, 0.1f, 0.3f));
           

            levelMap.put(12, new ArrayList<>());
            levelMap.get(12).add(new SpawnEntry("Goblin", 3, 3, 0.5f, 0.2f, 0.5f));
            levelMap.get(12).add(new SpawnEntry("Lizard", 3, 3, 0.1f, 0.5f, 0.7f));
            levelMap.get(12).add(new SpawnEntry("GoblinRider", 3, 3, 1.0f, 0.3f, 0.5f));
            levelMap.get(12).add(new SpawnEntry("Goblin", 3, 3, 0.5f, 0.2f, 0.5f));
            levelMap.get(12).add(new SpawnEntry("Lizard", 4, 4, 1f, 0.2f, 0.4f));


            levelMap.put(13, new ArrayList<>());
            levelMap.get(13).add(new SpawnEntry("Golem", 1, 1, 0.5f, 0f, 0f));
        }


    }


    public ArrayList<Entity> getActiveEnemies() {
        return activeEnemies;
    }

    public boolean isLevelFinished() {
        return levelFinished;
    }
    
    public boolean isWaveActive(){
        return isWaveActive;
    }

    public void setIsWaveActive(boolean isWaveActive) {
        this.isWaveActive = isWaveActive;
    }

    public int getWaveNr() {
        return waveNr;
    }

    public int getMaxWaveNr() {
        return maxWaveNr;
    }
}
