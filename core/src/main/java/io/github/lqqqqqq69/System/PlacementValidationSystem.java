package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.lqqqqqq69.component.Graphic;
import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Hover;
import io.github.lqqqqqq69.component.Move;
import io.github.lqqqqqq69.System.Collision;
import io.github.lqqqqqq69.component.PlacementValid;
import io.github.lqqqqqq69.component.additionalTowerData;

/**
 * PlacementValidationSystem prueft, ob ein Turm aktuell platzierbar ist
 */
public class PlacementValidationSystem extends EntitySystem {

    private ImmutableArray<Entity> towers;          // Noch nicht platzierte Tuerme
    private ImmutableArray<Entity> placedTowers;    // platzierte Tuerme
    private ImmutableArray<Entity> paths;           // Pfade
    private CashSystem cashSystem;

    public PlacementValidationSystem(CashSystem cashSystem){
        this.cashSystem = cashSystem;
    }

    /**
     * addedToEngine initialisiert die Gruppen an Entitaeten, mit welchen in Update gearbeitet wird (platzierte Tuerme, noch nicht platzierte Tuerme, Pfade)
     */
    @Override
    public void addedToEngine(Engine engine) {
        // Hitbox und Hover-Komponente = noch nich platzierte Tuerme
        towers = engine.getEntitiesFor(Family.all(Hitbox.class, Hover.class, Graphic.class).get());
        // Hitbox, aber ohne Hover-Komponente = platzierte Tuerme 
        placedTowers = engine.getEntitiesFor(Family.all(Hitbox.class).exclude(Hover.class, Move.class).get());
        // Hitbox, aber ohne Hover und Graphic-Komponente = Pfade
        paths = engine.getEntitiesFor(Family.all(Hitbox.class).exclude(Hover.class, Graphic.class).get());
    }

    /**
     * update prueft pro Frame, ob der Turm an der aktuellen Stelle platzierbar ist und ob man genug  Geld fuer die Platzierung besutzt
     * 
     * - isOnPath und isOnTower standardmaeßig false
     * - Pruefen, ob sich noch nicht platzierter Turm mit platziertem Turm ueberlappt 
     *  - Falls ja: isOnTower = true
     * 
     * - Pruefen, ob sich noch nicht platzierter Turm mit Pfad ueberlappt 
     *  - Falls ja: isOnPath = true
     * 
     * - Wenn ein Turm aktuell nicht platzierbar -> rot faerben
     * - sonst normale Faerbung
     */
    @Override
    public void update(float deltaTime) {

        for (Entity tower : towers) {
            boolean isOnPath = false;
            boolean isOnTower = false;

            for (Entity tower2 : placedTowers) {
                // Pruefen ob Tower sich ueberlappen
                if (Collision.overlaps(Hitbox.MAPPER.get(tower2), Hitbox.MAPPER.get(tower))) {
                    isOnTower = true;
                }
            }

            for (Entity path : paths) {
                // Pruefen ob Tower sich mit Weg ueberlappt
                if (Collision.overlaps(Hitbox.MAPPER.get(path), Hitbox.MAPPER.get(tower))) {
                    isOnPath = true;
                }
            }
            additionalTowerData data = additionalTowerData.MAPPER.get(tower);

            Graphic graphic = Graphic.MAPPER.get(tower);
            if (isOnPath || isOnTower || (data.getBuyCost() > cashSystem.getCashAmount())) {
               
                // Ungueltige Platzierung: rot
                graphic.setColor(1, 0, 0, 1);
                PlacementValid placementValid = PlacementValid.MAPPER.get(tower);
                placementValid.setValid(false);
               
            } else {
                // Gueltige Platzierung
                graphic.setColor(1, 1, 1, 1);
                PlacementValid placementValid = PlacementValid.MAPPER.get(tower);
                placementValid.setValid(true);
                
            }

        }
    }
}
