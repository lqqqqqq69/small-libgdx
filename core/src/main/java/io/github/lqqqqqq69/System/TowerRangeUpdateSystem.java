package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.lqqqqqq69.component.TowerRange;
import io.github.lqqqqqq69.component.Transform;
import io.github.lqqqqqq69.component.additionalTowerData;

/**
 * Das TowerRangeUpdateSystem updated die Schussreichweite der Tuerme/ Turmtruppen und passt sie den jeweiligen Offsets an
 */

public class TowerRangeUpdateSystem extends IteratingSystem {
    public TowerRangeUpdateSystem() {
        super(Family.all(TowerRange.class).get()); // System wird fuer alle Turmtruppen mit dem TowerRange Komponenten durchgefuehrt

    }
    /**
     * processEntity testet zu welcher Art Turm eine Range gehoert und passt die Mitte dieser Range dem jeweiligen Offset an
     * 
     * @param entity    aktuelle Entity (Turm)
     * @param deltaTime Zeit seit dem letzten Frame
     */
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Transform transform = Transform.MAPPER.get(entity);

        additionalTowerData data = additionalTowerData.MAPPER.get(entity);
        
        if(data.getTowerType().equals("Tower1") || data.getTowerType().equals("Tower2") || data.getTowerType().equals("Tower3")){
            TowerRange.MAPPER.get(entity).setCenter(transform.getPosition().x, transform.getPosition().y-Offset.ARCHER_OFFSET_Y);
        } else if (data.getTowerType().equals("CatapultTower")) {
            TowerRange.MAPPER.get(entity).setCenter(transform.getPosition().x, transform.getPosition().y-Offset.CATAPULT_OFFSET_Y);
        } else if(data.getTowerType().equals("WizardTower1")){
            TowerRange.MAPPER.get(entity).setCenter(transform.getPosition().x-Offset.WIZARD1_OFFSET_X, transform.getPosition().y-Offset.WIZARD1_OFFSET_Y);
        } else if(data.getTowerType().equals("WizardTower2")||data.getTowerType().equals("WizardTower3")){ 
            TowerRange.MAPPER.get(entity).setCenter(transform.getPosition().x-Offset.WIZARD2_OFFSET_X, transform.getPosition().y-Offset.WIZARD2_OFFSET_Y-Offset.WIZARD_TOWER2_TRANSFORM_Y);
        }
    }

}
