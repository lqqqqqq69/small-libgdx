package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.lqqqqqq69.System.Collision;
import io.github.lqqqqqq69.AudioService.AudioService;
import io.github.lqqqqqq69.InputService.InputService;
import io.github.lqqqqqq69.asset.SoundAsset;
import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Hover;
import io.github.lqqqqqq69.component.additionalEnemyData;
import io.github.lqqqqqq69.component.additionalTowerData;

/**
 * UpgradeCollisionSystem testet, ob auf einen Turm geklickt wurde und verwaltet die Klicks
 */

public class UpgradeCollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> placedTowers;        // Array der platzierten Tuerme
    private final CreateMouseCollision mouseCollision;  // Maus
    private boolean selectTower = false;                // zeigt, ob ein Turm ausgewaehlt ist
    private Entity currentlySelected;                   // aktuell ausgewaehlter Turm
    private InputService inputService;             
    private AudioService audioService;                 

    public UpgradeCollisionSystem(CreateMouseCollision mouseCollision, InputService inputService, AudioService audioService) {
        this.mouseCollision = mouseCollision;
        this.currentlySelected = null;
        this.inputService = inputService;
        this.audioService = audioService;
    }

    @Override
    public void addedToEngine(Engine engine) {
        // Hitbox + additionalTowerData, aber kein Hover oder additionalEnemyData -> placed Tower
        placedTowers = engine.getEntitiesFor(Family.all(Hitbox.class, additionalTowerData.class).exclude(Hover.class, additionalEnemyData.class).get());
    }

    /**
     * update testet pro Frame, ob auf einen Turm geklickt wurde
     * 
     * - Mouse-Hitbox ermitteln
     * - Wurde geklickt? -> nein = return
     * - Pruefung, ob auf einen Turm geklickt wurde
     *  - Falls ja:
     *      - wenn der Turm bereits ausgewaehlt war -> Turmauswahl aufheben
     *      - wenn ein neuer Turm ausgewaehlt -> neuen Turm auswaehlen + Sound abspielen
     *  - Falls nein:
     *      - Turmauswahl aufheben
     * 
     * @param deltaTime Zeit seit dem letzten Frame
     * 
     */
    @Override
    public void update(float deltaTime) {
        Hitbox mouse = Hitbox.MAPPER.get(mouseCollision.getMouse());
  
        if (!inputService.MOUSE1) return; // kein Linksklick -> return

        Entity clickedTower = null;

        // Pruefen, ob auf einen Turm geklickt wurde
        for (Entity tower : placedTowers) {
            Hitbox towerHitbox = Hitbox.MAPPER.get(tower);
            if (Collision.overlaps(towerHitbox, mouse)) {
                clickedTower = tower;
                break;
            }
        }

        if (clickedTower != null) {
            // Klick auf einen Turm
            if (currentlySelected == clickedTower) {
                // Geklickter Turm schon ausgewaehlt -> Turm nicht mehr auswaehlen
                selectTower = false;
                currentlySelected = null;
            } else {
                // Neuen Turm auswaehlen
                selectTower = true;
                currentlySelected = clickedTower;
                audioService.playSound(SoundAsset.ARCHERTOWER_SELECT);
            }
        } else if (selectTower) {
            // Klick ins Leere -> Turmauswahl aufheben
            selectTower = false;
            currentlySelected = null;
        }

        
    }

    public boolean isSelectTower() {
        return selectTower;
    }

    public Entity getCurrentlySelected() {
        return currentlySelected;
    }
}
