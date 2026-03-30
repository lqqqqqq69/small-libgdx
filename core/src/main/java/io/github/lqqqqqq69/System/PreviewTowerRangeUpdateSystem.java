package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.github.lqqqqqq69.component.PreviewTowerRange;
import io.github.lqqqqqq69.component.Transform;

/**
 * PreviewTowerRangeUpdateSystem dient dafuer die Range von noch nicht platzierten Towern relativ zu Tranform-Position upzudaten
 */
public class PreviewTowerRangeUpdateSystem extends IteratingSystem {
    public PreviewTowerRangeUpdateSystem() {
        super(Family.all(PreviewTowerRange.class).get());

    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Transform transform = Transform.MAPPER.get(entity);
        
        PreviewTowerRange.MAPPER.get(entity).setCenter(transform.getPosition().x, transform.getPosition().y);
    }

}
