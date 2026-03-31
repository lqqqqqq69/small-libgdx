package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.lqqqqqq69.component.Transform;
import io.github.lqqqqqq69.component.Visualbox;

public class VisualboxSystem extends EntitySystem {
    private ImmutableArray<Entity> visualboxes;
    private boolean anyOverlap = false;

    public VisualboxSystem(){
    }
    @Override
    public void addedToEngine(Engine engine) {
        visualboxes = engine.getEntitiesFor(Family.all(Visualbox.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity visualbox : visualboxes){
            anyOverlap = false;
            for (Entity othervisualbox : visualboxes){
                Transform transform = Transform.MAPPER.get(visualbox);
                Transform othertransform = Transform.MAPPER.get(othervisualbox);
                if (visualbox != othervisualbox && transform.getPosition().y < othertransform.getPosition().y){
                    Visualbox visualboxComponent = Visualbox.MAPPER.get(visualbox);
                    Visualbox othervisualboxComponent = Visualbox.MAPPER.get(othervisualbox);
                    if (Collision.overlaps(visualboxComponent, othervisualboxComponent)){
                        transform.setZ(othertransform.getZ() + 2);
                        anyOverlap = true;
                    }
                }
            }
            if (!anyOverlap){
                Transform transform = Transform.MAPPER.get(visualbox);
                transform.setZ(0);
            }
        }
    }

}
