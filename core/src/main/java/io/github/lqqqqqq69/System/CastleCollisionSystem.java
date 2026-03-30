package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Animation;

import io.github.lqqqqqq69.AudioService.AudioService;
import io.github.lqqqqqq69.asset.SoundAsset;
import io.github.lqqqqqq69.component.Animation2D;
import io.github.lqqqqqq69.component.Damage;
import io.github.lqqqqqq69.component.Health;
import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Move;
import io.github.lqqqqqq69.component.additionalEnemyData;

/**
 * CastleCollisionSystem prüft, ob ein Gegner die Burg (Tür) erreicht hat und er dementsprechend Schaden ausübt
 */
public class CastleCollisionSystem extends EntitySystem {

    private ImmutableArray<Entity> enemies; // Array der Gegner
    private ImmutableArray<Entity> castles; // Array der Tür(en) - spät bemerkt, dass man das eigentlich anders lösen könnte. Ist aber trotzdem funktional
    private final AudioService audioService;
    private final WaveSystem waveSystem;

    public CastleCollisionSystem(AudioService audioService, WaveSystem waveSystem){
        this.audioService = audioService;
        this.waveSystem = waveSystem;
    }


    @Override
    public void addedToEngine(Engine engine) {
        // Hitbox und isEnemy-Komponente = Gegner
        enemies = engine.getEntitiesFor(Family.all(Hitbox.class, additionalEnemyData.class).get());
        // Hitbox und Health, aber ohne Damage-Komponente = Tür
        castles = engine.getEntitiesFor(Family.all(Hitbox.class, Health.class).exclude(Damage.class).get());
    }

    /**
     * update prüft für jeden Gegner, ob ein Gegner die Tür erreicht hat
     * 
     * - Überlappen sich die Hitbox der Tür und die Hitbox des Gegners
     *  - Falls ja:
     *      - befindet sich der Gegner schon in der Angriffsanimation?
     *          - Falls nein -> Stoppen der Bewegung, Setzen der Angriffsanimation, abspielen Angriffssound
     *          - Falls ja -> Wenn Animation beendet Anwendung des Schadens + Entfernung Gegners
     */
    @Override
    public void update(float deltaTime) {
        for (Entity enemy : enemies) {
            Hitbox enemyBox = Hitbox.MAPPER.get(enemy);

            for (Entity castle : castles) {
                Hitbox castleBox = Hitbox.MAPPER.get(castle);

                // Prüfen, ob der Gegner das Schloss erreicht hat
                if (Collision.overlaps(enemyBox, castleBox)) {
                    Move move = Move.MAPPER.get(enemy);
                    Health health = Health.MAPPER.get(castle);
                    Damage damage = Damage.MAPPER.get(enemy);
                    Animation2D animation = Animation2D.MAPPER.get(enemy);;

                    if (!move.newAnimation) {
                        // Gegner beginnt Angriffsanimation
                        move.setSpeed(0);
                        animation.setType(Animation2D.AnimationType.ATTACK);
                        animation.setPlayMode(Animation.PlayMode.NORMAL);
                        move.newAnimation = true;

                        chooseAttackSound(enemy);
                        
                        waveSystem.getActiveEnemies().remove(enemy);

                    } else {
                        // Animation beendet -> Schaden anwenden
                        if (animation.isFinished()) {
                            health.applyDamage(damage.getDamage(), false);
                            getEngine().removeEntity(enemy);
                        }
                    }
                }
            }
        }
    }

    /**
     * chooseAttackSound spielt einen Angriffssound anhand der Art der angreifenden Entität ab
     * @param enemy
     */
    public void chooseAttackSound(Entity enemy) {
    String type = additionalEnemyData.MAPPER.get(enemy).getEnemyType();

        switch (type) {
            case "Goblin" -> audioService.playSound(SoundAsset.GOBLIN_ATTACK);
            case "Slime" -> audioService.playSound(SoundAsset.SLIME_ATTACK);
            case "Wolf" -> audioService.playSound(SoundAsset.WOLF_ATTACK);
            case "GoblinRider" -> audioService.playSound(SoundAsset.GOBLINRIDER_ATTACK);
            case "Lizard" -> audioService.playSound(SoundAsset.LIZARD_ATTACK);
            case "Seagull" -> audioService.playSound(SoundAsset.SEAGULL_ATTACK);
            case "Golem" -> audioService.playSound(SoundAsset.GOLEM_ATTACK);
        }
    }
}
