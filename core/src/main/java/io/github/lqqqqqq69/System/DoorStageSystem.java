package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.lqqqqqq69.Defend;
import io.github.lqqqqqq69.Main;
import io.github.lqqqqqq69.asset.AssetService;
import io.github.lqqqqqq69.asset.AtlasAsset;
import io.github.lqqqqqq69.component.Graphic;
import io.github.lqqqqqq69.component.Health;
import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Transform;


/**
 * DoorStageSystem erstellt dynamisch die zu verteidigende Tuer und updated den oeffnungsgrad dieser
 */
public class DoorStageSystem extends EntitySystem {

    private final AssetService assetService;
    private final Array<AtlasRegion> stages;    // Array der "Animationsstufen"
    private final Engine engine;
    private final Entity door;                  // Tuer-Entitaet

    /**
     * Der Konstruktor dient fuer die Erstellung und Platzierung der Tuer fuer die jeweiligen Level
     * 
     * @param assetService
     * @param engine
     * @param level aktuelles Level
     */
    public DoorStageSystem(AssetService assetService, Engine engine, int level) {
        this.engine = engine;
        this.assetService = assetService;

        if (level != 3) {
            stages = assetService.get(AtlasAsset.OBJECTS).findRegions("Door/s_right");
            door = createDefend(new Vector2(29f / Main.UNIT_SCALE, 10.5f / Main.UNIT_SCALE), level);
        } else {
            stages = assetService.get(AtlasAsset.OBJECTS).findRegions("Door/s_down");
            door = createDefend(new Vector2(15.5f / Main.UNIT_SCALE, 19.25f / Main.UNIT_SCALE), level);
        }

        engine.addEntity(door);
    }

    /**
     * update ermittelt pro Frame den aktuelle Stageindex und aktualisiert das Erscheinungsbild der Tuer daran
     */
    @Override
    public void update(float deltaTime) {
        Health health = Health.MAPPER.get(door);
        Graphic graphic = Graphic.MAPPER.get(door);

        int stageIndex = getStageIndex(health.getHealth(), health.getMaxHealth());
        if (stageIndex >= 0 && stageIndex < stages.size) {
            graphic.setRegion(stages.get(stageIndex));
        }
    }

    /**
     * getStageIndex bestimmt anhand von den HP der Tuer die Animationsstufe.
     * - HP werden in gleich große Abschnitte geteilt.
     * - verlorene Stages anhand aktuelle HP im Vergleich zu maximalen HP ermittel
     * 
     * @param currentHp aktuelleHP
     * @param maxHp maximaleHP
     * @return aktuelle Stage
     */
    private int getStageIndex(float currentHp, float maxHp) {
        float hpPerStage = maxHp / (stages.size - 1);
        int lostStages = (int)((maxHp - currentHp) / hpPerStage);
        return lostStages;
    }

    /**
     * create Defend erstellt die zu verteidigenden Tuer mit ggf. leicht angepasster Groeße
     * @param position Position
     * @param level aktuelles Level
     * @return Tuer
     */
    private Entity createDefend(Vector2 position, int level) {
        Entity entity = engine.createEntity();

        Defend defendsData = Defend.getType(String.valueOf(level)).createInstance();

        TextureRegion region = assetService
            .get(AtlasAsset.OBJECTS)
            .findRegion(defendsData.name + "/s_right");

        entity.add(new Graphic(region, Color.WHITE.cpy()));
        entity.add(new Health(defendsData.health, 0, false));

        // Unterschiedliche Groeßen fuer Level 3 oder andere
        Vector2 size = (level == 3) ? new Vector2(48, 64) : new Vector2(64, 96);

        entity.add(new Transform(
            position.cpy().scl(Main.UNIT_SCALE),
            0,
            size.scl(Main.UNIT_SCALE),
            new Vector2(1, 1),
            0f,
            0
        ));

        entity.add(new Hitbox(
            position.cpy().scl(Main.UNIT_SCALE),
            3f,
            3.75f,
            Hitbox.BoxType.DEFEND
        ));

        return entity;
    }

    public Entity getDoor() {
        return door;
    }
}
