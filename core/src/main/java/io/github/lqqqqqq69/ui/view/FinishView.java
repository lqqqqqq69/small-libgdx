package io.github.lqqqqqq69.ui.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import io.github.lqqqqqq69.InputService.InputService;
import io.github.lqqqqqq69.System.FailureCheckerSystem;
import io.github.lqqqqqq69.System.WaveSystem;
import io.github.lqqqqqq69.ui.model.FinishViewModel;

/**
 * FinishView bereitet die Anzeige des Victory-Screens vor und zeigt diesen an, wenn noetig
 */
public class FinishView extends View<FinishViewModel> {
    private final WaveSystem waveSystem;
    private final InputService inputService;
    private final FailureCheckerSystem failureCheckerSystem;
    private int level;          // aktuelles Level
    private boolean menuVisible = false; // wird das Menue angezeigt
    private Table victoryMenu;  // Tabelle fuer die Buttons des FInish UIs
    private Image overlay;      // verdunkelnde OVerlay
    private Image finishImage;  // angezeigtes Bild (ueber den Buttons)

    
    public FinishView(Stage stage, Skin skin, FinishViewModel viewModel, WaveSystem waveSystem, InputService inputService, FailureCheckerSystem failureCheckerSystem, int level) {
        super(viewModel, skin, stage);
        this.waveSystem = waveSystem;
        this.inputService = inputService;
        this.failureCheckerSystem = failureCheckerSystem;
        this.level = level;
        
    }

    /**
     * setupUI fuehrt die Methoden zum Setup des Victory Menues durch
     */
    @Override
    protected void setupUI() {
        setFillParent(true);

        victoryMenu();
    }

    /**
     * act testet pro Frame, ob man alle Wellen besiegt (und dabei nicht verloren) hat
     * 
     * - Wenn Sieg
     *  - Blockierung Inputs
     *  - Anzeigen Victory Screen
     */
    @Override
    public void act(float delta) {

        if (waveSystem.isLevelFinished() && !failureCheckerSystem.isFailure()) {
            inputService.blocked = true;
            victoryMenu.setVisible(true);
            overlay.setVisible(true); 
        }

    }

    /**
     * Methode zur Erstellung des Victory Menues inklusive Overlay
     */
    public void victoryMenu() {

        // Initialisierung Victory-Tabelle
        victoryMenu = new Table();
        victoryMenu.setFillParent(true);

        // Image ueber den Buttons
        finishImage = new Image(skin,"Victory_256x256");
        victoryMenu.add(finishImage).row();

        // Buttons
        TextButton menuBtn = new TextButton("Main Menu", skin);
        onClick(menuBtn, viewModel::returnMenu);
        victoryMenu.add(menuBtn).row();

        TextButton replayBtn = new TextButton("Replay Level", skin);
        onClick(replayBtn, () -> viewModel.replayLevel(level));
        victoryMenu.add(replayBtn).row();

        victoryMenu.setVisible(false); // standardmaeßig unsichtbar

        // Overlay
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        overlay = new Image(new TextureRegionDrawable(new Texture(pixmap)));
        overlay.setColor(0, 0, 0, 0.85f);
        overlay.setFillParent(true);
        overlay.setVisible(false);

        // Hinzufuegen der Actors zur Stage
        addActor(overlay);      
        addActor(victoryMenu); 
    }

}
