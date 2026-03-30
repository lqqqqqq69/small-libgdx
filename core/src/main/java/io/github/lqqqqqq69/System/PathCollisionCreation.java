package io.github.lqqqqqq69.System;

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import io.github.lqqqqqq69.component.Hitbox;

/**
 * PathCollisionCreation erstellt die Pfad-Hitboxen (damit man Tuerme nicht auf den Pfaden platzieren kann)
 */
public class PathCollisionCreation{
    private Engine engine;
    private ArrayList<Entity> pathCollisions = new ArrayList<>(); // Liste der Stuecke des Pfads


    /**
     * Der Konstruktor erstellt fuer das aktuelle Level die Pfad-Hitboxen und fuegt sie zur List der Pfade hinzu
     * 
     * @param engine
     * @param level aktuelles Level
     */
    public PathCollisionCreation(Engine engine, int level) {
        this.engine = engine; 

        if (level == 1) {
                pathCollisions.add(createPathCollision(new Vector2(6.5f, 10.5f), 14f, 2f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(12.5f, 12f), 2f, 5f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(14.5f, 13.5f), 6f, 2f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(16.5f, 10f), 2f, 9f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(20.5f, 6.5f), 10f, 2f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(24.5f, 8f), 2f, 5f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(27.5f, 9.5f), 8f, 2f, Hitbox.BoxType.PATH ));

                pathCollisions.add(createPathCollision(new Vector2(29f, 9f),3.5f, 12f, Hitbox.BoxType.PATH ));

                
        } if (level == 2) {
                pathCollisions.add(createPathCollision(new Vector2(5f, 10.5f), 13f, 2f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(10.5f, 12f), 2f, 21f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(13.5f, 2.5f), 8f, 2f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(16.5f, 10.5f), 2f, 14f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(19.5f, 16.5f), 8f, 2f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(22.5f, 13f), 2f, 9f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(26.5f, 9.5f),10f, 2f, Hitbox.BoxType.PATH ));

                pathCollisions.add(createPathCollision(new Vector2(29f, 9f),3.5f, 12f, Hitbox.BoxType.PATH ));
    
        } if (level == 3) {
                pathCollisions.add(createPathCollision(new Vector2(12f, 6.5f), 33f, 2f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(8f, 3.5f), 25f, 2f, Hitbox.BoxType.PATH ));

                pathCollisions.add(createPathCollision(new Vector2(19.5f, 7f), 2f, 9f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(11.5f, 10.5f), 18f, 2f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(3.5f, 13f), 2f, 7f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(27.5f, 11f), 2f, 11f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(15.5f, 15.5f), 26f, 2f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(15.5f, 19), 10.5f, 4f, Hitbox.BoxType.PATH ));
                pathCollisions.add(createPathCollision(new Vector2(15.5f, 17.5f), 2f, 6f, Hitbox.BoxType.PATH ));
        }
        
        for (Entity pathCollision : pathCollisions) {
            this.engine.addEntity(pathCollision);
        }
    }

    /**
     * createPathCollision kreiert die Pfade
     * 
     * @param position Position der Hitbox
     * @param width Breite der Hitbox
     * @param height Hoehe der Hitbox
     * @param boxType Art der Hitbox (Path)
     * @return Pfad-Entitaet
     */
    public Entity createPathCollision(Vector2 position, float width, float height, Hitbox.BoxType boxType) {
        Entity entity = this.engine.createEntity();
        entity.add(new Hitbox(position, width, height, boxType));

        return entity;
    }



}
