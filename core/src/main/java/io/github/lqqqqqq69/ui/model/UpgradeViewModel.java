package io.github.lqqqqqq69.ui.model;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import io.github.lqqqqqq69.Main;
import io.github.lqqqqqq69.System.CashSystem;
import io.github.lqqqqqq69.System.TowerCreationSystem;
import io.github.lqqqqqq69.System.TowerPlacementSystem;
import io.github.lqqqqqq69.component.Transform;
import io.github.lqqqqqq69.component.Troop;
import io.github.lqqqqqq69.component.additionalTowerData;

/**
 * UpgradeViewModel enthält die von UpgradeView verwendeten Methoden fuer:
 * - Upgrade eines Turms
 * - Entfernung eines Turms
 */
public class UpgradeViewModel extends ViewModel {
    private final Engine engine;
    private final CashSystem cashSystem;
    private final TowerPlacementSystem towerPlacementSystem;
    private final TowerCreationSystem towerCreationSystem;
    
    public UpgradeViewModel(Main game, Engine engine, CashSystem cashSystem, TowerCreationSystem towerCreationSystem, TowerPlacementSystem towerPlacementSystem) {
        super(game);
        this.engine = engine;
        this.cashSystem = cashSystem;
        this.towerCreationSystem = towerCreationSystem;
        this.towerPlacementSystem = towerPlacementSystem;
    }

    /**
     * upgradeTower beinhaltet die Methodik zum Upgrade eines Turms
     * 
     * - Genug Guthaben vorhanden? nein -> return
     * - Erstellung und PLatzierung eines neuen Towers an der aktuellen Position
     * - Entfernung des alten Turms (+ Turmtruppe)
     * 
     * @param entity ausgewählte (Turm-) Entität
     * @param index Index des ausgewählten Upgrades (wichtig bei Magiertuermen)
     */
    public void upgradeTower(Entity entity, int index){
        Troop troop = Troop.MAPPER.get(entity);
        additionalTowerData additionalData = additionalTowerData.MAPPER.get(entity);
        Transform transform = Transform.MAPPER.get(entity);
        
        // genug Guthaben?
        if (additionalData.getUpgradeCost(index) > cashSystem.getCashAmount()) return;

        // Erstellung neuer Turm
        Entity newTower = towerCreationSystem.createTower(transform.getPosition(), additionalData.getUpgradeTo(index));
        this.engine.addEntity(newTower);
        towerPlacementSystem.placeTower(newTower);

        // Entfernung alter Turm
        this.engine.removeEntity(troop.getTroop());
        this.engine.removeEntity(entity);
    }

    /**
     * removeTower beinhaltet die Methodik zum Entfernen eines Turms
     * 
     * - Entfernen des Turms
     * - Rueckerstattungn von 75% der Gesamtkosten des Turms
     * 
     * @param entity
     */
    public void removeTower(Entity entity){
        Troop troop = Troop.MAPPER.get(entity);
        additionalTowerData additionalData = additionalTowerData.MAPPER.get(entity);
        this.engine.removeEntity(troop.getTroop());
        this.engine.removeEntity(entity);
        cashSystem.setCashAmount(cashSystem.getCashAmount()+ (int)(0.75*additionalData.getTotalCost()));

    }
    
}
