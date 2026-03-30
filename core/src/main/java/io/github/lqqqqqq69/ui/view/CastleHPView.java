package io.github.lqqqqqq69.ui.view;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import io.github.lqqqqqq69.Main;
import io.github.lqqqqqq69.System.CashSystem;
import io.github.lqqqqqq69.System.WaveSystem;
import io.github.lqqqqqq69.component.Health;
import io.github.lqqqqqq69.component.Transform;
import io.github.lqqqqqq69.ui.model.CastleHPViewModel;

/**
 * CastleHPView enthaelt, erstellt und aktualisert alle UI-Elemente mit dynamischer Anzeige
 */
public class CastleHPView extends View<CastleHPViewModel> {
    private Table HPTable;          // Tabelle mit Inhalten zu Tuer-HP
    private Label hpLabel;          // Label mit der genauen HP-Anzeige der Tuer
    private Label waveActiveLabel;  // Label, welches anzeigt, dass eine Welle (und welche Welle) aktiv ist
    private ProgressBar hpBar;      // (ungenaue) Anzeige der Tuer-HP als Progress Bar
    private Entity door;            // Tuer
    private int maxDoorHealth;      // maximale HP der Tuer
    private int currentDoorHealth;  // aktuelle HP der Tuer
    private Health health;          
    private ImageTextButton imageTextButton;    // Anzeige fuer das aktuelle Guthaben
    private final int level;                    // aktuelles Level
    private final CashSystem cashSystem;
    private final WaveSystem waveSystem;


    public CastleHPView(Stage stage, Skin skin, CastleHPViewModel viewModel, Entity door, CashSystem cashSystem, WaveSystem waveSystem, int level) {
        super(viewModel, skin, stage);
        this.door = door;
        this.health = Health.MAPPER.get(door);
        this.maxDoorHealth = 0;
        this.currentDoorHealth = 0;
        this.cashSystem = cashSystem;
        this.waveSystem = waveSystem;
        this.level = level;
    }

    /**
     * setupUI erstellt die grundlegende Tabellen fuer die CastleHP, die Geldanzeige sowie das WaveActiveLabel
     */
    @Override
    protected void setupUI() {
        // Aufsetzung der grundlegenden Tabelle fuer die HP-Anzeige
        HPTable = new Table();
        hpLabel = new Label("", skin, "cherry_white");
        HPTable.add(hpLabel).row();

        hpBar = new ProgressBar(0,maxDoorHealth, 1f, false, skin, "hpbar");
        HPTable.add(hpBar);
        
        addActor(HPTable); //Hinzufuegung HP-Tabelle als Actor (spaetere Positionierung)

        // Credit Anzeige
        Table table = new Table();
        table.align(Align.topLeft);             // Positionierung oben links
        table.setFillParent(true);

        imageTextButton = new ImageTextButton("Current Credits:", skin, "cash");
        table.add(imageTextButton).pad(5f);
        stage.addActor(table);

        // waveActiveLabel
        waveActiveLabel = new Label("wave active", skin, "cherry_white_big");
        waveActiveLabel.setFillParent(true);
        waveActiveLabel.setAlignment(Align.bottomRight);
        waveActiveLabel.setVisible(false);

        stage.addActor(waveActiveLabel);
    }

    /**
     * act aktualsiert pro Frame die Anzeige der dynamischen UI Elemente
     * - Ausfuehrung castleHealthUIUpdate und cashUIUpdate
     * - wenn Welle aktiv, dann Anzeige des WaveActiveLabels
     * - Aktualisierung des WaveActiveLabels, anhand der aktuellen Welle
     */
    @Override
    public void act(float delta) {
        castleHealthUIUpdate();

        cashUIUpdate();

        waveActiveLabel.setVisible(waveSystem.isWaveActive());
        if (waveSystem.isWaveActive()){
            waveActiveLabel.setText("wave " + waveSystem.getWaveNr() + " / " + waveSystem.getMaxWaveNr());
        }
    }

   
    /**
     * castleHealthUIUpdate platzierte und aktualisiert die HP-Anzeige der zu verteidigenden Tuer
     * 
     * - wenn health != und maxDoorHealth == 0 (Tuer existiert aber Tabelle noch nicht erstellt)
     *  - Erstellen Tabelle anhand Tuerposition
     *  - Grundinitialisierung HP-Anzeige
     * - sonst, wenn Tuer + HP-Anzeige bereits existiert und sich die HP der Tuer geaendert haben
     *  - Update der currentDorrHealth und HP-Anzeige
     */
    private void castleHealthUIUpdate(){
        if (health != null && maxDoorHealth == 0){
            //Positionierung der Tabelle
            Transform transform = Transform.MAPPER.get(door);
            
            switch (level) {
                case 3 -> HPTable.setPosition((transform.getPosition().x+3.5f) * Main.UNIT_TO_UI, (transform.getPosition().y-1) * Main.UNIT_TO_UI);
                default -> HPTable.setPosition((transform.getPosition().x-1) * Main.UNIT_TO_UI, (transform.getPosition().y+2) * Main.UNIT_TO_UI);
            }
            
            //Anpassung Tabelleninhalt -> Grundinitialisierung der Werte
            this.maxDoorHealth = (int)health.getHealth();
            this.currentDoorHealth = (int)health.getHealth();
            hpLabel.setText("HP:" + currentDoorHealth + " / " + maxDoorHealth);
            hpBar.setRange(0, maxDoorHealth);
            hpBar.setValue(maxDoorHealth);

        } 
        else if (health != null && maxDoorHealth != 0 && currentDoorHealth != (int)health.getHealth()){
            currentDoorHealth = (int)health.getHealth();
            hpLabel.setText("HP:" + currentDoorHealth + " / " + maxDoorHealth);
            hpBar.setValue(currentDoorHealth);
        }  
    }

    /*
     * Aktualisierung Credit-Anzeige
    */
    private void cashUIUpdate(){
        imageTextButton.setText("Current Credits: " + cashSystem.getCashAmount());
    }
}

    
