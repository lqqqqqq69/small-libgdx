package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import io.github.lqqqqqq69.component.Damage;
import io.github.lqqqqqq69.component.Health;
import io.github.lqqqqqq69.component.Move;

/**
 * Das FailureCheckerSystem testet, ob die zu verteidigende Tuer 0 HP hat und man dementsprechend verliert
 */
public class FailureCheckerSystem extends EntitySystem{
    private ImmutableArray<Entity> enemies;     // Aktuell lebende Gegner
    private Engine engine;
    private boolean failure = false;            // signalisiert, ob man verloren hat
    private final DoorStageSystem doorStageSystem;

    public FailureCheckerSystem(DoorStageSystem doorStageSystem){
        this.doorStageSystem = doorStageSystem;
    }


    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;

        enemies = engine.getEntitiesFor(Family.all(Health.class, Damage.class).get());
    }

    /**
     * update prueft pro Frame, ob man verloren hat
     * 
     * - Wenn bereits festgestellt wurde, dass man verloren hat -> return
     * - Test, ob die Tuer 0 HP hat
     *  - Falls ja:
     *      - alle Gegner nach unten drehen und die Todesanimation abspielen
     * 
     * - Test, ob Tuer 0 HP und ob alle Gegner "ordentlich gestorben" sind + entfernt wurden
     *  - Falls ja:
     *      - failure auf true setzen
     * 
     * @param deltaTime Zeit zwischen Frames
     */
    @Override
    public void update(float deltaTime){
        if (failure == true) return;

        Health health = Health.MAPPER.get(doorStageSystem.getDoor());

        if(health.getHealth() <= 0.0f){
            // Abspielen der Todesanimation aller Gegner, wenn die Tuer 0 HP hat
            for (Entity enemy : enemies){
                Move move = Move.MAPPER.get(enemy);
                move.setDirection(new Vector2(0,1 ));
                
                HealthSystem hs = engine.getSystem(HealthSystem.class);
                hs.enemyDeath(enemy);
            }

        }
        if (health.getHealth() <= 0.0f && enemies.size() == 0){
            failure = true;
        }
    }

    public boolean isFailure() {
        return failure;
    }


}
