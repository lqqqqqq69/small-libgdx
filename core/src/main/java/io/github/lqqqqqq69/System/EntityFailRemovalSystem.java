package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Move;
import io.github.lqqqqqq69.component.TimeToLive;
import io.github.lqqqqqq69.component.Transform;


/**
 * Das EntityFailRemovalSystem dient für die Entfernung "fehlerhafter" Entitäten
 * 
 * Folgende Entitäten werden entfernt:
 * - Gegner, welche vom Weg abkommen
 * - Projektile, welche ihre TimeToLive überschritten haben (weil sie ggf. verfehlt haben)
 */
public class EntityFailRemovalSystem extends IteratingSystem{
    private final Engine engine;
    private ImmutableArray<Entity> paths;   // Array, welches alle Pfade (Hitboxen) enthält
    
    public EntityFailRemovalSystem(Engine engine){
        super(Family.all(Move.class, Transform.class).get());
        this.engine = engine;

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        
        // Hitbox aber kein Transform-Komponent = Pfad
        paths = engine.getEntitiesFor(Family.all(Hitbox.class).exclude(Transform.class).get());
    }

    /**
     * processEntity entfernt die fehlerhaften Entities
     * 
     * - wenn die Entität eine TimeToLive besitzt (Projektil)
     *  - Test: Ist TTL > 0?
     *      - Falls nein -> Entfernung der Entität
     *      - Falls ja -> TTL wird um deltaTime aktualisiert
     * 
     * - wenn Entität keine TTL besitzt und eine Hitbox hat (Gegner)
     *  - Ist der Gegner auf irgendeine Pfad-Hitbox?
     *      - Falls nein: Entfernung Gegner
     *      - Falls ja: Break
     * 
     * @param deltaTime Zeit seit letztem Frame
     * @param entity geteste Entity
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime){
        TimeToLive timeToLive = TimeToLive.MAPPER.get(entity);

        if (timeToLive != null){
            // wenn die Entity eine TimeToLive-Komponente bestzt
            if (timeToLive.getTimeToLive() <= 0){
                engine.removeEntity(entity);
            } else {
                timeToLive.setTimeToLive(timeToLive.getTimeToLive() - deltaTime);
            }
        } else {
            Hitbox enemyHitbox = Hitbox.MAPPER.get(entity);
            boolean onPath = false;
            if (enemyHitbox == null) return;
            for (Entity path : paths) {
                Hitbox pathHitbox = Hitbox.MAPPER.get(path);
                if (Collision.overlaps(enemyHitbox, pathHitbox)) {
                    onPath = true;
                    break;
                }
            }

            if (!onPath) {
                engine.removeEntity(entity);
            }
        }   
    }
}
