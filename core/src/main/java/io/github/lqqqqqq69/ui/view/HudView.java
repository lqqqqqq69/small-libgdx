package io.github.lqqqqqq69.ui.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import io.github.lqqqqqq69.AudioService.AudioService;
import io.github.lqqqqqq69.InputService.InputService;
import io.github.lqqqqqq69.System.FailureCheckerSystem;
import io.github.lqqqqqq69.System.WaveSystem;
import io.github.lqqqqqq69.asset.SoundAsset;
import io.github.lqqqqqq69.ui.model.HudViewModel;

/**
 * Hudview enthaelt dei nicht dynamischen Objekte des HUDs sowie das Escape Menu und das Loss Menu
 */
public class HudView extends View<HudViewModel> {
    private final InputService inputService;
    private final FailureCheckerSystem failureCheckerSystem;
    private final WaveSystem waveSystem;
    private final int level;        // aktuelles Level
    private final AudioService audioService;
    private boolean menuVisible = false;
    private Table escapeMenu;       // Escape-Menue
    private Table lossMenu;         // Menue fuer die Niederlage
    private Image overlay;          // Overlay um Escape-Menue und Loss-Menue hervorzuheben
    private Image gameOverImage;    
    private boolean over = false;   // Variable ob Game Over

    
    public HudView(Stage stage, Skin skin, HudViewModel viewModel, InputService inputService, FailureCheckerSystem failureCheckerSystem, WaveSystem waveSystem, int level, AudioService audioService) {
        super(viewModel, skin, stage);
        this.inputService = inputService;
        this.failureCheckerSystem = failureCheckerSystem;
        this.waveSystem = waveSystem;
        this.level = level;
        this.audioService = audioService;
    }

    /**
     * setupUI fuehrt die Methoden zum Setup des UIs durch
     */
    @Override
    protected void setupUI() {
        setFillParent(true);

        towerHud();
        escapeMenu();
        lossMenu();

    }
    /** 
     * act prueft pro Frame, ob das Escape Menue angezeigt werden soll
     * 
     * - Ist das Level bereits vorbei (durch Fail) -> ja = return
     * - wenn Escape gedrueckt
     *  - Blockieren der restlichen Inputs 
     *  - Anzeigen EscapeMenu
     * - wenn fail
     *  - Anzeige LossMenu
     *  - Abspielen Loss-Sound
     *  - Inputs blockieren
     *  - EscapeMenu nicht mehr sichtbar
     *  - over = true
     *  
     */
    @Override
    public void act(float delta) {
        if (over == true) return; // verhindert unnoetige Vergleiche

        if (inputService.KEY_ESC) {
            // Escape
            inputService.blocked = !inputService.blocked;
            menuVisible = !menuVisible;
            escapeMenu.setVisible(menuVisible);
            overlay.setVisible(menuVisible); 
        }
        if (failureCheckerSystem.isFailure()) {
            // Niederlage
            inputService.blocked = true;

            audioService.playSound(SoundAsset.LEVEL_LOSS);

            waveSystem.setIsWaveActive(false);
            escapeMenu.setVisible(false);
            lossMenu.setVisible(true);
            overlay.setVisible(true); 
            over = true;
        }

    }


    /** 
     * towerHud erstellt oben rechts die Anzeige der Tuerme
     */
    public void towerHud() {
        // Groeße der Buttons
        float buttonWidth = 200;
        float buttonHeight = 80;

        // Initialisierung der Tabelle
        Table towerTable = new Table();
        towerTable.top().right().pad(5f);
        towerTable.setFillParent(true);

        // TextButtons (aus designtechnischen Gruenden als Buttons)
        ImageTextButton button = new ImageTextButton("Archer Tower\nCost: 40\nPress 1", skin, "default");
        button.getColor().a = 0.6f;
        towerTable.add(button).size(buttonWidth, buttonHeight).row();

        button = new ImageTextButton("Catapult Tower\nCost: 70\nPress 2", skin, "Catapult");
        button.getColor().a = 0.6f;
        towerTable.add(button).size(buttonWidth, buttonHeight).row();

        button = new ImageTextButton("Wizard Tower\nCost: 50\nPress 3", skin, "Wizard");
        button.getColor().a = 0.6f;
        towerTable.add(button).size(buttonWidth, buttonHeight).row();

        // Hinzufuegen als Actor zur Stage
        addActor(towerTable);
    }

    /** 
     * escapeMenu erstellt eine Anzeige des Escape-Menues (+ ein Overlay zur Hingergrunddimmung)
     */
    public void escapeMenu() {
        // Initialisierung der Tabelle
        escapeMenu = new Table();
        escapeMenu.setFillParent(true);

        // Buttons
        TextButton menuBtn = new TextButton("Main Menu", skin);
        onClick(menuBtn, viewModel::returnMenu);
        escapeMenu.add(menuBtn).row();

        TextButton restartBtn = new TextButton("Restart Level", skin);
        onClick(restartBtn, () -> viewModel.restartLevel(level));
        escapeMenu.add(restartBtn).row();

        escapeMenu.setVisible(false); // standardmaeßig unsichtbar
       
        // Overlay zur Hintergrunddimmung
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        overlay = new Image(new TextureRegionDrawable(new Texture(pixmap)));
        overlay.setColor(0, 0, 0, 0.85f);
        overlay.setFillParent(true);
        overlay.setVisible(false); // standardmaeßig Invisible

        // Hinzufuegen als Actors zur Stage
        addActor(overlay);      
        addActor(escapeMenu); 
    }

    /** 
     * lossMenu erstellt mittig eine Anzeige des Loss-Menues
     */
    public void lossMenu() {
        // Initialisierung der Tabelle
        lossMenu = new Table();
        lossMenu.setFillParent(true);

        // Loss-Image
        gameOverImage = new Image(skin, "GameOver_256x256-removebg-preview");
        gameOverImage.setVisible(true);
        lossMenu.add(gameOverImage).row();

        // Buttons
        TextButton menuBtn = new TextButton("Main Menu", skin);
        onClick(menuBtn, viewModel::returnMenu);
        lossMenu.add(menuBtn).row();

        TextButton restartBtn = new TextButton("Restart Level", skin);
        onClick(restartBtn, () -> viewModel.restartLevel(level));
        lossMenu.add(restartBtn).row();

        lossMenu.setVisible(false); // standardmaeßig unsichtbar
    
        // Hinzufuegen als Actor zur Stage
        addActor(lossMenu); 
    }

}
