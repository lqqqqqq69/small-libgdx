package io.github.lqqqqqq69.System;

import com.badlogic.gdx.math.Vector2;

/**
 * Waypoint beschreibt einen Wegpunkt auf einer Map.
 * Wegpunkte dienen dazu, damit die Gegner ordentlich auf dem Pfad laufen 
 */

 public class Waypoint {
    
    private final Vector2 position;         // Position des Waypoints
    private final float tolerance;          // Toleranz des Waypoints
    private final Vector2 nextDirection;    // Richtung, in die ab dem Waypoint gelaufen werden soll

    public Waypoint(Vector2 position, float tolerance, Vector2 nextDirection) {
        this.position = position;
        this.tolerance = tolerance;
        this.nextDirection = nextDirection;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getTolerance() {
        return tolerance;
    }

    public Vector2 getNextDirection() {
        return nextDirection;
    }

    /**
     * isReached bestimmt, ob ein Waypoint (innerhalb der Toleranz) erreicht ist
     * 
     * @param currentPos aktuelle Position des Gegners
     * @return true, wenn der Waypoint erreicht wurde, sonst false
     */
    public boolean isReached(Vector2 currentPos) {
        return currentPos.dst(position) <= tolerance;
    }
}
