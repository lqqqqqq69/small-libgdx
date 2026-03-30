package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.github.lqqqqqq69.component.Damage;
import io.github.lqqqqqq69.component.Effect;
import io.github.lqqqqqq69.component.Health;
import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Move;
import io.github.lqqqqqq69.System.Collision;
/**
 * ProjectileCollisionSystem prueft Projektile auf Kollision mit Gegnern
 */
public class ProjectileCollisionSystem extends EntitySystem {

    private ImmutableArray<Entity> enemies;     // aktive Gegner
    private ImmutableArray<Entity> projectiles; // Projectile
    private Engine engine;

    /**
     * addedToEngine initialisiert die Gruppen an Entitaeten, mit welchen in Update gearbeitet wird (Gegner, Pfade)
     */
    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;

        // Entities mit Health, Damage und Hitbox = Gegner
        enemies = engine.getEntitiesFor(Family.all(Health.class, Damage.class, Hitbox.class).get());

        // Entities mit Hitbox und Damage, aber ohne Health = Projektile
        projectiles = engine.getEntitiesFor(Family.all(Hitbox.class, Damage.class).exclude(Health.class).get());
    }


    /**
     * update prueft fuer jedes Projektil ob es mit einer Gegner-Hitbox kollidiert
     * 
     * - pruefen, ob sich Projektil mit eine Hitbox ueberlappt
     *  - Falls ja:
     *      - Pruefen, ob das Projektil einen (AOE) Effekt besitzt
     *          - Falls nein -> Schaden an einzelnem Gegner
     *          - Falls ja:
     *              - Erstellung neuer AOE-Hitbox (dem Effekt entsprechend)
     *              - Alle Gegner die mit dieser AOE-Hitbox kollidieren bekommen den Schaden
     *              - Falls der Effekt des Projectils = "slowaoe" -> Anwendung eines Slow-Effekt auf getroffene Gegner
     *      
     *      - Entfernung des Projektils
     *      - Abbrechen der Enemy-Schleife (damit nicht zufaellig mehrere Gegner durch normale Projektile getroffen werden)
     * 
     * @param deltaTime Zeit seit dem letzten Frame
     */
    @Override
    public void update(float deltaTime) {
        for (Entity projectile : projectiles) {
            Hitbox projBox = Hitbox.MAPPER.get(projectile);

            for (Entity enemy : enemies) {
                Health enemyHealth = Health.MAPPER.get(enemy);
                Hitbox enemyBox = Hitbox.MAPPER.get(enemy);

                if (Collision.overlaps(projBox, enemyBox)) {

                    // Pruefen ob Projektil einen Effekt hat
                    if (Effect.MAPPER.get(projectile) == null) {
                        // Normales Projektil
                        enemyHealth.applyDamage(Damage.MAPPER.get(projectile).getDamage(), false);
                        engine.removeEntity(projectile);
                        break; // nur ein Gegner treffen
                    } else {
                        // AOE
                        Hitbox aoeBox;

                        // verschiedene Reichweite der AOE-Effekte
                        if(Effect.MAPPER.get(projectile).getEffect().equals("slowaoe")){
                            aoeBox = new Hitbox(projBox.getPosition().cpy(), 1.5f, 1.5f, Hitbox.BoxType.PROJECTILE);
                        } else {
                            aoeBox = new Hitbox(projBox.getPosition().cpy(), 2.5f, 2.5f, Hitbox.BoxType.PROJECTILE);
                        }

                        for (Entity aoeEnemy : enemies) {
                            // Test, ob neue AOE-Hitbox des Projektils mit weiteren Gegnern kollidiert
                            Hitbox aoeEnemyBox = Hitbox.MAPPER.get(aoeEnemy);
                            
                            if (Collision.overlaps(aoeBox, aoeEnemyBox)) {
                                Health.MAPPER.get(aoeEnemy).applyDamage(Damage.MAPPER.get(projectile).getDamage(), true);
                                
                                if (Effect.MAPPER.get(projectile).getEffect().equals("slowaoe")){
                                    // Anwendung eines Slow Effekts, falls Projektil "slowaoe" Effekt besitzt
                                    Move move = Move.MAPPER.get(aoeEnemy);
                                    move.setModifiedMovement(true);
                                }
                            }
                        }
                        
                        // Entfernen des Projektils bei Treffer
                        engine.removeEntity(projectile);
                        break;
                    }
                }
            }
        }
    }
}
