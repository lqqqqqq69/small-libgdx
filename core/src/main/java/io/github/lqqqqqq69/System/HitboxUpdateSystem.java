package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Hitbox.BoxType;
import io.github.lqqqqqq69.component.Transform;
import io.github.lqqqqqq69.component.additionalTowerData;

/**
 * HitboxUpdateSystem aktualisiert die Position einer Entitaet relativ zur Position der Transform-Komponente (gegebenfalls mit Einberechnung eines Offsets)
 */
public class HitboxUpdateSystem extends IteratingSystem {
    public HitboxUpdateSystem() {
        super(Family.all(Hitbox.class, Transform.class).get());
    }
    
    /**
     * processEntity aktualisiert die Hitboxen der Entitaeten pro Frame
     * 
     * - Falls die Entitaet ein Turm ist -> Setzung Hitbox auf Transform-Position + Einberechnung Offset
     * - Sonst: Setzung Hitbox auf Transform-Position
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Transform transform = Transform.MAPPER.get(entity);
        Hitbox hitbox = Hitbox.MAPPER.get(entity);
        BoxType boxType = hitbox.getBoxType();
        additionalTowerData towerData = additionalTowerData.MAPPER.get(entity);

        if (boxType == BoxType.TOWER) {
            if (towerData.getTowerType().equals("Tower1") || towerData.getTowerType().equals("Tower2") || towerData.getTowerType().equals("Tower3")) {
                hitbox.setCenter(transform.getPosition().cpy().add(0, Offset.TOWER1_HITBOX_Y));
            } 
            if (towerData.getTowerType().equals("CatapultTower")) {
                hitbox.setCenter(transform.getPosition().cpy().add(0, Offset.CATAPULT_TOWER_HITBOX_Y));
            }
            if (towerData.getTowerType().equals("WizardTower1")) {
                hitbox.setCenter(transform.getPosition().cpy().add(0, Offset.WIZARD_TOWER1_HITBOX_Y));
            }
            if (towerData.getTowerType().equals("WizardTower2") || towerData.getTowerType().equals("WizardTower3")) {
                hitbox.setCenter(transform.getPosition().cpy().add(0, Offset.WIZARD_TOWER2_HITBOX_Y));
            }
        } else {
            hitbox.setCenter(transform.getPosition());
        }
    }
}
