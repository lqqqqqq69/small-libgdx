package io.github.lqqqqqq69.System;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.github.lqqqqqq69.AudioService.AudioService;
import io.github.lqqqqqq69.Main;
import io.github.lqqqqqq69.asset.AssetService;
import io.github.lqqqqqq69.asset.AtlasAsset;
import io.github.lqqqqqq69.asset.SoundAsset;
import io.github.lqqqqqq69.System.Collision;
import io.github.lqqqqqq69.component.Animation2D;
import io.github.lqqqqqq69.component.Damage;
import io.github.lqqqqqq69.component.Effect;
import io.github.lqqqqqq69.component.Facing;
import io.github.lqqqqqq69.component.Graphic;
import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Move;
import io.github.lqqqqqq69.component.ShootingData;
import io.github.lqqqqqq69.component.TimeToLive;
import io.github.lqqqqqq69.component.TowerRange;
import io.github.lqqqqqq69.component.Transform;
import io.github.lqqqqqq69.component.additionalTowerData;

/** 
 * TargetingSystem prüft, ob ein Gegner innerhalb der Reichweite eines Turms ist und schießt bei Bedarf ein Projektil ab 
 */
public class TargetingSystem extends EntitySystem {

    private ImmutableArray<Entity> troops; // Liste aller Türme/Turmtruppen
    private Engine engine;
    private final AssetService assetService;
    private final Map<Entity, Float> cooldowns = new HashMap<>(); // Cooldown je Turm
    private final WaveSystem waveSystem;
    private final Map<Entity, Boolean> attackExecuted = new HashMap<>(); // wurde in diesem Angriff schon ein Projektil erstellt?
    private final AudioService audioService;

    public TargetingSystem(AssetService assetService, WaveSystem waveSystem, AudioService audioService) {
        this.assetService = assetService;
        this.waveSystem = waveSystem;
        this.audioService = audioService;
    }

