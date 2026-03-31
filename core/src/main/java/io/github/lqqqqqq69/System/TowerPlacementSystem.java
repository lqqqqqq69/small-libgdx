package io.github.lqqqqqq69.System;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.lqqqqqq69.AudioService.AudioService;
import io.github.lqqqqqq69.InputService.InputService;
import io.github.lqqqqqq69.Main;
import io.github.lqqqqqq69.Tower;
import io.github.lqqqqqq69.asset.AssetService;
import io.github.lqqqqqq69.asset.AtlasAsset;
import io.github.lqqqqqq69.asset.SoundAsset;
import io.github.lqqqqqq69.component.Animation2D;
import io.github.lqqqqqq69.component.Facing;
import io.github.lqqqqqq69.component.Graphic;
import io.github.lqqqqqq69.component.Hitbox;
import io.github.lqqqqqq69.component.Hover;
import io.github.lqqqqqq69.component.PlacementValid;
import io.github.lqqqqqq69.component.PreviewTowerRange;
import io.github.lqqqqqq69.component.ShootingData;
import io.github.lqqqqqq69.component.TowerRange;
import io.github.lqqqqqq69.component.Transform;
import io.github.lqqqqqq69.component.Troop;
import io.github.lqqqqqq69.component.Visualbox;
import io.github.lqqqqqq69.component.additionalTowerData;

/**
 * TowerPlacementSystem platziert einen noch nicht platzierten Turm oder entfernt diesen, abhaengig davon, welche Taste gedrueckt wurde
 */
public class TowerPlacementSystem extends IteratingSystem {
    private final TowerCreationSystem towerCreationSystem;
    private final Engine engine;
    private final AssetService assetService;
    private final PlacementValidationSystem collisionSystem;
    private final CashSystem cashSystem;
    private final InputService inputService;
    private final AudioService audioService;

    public TowerPlacementSystem( TowerCreationSystem towerCreationSystem, Engine engine, AssetService assetService,
            PlacementValidationSystem collisionSystem, CashSystem cashSystem, InputService inputService, AudioService audioService) 
    {
        super(Family.all(Hover.class, Graphic.class, PlacementValid.class).get());
        this.towerCreationSystem = towerCreationSystem;
        this.engine = engine;
        this.assetService = assetService;
        this.collisionSystem = collisionSystem;
        this.cashSystem = cashSystem;
        this.inputService = inputService;
        this.audioService = audioService;
    }

    /**
     * processEntity prueft pro Frame, ob eine Maustaste gedrueckt wurde und fuehrt dementsprechend eine Aktion durch fuer den hovernden Turm durch
     * 
     * - linke Maustaste + valides Placement -> Platzierung + Placement-Valid-Sound
     * - linke Maustaste + invalides Placement -> Invalid-Placement-Sound
     * - rechte Maustaste -> Abbruch der Platzierung
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlacementValid placementValid = PlacementValid.MAPPER.get(entity);

        if (inputService.MOUSE1 && towerCreationSystem.isHovering() && placementValid.isValid()) {
            audioService.playSound(SoundAsset.TOWER_PLACEMENT);
            placeTower(entity);
        } else if (inputService.MOUSE1 && towerCreationSystem.isHovering() && !placementValid.isValid()){
            audioService.playSound(SoundAsset.TOWER_PLACEMENT_INVALID);
        } else if (inputService.MOUSE2 && towerCreationSystem.isHovering()) {
            towerCreationSystem.setHovering(false);
            engine.removeEntity(entity);
        }
    }

    /**
     * placeTower platziert den Turm vollstaendig
     * 
     * - Entfernen der Hover-Komponente (damit der Tower sich nicht mehr mit der Maus bewegt)
     * - Erstellung der Turmtruppe (+Turmtruppen-Komponent)
     * - Entfernen der PreviewTowerRange-Komponente
     * 
     * @param entity zu platzierender Turm
     */
    public void placeTower(Entity entity) {
        
        towerCreationSystem.setHovering(false);
        entity.remove(Hover.class); 
        
        Transform transform = Transform.MAPPER.get(entity);
        Vector2 position = transform.getPosition();

        Entity troop = createTroop(position, towerCreationSystem.getTowerType());
        entity.add(new Troop(troop));
        engine.addEntity(troop);
        createVisualBox(entity);

        entity.remove(PreviewTowerRange.class);
    }

