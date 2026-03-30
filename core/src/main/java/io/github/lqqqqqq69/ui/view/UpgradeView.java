package io.github.lqqqqqq69.ui.view;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import io.github.lqqqqqq69.Main;
import io.github.lqqqqqq69.System.UpgradeCollisionSystem;
import io.github.lqqqqqq69.component.Transform;
import io.github.lqqqqqq69.component.additionalTowerData;
import io.github.lqqqqqq69.ui.model.UpgradeViewModel;

/**
 * UpgradeView kuemmert sich um die Anzeige von Buttons zum Upgraden und Entfernen von Tuermen.
 * Es kuemmert sich darum, wo sie erscheinen, wann sie erscheinen und welche Buttons ueberhaupt angezeigt werden (koennen)
 */
public class UpgradeView extends View<UpgradeViewModel>{
    private final UpgradeCollisionSystem upgradeCollisionSystem;
    private Table TowerButtonTable;         // Tabelle fuer die Upgrade- und Remove-Buttons
    private boolean isTableCreated = false; // Ob die Tabelle schon (fuer irgendeinen Turm) existiert

    public UpgradeView(Stage stage, Skin skin, UpgradeViewModel viewModel, UpgradeCollisionSystem upgradeCollisionSystem){
        super(viewModel, skin, stage);
        this.upgradeCollisionSystem = upgradeCollisionSystem;
    }
    @Override
    protected void setupUI() {}

    /**
     * act ruft pro Frame createUpdate auf
     */
    @Override
    public void act(float delta) {
        createUpdate(upgradeCollisionSystem.getCurrentlySelected());
    }


    /**
     * createUpdate gibt die Erstellung der UPgrade-Tabelle in Auftrag
     * 
     * - Falls kein Turm ausgewaehlt -> Entfernen der Tabelle und return
     * - Falls Turm ausgewaehlt aber noch keine Tabelle erstellt
     *  - Erstellung neu Tabelle
     *  - Setzen isTableCreated auf true
     * - Falls Turm ausgeaehlt aber es existiert schon eine Tabelle
     *  - Entfernung aktuelle Tabelle
     *  - Erstellen neuer Tabelle
     * 
     * @param entity ausgewaehlte (Turm-) Entitaet
     */
    public void createUpdate(Entity entity) {
        if (entity == null) {
            // nichts ausgewaehlt -> keine Tabelle erstellen/ bestehende Tabelle entfernen
            if (TowerButtonTable != null) {
                isTableCreated = false;
                TowerButtonTable.remove();
                TowerButtonTable = null;
            }
            return;
        }

        if (!isTableCreated){
            // Erstellen neue Tabelle, falls keine existiert
            createUpgradeTable(entity);
            isTableCreated = true;
        } else if (isTableCreated){
            // Entfernen alter und erstellen einer neuen Tabelle
            TowerButtonTable.remove();
            createUpgradeTable(entity);
        }

    }
    
    /**
     * createUpgradeTable erstellt Tabellen abhaengig vom ausgewaehlten Turmtyp
     * 
     * - Falls Turm upgradable und hat nur ein naechstes Level -> ein Button 
     * - Sonst, falls Turm upgradable und vom Typ "WizardTower1" -> 2 Buttons mit etwas angepasstem Text
     * - Hinzufuegen Upgrade-Button zur Tabelle
     * - Hinzufuegen Tabelle zur Stage
     * 
     * @param entity ausgewaehlter Turm
     */
    public void createUpgradeTable(Entity entity){
        additionalTowerData data = additionalTowerData.MAPPER.get(entity);
        Transform transform = Transform.MAPPER.get(entity);
            
        // Neue Tabelle erstellen
        TowerButtonTable = new Table();
        TowerButtonTable.defaults().pad(5); 


        
        if (data.isUpgradable() && data.getUpgradeCost().length == 1) {
            // Upgrade-Buttons fuer normale Tuerme
            TextButton upgradeButton1 = new TextButton("Upgrade: " + data.getUpgradeCost(0), skin);
            onClick(upgradeButton1, () -> viewModel.upgradeTower(entity,0));
            TowerButtonTable.add(upgradeButton1);      
        
        } else if (data.isUpgradable() && data.getTowerType().equals("WizardTower1")){
            // Upgrade-Buttons fuer Magiertuerme
            TextButton upgradeButton1 = new TextButton("Ice Upgrade: " + data.getUpgradeCost(0), skin);
            onClick(upgradeButton1, () -> viewModel.upgradeTower(entity,0));
            TowerButtonTable.add(upgradeButton1); 

            TextButton upgradeButton2 = new TextButton("Fire Upgrade: " + data.getUpgradeCost(1), skin);
            onClick(upgradeButton2, () -> viewModel.upgradeTower(entity,1));
            TowerButtonTable.add(upgradeButton2);
        }
        
        
        // Remove-Button
        TextButton removeButton = new TextButton("Remove", skin);
        onClick(removeButton, () -> viewModel.removeTower(entity));
        TowerButtonTable.add(removeButton);

        // Buttons unter dem Tower platzieren
        TowerButtonTable.setPosition(transform.getPosition().x * Main.UNIT_TO_UI, transform.getPosition().y * Main.UNIT_TO_UI - 80);

        stage.addActor(TowerButtonTable);
    }

}
