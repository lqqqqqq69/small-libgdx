package io.github.lqqqqqq69.ui.view;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import io.github.lqqqqqq69.ui.model.MenuViewModel;

/**
 * MenuView erstellt und verwaltet das Hauptmenü des Spiels
 */
public class MenuView extends View<MenuViewModel>{

    private final Image selectionImage;
    private Group selectedItem;     // aktuell ausgewähltes Item
    private TextButton textButton1; // Button für Level 1
    private TextButton textButton2; // Button für Level 2
    private TextButton textButton3; // Button für Level 3

    public MenuView(Stage stage, Skin skin, MenuViewModel viewModel){
        super(viewModel, skin, stage);

        this.selectionImage = new Image(skin, "select.9"); // Initialisierung eines "Auswahlkastens"
        this.selectionImage.setTouchable(Touchable.disabled);
        this.selectedItem = findActor(MenuOption.START_GAME1.name());   // Standardauswahl Level 1
        selectMenuItem(selectedItem);
        
    }

    /**
     * selectMenuItem erstellt eine sich bewegende Auswahlbox um ein ausgewähltes MenuItem (aufgerufen, wenn ein neues Item im Menü beruhrt wird)
     * 
     * - falls schon ein Item ausgsewählt wird -> Entfernen der alten Auswahl
     * - Setzen des aktuellen Items als selectedItem
     * - Setzen der Auswahlanimation
     * - Hinzufügen zur Stage
     * 
     * @param menuItem ausgwähltes MenuItem
     */
    private void selectMenuItem(Group menuItem){
        // Entfernen eines alten Selection Images
        if(selectionImage.getParent() != null){
            selectionImage.getParent().removeActor(selectedItem);
        }
        // Setzen des aktuellen Items als selected Item 
        this.selectedItem = menuItem;

        // Auswahlanimation
        float extraSize = 7f;
        float halfExtraSize = extraSize * 0.5f;
        float resizeTime = 0.2f;

        selectionImage.setPosition(-halfExtraSize, -halfExtraSize);
        selectionImage.setSize(menuItem.getWidth() + extraSize, menuItem.getHeight() + extraSize);

        selectionImage.clearActions();
        selectionImage.addAction(Actions.forever(Actions.sequence(
            Actions.parallel(
                Actions.sizeBy(extraSize, extraSize, resizeTime, Interpolation.linear),
                Actions.moveBy(-halfExtraSize, -halfExtraSize, resizeTime, Interpolation.linear)
            ),
            Actions.parallel(
                Actions.sizeBy(-extraSize, -extraSize, resizeTime, Interpolation.linear),
                Actions.moveBy(halfExtraSize, halfExtraSize, resizeTime, Interpolation.linear)
            )

        )));

        // Hinzufügen zur Stage
        menuItem.addActor(selectionImage);
    }

    /**
     * setupUI erstellt den grundlegenden Inhalt des Hauptmenüs
     */
    @Override
    protected void setupUI(){
        setFillParent(true);
        // Image über den Buttons und dem restliche Menüinhalt
        Image image = new Image(skin, "village_defense_logo_small");
        add(image).expandX().align(Align.top);
        row();

        setupMenuContent();
    }

    /**
     * setupMenuContent erstellt die Element des Munüs, mt welchen man interagieren
     */
    private void setupMenuContent(){
        // Initialisierung der Tabelle für das Hauptmenü
        Table contentTable = new Table();

        // Buttons zur Levelauswahl
        textButton1 = new TextButton("Start Level 1 ", skin);
        onClick(textButton1, () -> viewModel.startGame(1));
        onEnter(textButton1, this::selectMenuItem);
        textButton1.setName(MenuOption.START_GAME1.name());
        contentTable.add(textButton1).row();

        textButton2 = new TextButton("Start Level 2", skin);
        onClick(textButton2, () -> viewModel.startGame(2));
        onEnter(textButton2, this::selectMenuItem);
        contentTable.add(textButton2).row();

        textButton3 = new TextButton("Start Level 3", skin);
        onClick(textButton3, () -> viewModel.startGame(3));
        onEnter(textButton3, this::selectMenuItem);
        contentTable.add(textButton3).row();

        // Slider für die Musiklautstärke 
        Slider musicSlider = setupVolumeSlider(contentTable, "Music Volume", MenuOption.MUSIC_VOLUME);
        musicSlider.setValue(viewModel.getMusicVolume());
        onChange(musicSlider, (slider) -> viewModel.setMusicVolume(slider.getValue()));


        // Slider für die Soundlautstärke
        Slider soundSlider = setupVolumeSlider(contentTable, "Sound Volume", MenuOption.SOUND_VOLUME);
        soundSlider.setValue(viewModel.getSoundVolume());
        onChange(soundSlider, (slider) -> viewModel.setSoundVolume(slider.getValue()));

        // Button zum Schließen des Spiels
        TextButton quitButton = new TextButton("Quit Game", skin);
        contentTable.add(quitButton).padTop(10f);
        onClick(quitButton, viewModel::quitGame);
        onEnter(quitButton, this::selectMenuItem);
        contentTable.row();

        add(contentTable).row();
    }

    /**
     * setUpVolumeSlider erstellt einen Slider mit dazugehörigem Label
     * 
     * @param contentTable zugehörige Tabelle
     * @param title LabelText
     * @param menuOption Art des Sliders
     * @return  Slider
     */
    private Slider setupVolumeSlider(Table contentTable, String title, MenuOption menuOption){
        // Initialisierung Slider-Tabelle
        Table table = new Table();

        // Slider-Label
        Label label = new Label(title, skin, "cherry_white");
        table.add(label).row();

        // Slider
        Slider slider = new Slider(0f, 1f, 0.05f, false, skin, "default-horizontal");
        table.add(slider).width(200).padTop(5).row();

        // Hinzufügen zur Haupttabelle
        contentTable.add(table).padTop(10f).row();
        onEnter(table, this::selectMenuItem);
        return slider;
    }
    
    
    
    private enum MenuOption {
        START_GAME1,
        START_GAME2,
        START_GAME3,
        MUSIC_VOLUME,
        SOUND_VOLUME,
        QUIT_GAME
    }
}
