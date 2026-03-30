package io.github.lqqqqqq69.System;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import io.github.lqqqqqq69.component.Move;
import io.github.lqqqqqq69.component.Transform;
import io.github.lqqqqqq69.component.additionalEnemyData;

/**
 * PathingSystem kreiert die Waypoints ens Levels, prueft ob diese erreicht wurden und aendert gegebenenfalls die Bewegungsrichtung der Gegner
 */
public class PathingSystem extends IteratingSystem{
    private ArrayList<Waypoint> waypoints = new ArrayList<>(); // Liste der Wegpunkte

    /**
     * Der Konsturktor definiert welche Entitaeten vom System pro Frame aktualisiert werden muessen und die Wegpunkte welches Levels geladen werden muessen
     * 
     * @param level aktuelles Level
     */
    public PathingSystem(int level) {
        super(Family.all(Move.class, Transform.class, additionalEnemyData.class).get());

        createPath(level);
    }

    /**
     * processEntity prueft, ob ein Waypoint erreicht wurde und dementsprechend die Richtung geaendert werden muss
     * 
     * @param entity zu pruefende Entitaet
     * @param deltaTime Zeit seit dem letzten Frame
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime){
        Move move = Move.MAPPER.get(entity);
        
        Transform transform = Transform.MAPPER.get(entity);
        Vector2 position = transform.getPosition();

        // Pruefen ob ein Waypoint erreicht wurde
        for (Waypoint waypoint : waypoints){
            if (waypoint.isReached(position)){
                // Setzen neuer Richtung
                move.setDirection(waypoint.getNextDirection());
            }
        }
    }

    /**
     * createPath erstellt den Pfad bzw. dessen Wegpunkte fuer das aktuelle Level
     * Abhaengig vom Level werden Waypoint Koordinaten in die Waypoint-Liste eingetragen
     * 
     * @param level aktuelles Level
     */
    public void createPath(int level){
        if (level == 1){
            waypoints.add(new Waypoint(new Vector2(12.6f, 10.7f), 0.1f, new Vector2(0, 1)));
            waypoints.add(new Waypoint(new Vector2(12.6f, 13.7f), 0.11f, new Vector2(1, 0))); 
            waypoints.add(new Waypoint(new Vector2(16.6f, 13.7f), 0.12f, new Vector2(0, -1))); 
            waypoints.add(new Waypoint(new Vector2(16.6f, 6.7f),   0.13f, new Vector2(1, 0))); 
            waypoints.add(new Waypoint(new Vector2(24.6f, 6.7f), 0.14f, new Vector2(0, 1)));  
            waypoints.add(new Waypoint(new Vector2(24.6f, 9.5f),   0.15f, new Vector2(1, 0)));

        } if (level == 2){
            waypoints.add(new Waypoint(new Vector2(10.5f, 20.5f), 0.1f, new Vector2(0, -1)));
            waypoints.add(new Waypoint(new Vector2(10.6f, 10.7f), 0.11f, new Vector2(0, -1)));
            waypoints.add(new Waypoint(new Vector2(10.6f, 2.7f), 0.12f, new Vector2(1, 0)));
            waypoints.add(new Waypoint(new Vector2(16.6f, 2.7f), 0.13f, new Vector2(0, 1)));
            waypoints.add(new Waypoint(new Vector2(16.6f, 16.7f), 0.14f, new Vector2(1, 0)));
            waypoints.add(new Waypoint(new Vector2(22.6f, 16.7f), 0.15f, new Vector2(0, -1)));
            waypoints.add(new Waypoint(new Vector2(22.6f, 9.7f), 0.16f, new Vector2(1, 0)));

        } if (level == 3){
            // Weg 1
            waypoints.add(new Waypoint(new Vector2(19.6f, 3.7f), 0.1f, new Vector2(0, 1)));
            waypoints.add(new Waypoint(new Vector2(19.6f, 10.7f), 0.11f, new Vector2(-1, 0)));
            waypoints.add(new Waypoint(new Vector2(3.4f, 10.7f), 0.12f, new Vector2(0, 1)));
            waypoints.add(new Waypoint(new Vector2(3.4f, 15.7f), 0.13f, new Vector2(1, 0)));

            // Weg 2
            waypoints.add(new Waypoint(new Vector2(27.6f, 6.7f), 0.1f, new Vector2(0, 1)));
            waypoints.add(new Waypoint(new Vector2(27.6f, 15.7f), 0.11f, new Vector2(-1, 0)));

            waypoints.add(new Waypoint(new Vector2(15.5f, 15.7f), 0.14f, new Vector2(0, 1)));

        }

        
       
    }
}