    /**
     * createVisualBox erstellt eine unsichtbare Hitbox für platzierte Türme
     * diese wird benoetigt, da sonst Truppen von Tuermen, welche in der Theorie weiter entfernt liegen, vorne liegende Tuerme überdecken koennen
     */
    public void createVisualBox(Entity entity){
        Transform transform = Transform.MAPPER.get(entity);
        Vector2 position = transform.getPosition();

        Hitbox hitbox = Hitbox.MAPPER.get(entity);
        Rectangle bounds = hitbox.getBounds();
        float offset = 0;

        switch (towerCreationSystem.getTowerType()) {
            case "Tower1":
                offset = Offset.TOWER1_VISUALBOX_Y;
                break;
            case "Tower2":
                offset = Offset.TOWER2_VISUALBOX_Y;
                break;
            case "Tower3":
                offset = Offset.TOWER3_VISUALBOX_Y;
                break;
            case "CatapultTower":
                offset = Offset.CATAPULT_TOWER_VISUALBOX_Y;
                break;
            case "WizardTower1":
                offset = Offset.WIZARD_TOWER1_VISUALBOX_Y;
                break;
            case "WizardTower2":
                offset = Offset.WIZARD_TOWER2_VISUALBOX_Y;
                break;
            case "WizardTower3":
                offset = Offset.WIZARD_TOWER2_VISUALBOX_Y;
                break;
        }
        entity.add(new Visualbox(new Vector2(position.x, position.y+offset), bounds.width, 4f));
    }


    /**
     * createTroop erstellt die Turmtruppe eines Turms
     * 
     * - Anhaengen "normaler" Komponenten fuer die Turmtruppe
     * - Anhaengen "spezifischer" Komponenten (Transform, Graphic) an die Turmtruppe
     * - Anpassung Guthaben
     * 
     * @param position Position des Turms
     * @param tower Art des Turms
     * @return Turmtruppe
     */
    public Entity createTroop(Vector2 position, String tower) {
        Entity entity = engine.createEntity();
        Tower towerData = Tower.getType(tower).createInstance();

        TextureRegion region = assetService.get(AtlasAsset.OBJECTS).findRegion(towerData.troopName + "/s_down");

        entity.add(new additionalTowerData(
                tower,
                towerData.upgradeCost,
                towerData.totalCost,
                towerData.upgradable,
                towerData.upgradeTo,
                towerData.buyCost
        ));
        
        entity.add(new ShootingData(
                towerData.projectilespeed,
                towerData.damage,
                towerData.cooldown,
                towerData.projectileType,
                towerData.effect
        ));

        entity.add(new Animation2D(
                AtlasAsset.OBJECTS,
                towerData.troopName,
                Animation2D.AnimationType.IDLE,
                Animation.PlayMode.LOOP,
                0.65f
        ));

        // Anpassung truppenspezifischer Komponenten
        whichTroop(entity, towerData, position, region);

        entity.add(new TowerRange(Transform.MAPPER.get(entity).getPosition().cpy(), towerData.range));

        entity.add(new Facing(Facing.FacingDirection.DOWN));

        // Anpassung Guthaben
        cashSystem.setCashAmount(cashSystem.getCashAmount() - towerData.buyCost);

        return entity;
    }

    /**
     * whichTroop passt die Position und die Erscheinung (Groeße) an die jeweiligen Turmtruppen an
     * 
     * @param entity Turmtruppe der noch Komponenten fehlen
     * @param towerData Turmdaten
     * @param position noch nicht angepasste Position der Turmtruppe
     * @param region Ausschnitt aus dem TexturAatlas
     */
    private void whichTroop(Entity entity, Tower towerData, Vector2 position, TextureRegion region) {
        Vector2 transformPosition = new Vector2(position.x, position.y);
        
        Vector2 size = new Vector2();

        int z = 2;

        if (towerData.troopName.equals("Archer1")|| towerData.troopName.equals("Archer2") || towerData.troopName.equals("Archer3")) {
            transformPosition.y += Offset.ARCHER_OFFSET_Y;
            size.set(24, 24);
        
        } else if (towerData.troopName.equals("Catapult")) {
            transformPosition.y += Offset.CATAPULT_OFFSET_Y;
            size.set(34, 34);
        
        } else if (towerData.troopName.equals("Wizard1")) {
            transformPosition.y += Offset.WIZARD1_OFFSET_Y;
            transformPosition.x += Offset.WIZARD1_OFFSET_X;
            size.set(22, 22);

        } else if (towerData.troopName.equals("Wizard2")){
            transformPosition.y += Offset.WIZARD2_OFFSET_Y;
            transformPosition.x += Offset.WIZARD2_OFFSET_X;
            size.set(22, 22);
            z =-1; // Anpassung z, damit Truppe nicht sichtbar ist
        }

        entity.add(new Graphic(region, Color.WHITE.cpy()));
        entity.add(new Transform(
                transformPosition.cpy(),
                z,                        
                size.scl(Main.UNIT_SCALE),
                new Vector2(1, 1),        
                0f,                       
                0           
        ));
    }
}