    /**
     * addedToEngine initialisiert die Gruppen an Entitäten, mit welchen in Update gearbeitet wird (Turmtruppen)
     */
    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        // TowerRange + Animation (aber keine Hitbox) = Türme/Turmtruppen
        troops = engine.getEntitiesFor(Family.all(TowerRange.class, Animation2D.class).exclude(Hitbox.class).get());
    }

    /**
     * update führt die Angriffe der Türme aus
     * 
     * - Für jeden Turm prüfen ob ein Gegner in Reichweite ist
     * - Wenn ja:
     *   - Angriffscooldown prüfen
     *   - Angriffsanimation starten
     *   - Richtung berechnen und setzen
     *   - Projektil erzeugen sobald Attack-Trigger erreicht
     * - Wenn Animation fertig -> zurück auf Idle, Cooldown zurücksetzen
     * - Wenn kein Gegner in Reichweite -> Idle setzen
     * 
     * @param deltaTime Zeit seit dem letzten Frame
     */
    @Override
    public void update(float deltaTime) {
        for (Entity troop : troops) {
            ShootingData shootingData = ShootingData.MAPPER.get(troop);
            Animation2D animation = Animation2D.MAPPER.get(troop);
            TowerRange towerRange = TowerRange.MAPPER.get(troop);
            Transform transform = Transform.MAPPER.get(troop);

            boolean targetInRange = false;

            float timer = cooldowns.getOrDefault(troop, 0f);
            boolean attack = attackExecuted.getOrDefault(troop, false);

            float attacktrigger;
            if ("CatapultTower".equals(additionalTowerData.MAPPER.get(troop).getTowerType())) {
                attacktrigger = 1 / 6f;
            } else {
                attacktrigger = 5 / 6f;
            }

            for (Entity enemy : waveSystem.getActiveEnemies()) {
                Hitbox enemyHitbox = Hitbox.MAPPER.get(enemy);
                if (enemyHitbox == null) continue;

                // Gegner innerhalb der Reichweite?
                if (Collision.overlaps(towerRange, enemyHitbox)) {
                    targetInRange = true;

                    // Angriff läuft noch / Cooldown
                    if (shootingData.getAttackSpeed() > timer) {
                        if (animation.getType() != Animation2D.AnimationType.ATTACK) {
                            // Richtung berechnen
                            Vector2 archerPos = transform.getPosition();
                            Vector2 enemyPos = enemyHitbox.getPosition();
                            Vector2 dir = new Vector2(enemyPos).sub(archerPos);

                            Facing.FacingDirection facing;
                            if (Math.abs(dir.x) > Math.abs(dir.y)) {
                                facing = dir.x > 0 ? Facing.FacingDirection.RIGHT : Facing.FacingDirection.LEFT;
                            } else {
                                facing = dir.y > 0 ? Facing.FacingDirection.UP : Facing.FacingDirection.DOWN;
                            }

                            Facing.MAPPER.get(troop).setDirection(facing);

                            animation.setType(Animation2D.AnimationType.ATTACK);
                            animation.setPlayMode(Animation.PlayMode.NORMAL);
                            animation.setSpeed(1 / shootingData.getAttackSpeed() * 0.6f);
                        }
                        // Projektil erzeugen
                        if (shootingData.getAttackSpeed() * attacktrigger <= timer && !attack) {
                            chooseAttackSound(troop);
                            engine.addEntity(
                                createProjectiles(transform.getPosition(), enemyHitbox.getPosition(), troop)
                            );
                            attack = true;
                        }
                        timer += deltaTime;
                
                    
                } else if (animation.getType() == Animation2D.AnimationType.ATTACK && animation.isFinished()) {
                    // Angriff beendet -> zurücksetzen
                        timer = 0;
                        attack = false;
                        animation.setType(Animation2D.AnimationType.IDLE);
                        animation.setPlayMode(Animation.PlayMode.LOOP);
                        animation.setSpeed(0.65f);
                    }
                    
                    break;
                }
                
            }

            // Kein Gegner in Reichweite -> Idle 
            if (!targetInRange) {
                if (!waveSystem.isWaveActive() && (animation.getType() != Animation2D.AnimationType.IDLE || Facing.MAPPER.get(troop).getDirection() != Facing.FacingDirection.DOWN)){
                    Facing.MAPPER.get(troop).setDirection(Facing.FacingDirection.DOWN);
                    animation.setType(Animation2D.AnimationType.IDLE);
                    animation.setPlayMode(Animation.PlayMode.LOOP);
                    animation.setSpeed(0.65f);
                    timer = 0;
                    attack = false;
                }
                
                if (animation.getType() == Animation2D.AnimationType.ATTACK && animation.isFinished()) {
                    animation.setType(Animation2D.AnimationType.IDLE);
                    animation.setPlayMode(Animation.PlayMode.LOOP);
                    animation.setSpeed(0.65f);
                    timer = 0;
                    attack = false;

                }
            }

            cooldowns.put(troop, timer);
            attackExecuted.put(troop, attack);
        }
    }

    /**
     * creatProjectiles erstellt ein Projektil und berechnet dessen Startposition und Flugbahn
     * 
     * @param startingPos Startposition (Position der Truppe)
     * @param targetPos Zielposition (Position des anvisierten Gegners)
     * @param troop schießende Truppe
     * @return Projektil-Entität
     */
    public Entity createProjectiles(Vector2 startingPos, Vector2 targetPos, Entity troop) {
        Entity entity = this.engine.createEntity();

        ShootingData shootingData = ShootingData.MAPPER.get(troop);
        TextureRegion region = this.assetService.get(AtlasAsset.OBJECTS)
            .findRegion(shootingData.getProjectileType() + "/1");

        String type = shootingData.getProjectileType();

        // Startpunkt anpassen
        Vector2 projStart;
        if ("Magic".equals(type)) {
            projStart = new Vector2(startingPos.x, startingPos.y + 0.9f);
        } else if ("Ice".equals(type) || "Fire".equals(type)) {
            projStart = new Vector2(startingPos.x, startingPos.y + 1.5f);
        } else {
            projStart = startingPos.cpy();
        }

        // Richtungsvektor & Rotation
        Vector2 dir = targetPos.cpy().sub(projStart);
        float rotDeg = (float) Math.toDegrees(Math.atan2(dir.y, dir.x)) - 90f;

        entity.add(new Graphic(region, Color.WHITE.cpy()));
        entity.add(new Damage(shootingData.getDamage()));

        // Transform & Hitbox pro Typ
        if ("Arrow".equals(type)) {
            entity.add(new Transform(projStart.cpy(), 1,
                new Vector2(2, 10).scl(Main.UNIT_SCALE),
                new Vector2(1, 1), rotDeg, 0
            ));
            entity.add(new Hitbox(projStart.cpy(), 0.4f, 0.4f, Hitbox.BoxType.PROJECTILE));

        } else if ("Stone".equals(type)) {
            entity.add(new Transform(projStart.cpy(), 1,
                new Vector2(6, 6).scl(Main.UNIT_SCALE),
                new Vector2(1, 1), rotDeg, 0
            ));
            entity.add(new Hitbox(projStart.cpy(), 0.6f, 0.6f, Hitbox.BoxType.PROJECTILE));

        } else if ("Magic".equals(type)) {
            entity.add(new Transform(projStart.cpy(), 1,
                new Vector2(5, 5).scl(Main.UNIT_SCALE),
                new Vector2(1, 1), rotDeg, 0
            ));
            entity.add(new Hitbox(projStart.cpy(), 0.6f, 0.6f, Hitbox.BoxType.PROJECTILE));

        } else if ("Ice".equals(type) || "Fire".equals(type)) {
            entity.add(new Transform(projStart.cpy(), 1,
                new Vector2(13, 13).scl(Main.UNIT_SCALE),
                new Vector2(1, 1), rotDeg, 0
            ));
            entity.add(new Hitbox(projStart.cpy(), 1.2f, 1.2f, Hitbox.BoxType.PROJECTILE));
            entity.add(new Effect(shootingData.geteffect())); // AoE
        }

    
        Move move = new Move(shootingData.getSpeed());
        move.setDirection(dir.nor());
        entity.add(move);

        entity.add(new TimeToLive(3f));
        return entity;
    }


    /**
     * chooseAttackSound wählt den Angriffssound, welcher zur angreifenden Turmtruppe passt und spielt ihn ab
     * 
     * @param troop angreifende Truppe
     */
    private void chooseAttackSound(Entity troop) {
        String towertype = additionalTowerData.MAPPER.get(troop).getTowerType();
        if (towertype.equals("Tower1") || towertype.equals("Tower2") || towertype.equals("Tower3")) {
            audioService.playSound(SoundAsset.ARCHER_ATTACK);
        } else if (towertype.equals("CatapultTower")) {
            audioService.playSound(SoundAsset.CATAPULT_ATTACK);
        } else {
            audioService.playSound(SoundAsset.WIZARD_ATTACK);
        }
    }
}
