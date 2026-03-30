package io.github.lqqqqqq69.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

/**
 * additionalTowerData ist ein Komponent, welcher diverse Daten für Türmen enthält in Bezug auf deren Upgrades
 * Zusätzlich dient er auch zur Unterscheidung zwischen Türmen und anderen Entitäten
 */
public class additionalTowerData implements Component {
    private String towerType;   // Art des Turms
    private int buyCost;        // Kosten für das aktuelle Upgrade
    private int[] upgradeCost;  // Kosten für dei folgenden Upgrades
    private int totalCost;      // Gesamtkosten bis zur aktuellen Stufe
    private boolean upgradable; // hat der Turm ein Upgrade?
    private String[] upgradeTo; // Arten der möglichen Upgrades

    public static final ComponentMapper<additionalTowerData> MAPPER = ComponentMapper.getFor(additionalTowerData.class);

    public additionalTowerData(String towerType, int[] upgradeCost, int totalCost, boolean upgradable, String[] upgradeTo, int buyCost) {
        this.towerType = towerType;
        this.upgradeCost = upgradeCost;
        this.buyCost = buyCost;
        this.totalCost = totalCost;
        this.upgradable = upgradable;
        this.upgradeTo = upgradeTo;
    }

    public String getTowerType() {
        return towerType;
    }

    /**
     * getUpgradeCost gibt die Kosten für das gewählte Upgrade wieder
     * 
     * @param arrayIndex Index des gewählten Upgrades
     * @return Kosten des Upgrades
     */
    public int getUpgradeCost(int arrayIndex) {
        return upgradeCost[arrayIndex];
    }

    public int getTotalCost() {
        return totalCost;
    }

    public boolean isUpgradable() {
        return upgradable;
    } 

    public String getUpgradeTo(int arrayIndex) {
        return upgradeTo[arrayIndex];
    }

    public int getBuyCost() {
        return buyCost;
    }

    public int[] getUpgradeCost() {
        return upgradeCost;
    }

}
