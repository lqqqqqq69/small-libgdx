package io.github.lqqqqqq69;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.lqqqqqq69.asset.MusicAsset;
import io.github.lqqqqqq69.asset.SkinAsset;
import io.github.lqqqqqq69.ui.model.MenuViewModel;
import io.github.lqqqqqq69.ui.view.MenuView;

/**
 * MainMenuScreen repraesentiert das Hauptmenue
 */
public class MainMenuScreen extends ScreenAdapter {

    private final Main game;         
    private final Stage stage;          // Stage (auf welcher das UI aufgebaut ist)           
    private final Skin skin;            // Arten von Elementen, welche im UI angezeigt werden koennen 
    private final Viewport uiViewport;  


    public MainMenuScreen(Main game) {
        this.game = game;
        this.uiViewport = new FitViewport(800f, 450f);
        this.stage = new Stage(uiViewport, game.getBatch());
        this.skin = game.getAssetService().get(SkinAsset.DEFAULT);
    
    }

    /**
     * resize passt den Viewport auf neue Screengroeßen an
     * 
     * @param width neue Breite
     * @param height neue Hoehe
     */
    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height, true);
    }

    /**
     * show bereitet alles fuer die Anzeige des Hauptmenues vor
     * 
     * - Setzt den Input-Prozessor
     * - Fuegt den Actor MenuView hinzu
     * - Spielt Musik ab
     */
    @Override
    public void show() {
        // Inputprozessor setzen
        Gdx.input.setInputProcessor(stage);
        
        // MenuView hinzufuegen
        stage.addActor(new MenuView(stage, skin, new MenuViewModel(game)));

        // Musik abspielen
        game.getAudioService().playMusic(MusicAsset.MENUMUSIC);
    }

    /**
     * hide leert die Stage
     */ 
    @Override
    public void hide() {
        stage.clear();
    }

    /**
     * render aktualisiert den Screen pro Frame
     * 
     * - Leeren des Screens
     * - Update der UI-Stage
     */
    @Override
    public void render(float delta) {
        // Screen leeren - verhindert Grafik-Bugs
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiViewport.apply();

        // Stage updaten
        stage.act(delta);
        stage.draw();
    }

    /**
     * dispose gibt (bspw. bei Start eines Levels) alle Ressourcen frei
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}

