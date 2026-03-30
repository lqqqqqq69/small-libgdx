package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import io.github.lqqqqqq69.component.Graphic;
import io.github.lqqqqqq69.component.Move;
import io.github.lqqqqqq69.component.Transform;

/**
 * MoveSystem aktualisiert die Position eine Gegners anhand von:
 * - Bewegungsrichtung
 * - Zeit seit letztem Frame
 * - Bewegungsgeschwindigkeit
 */
public class MoveSystem extends IteratingSystem {
    public MoveSystem() {
        super(Family.all(Move.class, Transform.class).get());

    }

    /**
     * processEntity aktualisiert die Position einer Entitaet pro Frame
     * 
     * - wenn Entitaet keine Bewegungsrichtung hat -> return
     * - wenn Entitaet modifiziertes Movement hat
     *      - blaeuliche Einfaerbung
     *      - Anpassung der Zeit, bis das das Movement nicht mehr modifiziert ist
     *      - Reduktion der Deltatime um 25% (um die Berechnung der neuen Position um 25% zu reduzieren)
     * 
     *      - Falls: Modifikationstimer kleiner gleich 0
     *          - Entfernung der Faerbung und des modifizierten Movements
     * 
     * - Setzung der neuen Position anhand Richtung, deltaTime und Speed
     * 
     * @param entity zu bewegende Entitaet
     * @param deltaTime Zeit seit letztem Frame
     */
    @Override
    public void processEntity(Entity entity, float deltaTime) {
        Move move = Move.MAPPER.get(entity);
        if (move.getDirection().isZero()) {
            return; 
        }
        
        Transform transform = Transform.MAPPER.get(entity);
        Vector2 position = transform.getPosition();
        
        if (move.isModifiedMovement()){
            // Anpassung der Faerbung des Gegner, wenn der Gegner einen (Slow-) Effekt hat
            Graphic graphic = Graphic.MAPPER.get(entity);
            graphic.setColor(0.5f, 0.8f, 1f, 1f); // blaeuliche Einfaerbung
            move.setModifiedTimer(move.getModifiedTimer() - deltaTime);
            deltaTime *= 0.75;
            
            if(move.getModifiedTimer() <= 0) {
                // Modifikationstimer abgelaufen -> Zuruecksetzung Faerbung
                move.setModifiedMovement(false);
                graphic.setColor(1f, 1f, 1f, 1f);
            }
        }

        // Setzung der neuen Position
        position.set(
            position.x + move.getDirection().x * move.getSpeed() * deltaTime,
            position.y + move.getDirection().y * move.getSpeed() * deltaTime
        );
    }

}
