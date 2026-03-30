package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import io.github.lqqqqqq69.component.Facing;
import io.github.lqqqqqq69.component.Facing.FacingDirection;
import io.github.lqqqqqq69.component.Move;

/**
 * FacingSytstem passt die Drehung einer Entitaet entsprechen der Bewegungsrichtung an (falls sich die Entitaet bewegt)
 */
public class FacingSystem extends IteratingSystem {
    public FacingSystem() {
        super(Family.all(Facing.class, Move.class).get());
    }

    /**
     * processEntity passt die Blickrichtung (Drehung) einer Enitaet an die Bewegungsrichtung an
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Move move = Move.MAPPER.get(entity);
        Vector2 dir = move.getDirection();

        if (dir.isZero()) return;

        Facing facing = Facing.MAPPER.get(entity);


        if (Math.abs(dir.x) > Math.abs(dir.y)) {
        
            if (dir.x > 0) {
                facing.setDirection(FacingDirection.RIGHT);
            } else {
                facing.setDirection(FacingDirection.LEFT);
            }
        } else {
            if (dir.y > 0) {
                facing.setDirection(FacingDirection.UP);
            } else {
                facing.setDirection(FacingDirection.DOWN);
            }
        }
    }
}
