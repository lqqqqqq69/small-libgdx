package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.lqqqqqq69.InputService.InputService;
import io.github.lqqqqqq69.Main;
import io.github.lqqqqqq69.Tower;
import io.github.lqqqqqq69.asset.AssetService;
import io.github.lqqqqqq69.asset.AtlasAsset;
import io.github.lqqqqqq69.component.Animation2D;
import io.github.lqqqqqq69.component.Facing;
import io.github.lqqqqqq69.component.Graphic;
import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Hover;
import io.github.lqqqqqq69.component.PlacementValid;
import io.github.lqqqqqq69.component.PreviewTowerRange;
import io.github.lqqqqqq69.component.Transform;
import io.github.lqqqqqq69.component.additionalTowerData;

/**
 * TowerCreationSystem erstellt bei Tastendruck einen (noch nicht platzierten) Tower
 */
public class TowerCreationSystem extends EntitySystem {
    private Engine engine;
    private Viewport viewport;
    public boolean isHovering = false;      // Flag, ob es aktuell einen noch nicht platzierten Turm gibt
    private final AssetService assetService;
    private InputService inputService;
    private String towerType;               // Art des noch nicht platzierten Turms

    public TowerCreationSystem(Engine engine, Viewport viewport, AssetService assetService, InputService inputService) {
        this.engine = engine;
        this.viewport = viewport;
        this.assetService = assetService;
        this.inputService = inputService;
    }

    /**
     * update waehlt je nach Tastendruck den zu erstellenden Turm aus
     * 
     * - Tastendruck von 1 und aktuell kein Turm ausgewaehlt
     *  - Erstellung Bogenschützenturm an Mausposition
     *  - isHovering = true setzen
     * - Tastendruck von 2 und aktuell kein Turm ausgewaehlt
     *  - Erstellung Katapult an Mausposition
     *  - isHovering = true setzen
     * - Tastendruck von 3 und aktuell kein Turm ausgewaehlt
     *  - Erstellung Magierturm an Mausposition
     *  - isHovering = true setzen
     */
    @Override
    public void update(float deltaTime) {
        if (inputService.KEY_1 && !isHovering) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(mousePos); // Umrechnung der "Bildschirmkoordinaten" in "Weltkoordinaten"
            Vector2 towerPos = new Vector2(mousePos.x, mousePos.y);

            Entity tower = createTower(towerPos, "Tower1");
            engine.addEntity(tower);
            isHovering = true;
        }
        if (inputService.KEY_2 && !isHovering) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(mousePos);
            Vector2 towerPos = new Vector2(mousePos.x, mousePos.y);

            Entity tower = createTower(towerPos, "CatapultTower");
            engine.addEntity(tower);
            isHovering = true;
        }
        if (inputService.KEY_3 && !isHovering) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(mousePos);
            Vector2 towerPos = new Vector2(mousePos.x, mousePos.y);

            Entity tower = createTower(towerPos, "WizardTower1");
            engine.addEntity(tower);
            isHovering = true;
        }
    }

    /**
     * createTower erstellt den gewünschten Turm 
     * 
     * - Erstellung einer Turm-Instanz
     * - Anfügen der Komponenten (additionalTowerData etc) anhand der Daten aus der Turm-Instanz
     * - Aufruf von whichTower um Daten für die Transform-Komponente und Hitbox-Groeße zu ermitteln
     * 
     * @param position Position an der der Turm erstellt werden soll
     * @param towerType Art des Turms
     * @return hovernder Turm
     */
    public Entity createTower(Vector2 position, String towerType) {
        Entity entity = this.engine.createEntity();
        this.towerType = towerType;

        Tower towerData = Tower.getType(towerType).createInstance();

        TextureRegion region = this.assetService
            .get(AtlasAsset.OBJECTS)
            .findRegion(towerData.name + "/s_down");

        entity.add(new additionalTowerData(
            towerType, towerData.upgradeCost, towerData.totalCost,
            towerData.upgradable, towerData.upgradeTo, towerData.buyCost
        ));

        entity.add(new Graphic(region, Color.WHITE.cpy()));
        entity.add(new Animation2D(
            AtlasAsset.OBJECTS,
            towerType,
            Animation2D.AnimationType.IDLE,
            Animation.PlayMode.LOOP,
            0.65f
        ));

        entity.add(new Facing(Facing.FacingDirection.DOWN));

        entity.add(new PreviewTowerRange(position, towerData.range));
        
        entity.add(new PlacementValid(false));
        entity.add(new Hover(viewport));

        whichTower(position, entity);

        return entity;
    }

    /**
     * whichTower bestimmt die angezeigte Groesse eines Sprites und dessen Hitbox
     * 
     * @param position Position an welcher der Turm grundlegend erstellt wird
     * @param entity Turm-Entitaet
     */
    public void whichTower(Vector2 position, Entity entity){
        int x=0,y=0;
        float width=0, height=0, offset = 0;

        switch (towerType) {
            case "Tower1":
            case "Tower2":
            case "Tower3":
                x = 40;
                y = 55;
                width = 1.85f;
                height = 1.85f; 
                break;
            case "CatapultTower":
                x = 54;
                y = 80;
                width = 2.75f;
                height = 2.75f;
                break;
            case "WizardTower1":
                x = 45;
                y = 65;
                width = 2.25f;
                height = 2.25f;
                break;
            case "WizardTower2":
            case "WizardTower3":
                x = 45;
                y = 84;
                width = 2.25f;
                height = 2.25f;
                offset = Offset.WIZARD_TOWER2_TRANSFORM_Y;
                break;
        }

        entity.add(new Transform(
                new Vector2(position.x, position.y + offset),
                1,
                new Vector2(x, y).scl(Main.UNIT_SCALE),
                new Vector2(1, 1),
                0f,
                0
            ));
            entity.add(new Hitbox(new Vector2(position.x, position.y),
                width, height, Hitbox.BoxType.TOWER));
    }

    public void setHovering(boolean hovering) {
        this.isHovering = hovering;
    }

    public boolean isHovering() {
        return isHovering;
    }

    public String getTowerType() {
        return towerType;
    }
}
