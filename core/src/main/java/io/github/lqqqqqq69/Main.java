package io.github.lqqqqqq69;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.lqqqqqq69.AudioService.AudioService;
import io.github.lqqqqqq69.asset.AssetService;
import io.github.lqqqqqq69.asset.AtlasAsset;
import io.github.lqqqqqq69.asset.SkinAsset;
import io.github.lqqqqqq69.asset.SoundAsset;

/**
 * Main ist die Hauptklasse des Spiels und verwaltet die Spielumgebung
 */
public class Main extends Game {
    // Weltgroeße
    public static final float SCREEN_WIDTH = 30f;
    public static final float SCREEN_HEIGHT = 20f;

    // 1 Feld auf TiledMap = 16 Pixel
    public static final float UNIT_SCALE = 1f / 16f; 

    public static final float UNIT_TO_UI = 40f;


    // Globale Objekte fuer Rendering und Assets
    private Batch batch;                       
    private OrthographicCamera camera;         
    private Viewport viewport;                 
    private AssetService assetService;  
    private InputMultiplexer inputMultiplexer;
    private AudioService audioService;

    // Cache fuer Screens
    private final Map<Class<? extends Screen>, Screen> screenCache = new HashMap<>();

    /**
     * create initialisiert die wichtigsten globalen Objekte
     * 
     * - Erzeugt batch, camera und viewport 
     * - Initalisiert asset- und audioService
     * - laedt die Assets
     * - laedt die jeweiligen JSON-Dateien
     * - registriert die Screens und setzt den Anfangsscreen
     */
    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera(); // ist in dem Spiel eher unnoetig
        this.viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);

        this.assetService = new AssetService(new InternalFileHandleResolver());
        this.audioService = new AudioService(assetService);

        // Laden der Asssets
        assetService.load(SkinAsset.DEFAULT);
        assetService.load(AtlasAsset.OBJECTS);  

        for (SoundAsset soundAsset : SoundAsset.values()) {
            assetService.load(soundAsset);
        }


        // JSONs laden
        Enemy.loadEnemies("Enemy.json");
        Defend.loadDefends("Defend.json");
        Tower.loadTowers("tower.json");

        // Screens registrieren
        addScreen(new GameScreen(this, 1));
        addScreen(new GameScreen(this, 2));
        addScreen(new GameScreen(this, 3));
        addScreen(new MainMenuScreen(this));

        setScreen(MainMenuScreen.class);
    }

   
    /**
     * addScreen fuegt einen Screen dem internen Cache hinzu 
     * 
     * @param screen Screen, der hinzugefuegt werden soll
     */
    public void addScreen(Screen screen) {
        screenCache.put(screen.getClass(), screen);
    }

    /**
     * removeScreen entfernt einen Screen aus dem internen Cache
     * 
     * @param screen Screen, der entfernt werden soll
     */
    public void removeScreen(Screen screen ) {
        screenCache.remove(screen.getClass());
    }


    /**
     * setScreen setzt einen neuen aktiven Screen
     * @param screenClass
     */
    public void setScreen(Class<? extends Screen> screenClass) {
        Screen screen = screenCache.get(screenClass);
        if (screen == null) {
            throw new GdxRuntimeException("No screen with class " + screenClass + " found in the screen cache");
        }
        super.setScreen(screen);
    }

    /**
     * dispose gibt alle Ressourcen frei, wenn das Spiel geschlossen wird
     */
    @Override
    public void dispose() {
        screenCache.values().forEach(Screen::dispose);
        screenCache.clear();

        this.assetService.dispose();

        this.batch.dispose();
    }

    /**
     * resize passt die Groeße des Viewports auf neue Screengroeßen an
     * 
     * @param width neue Breite
     * @param height neue Hoehe
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        super.resize(width, height);
    }
    
    public Batch getBatch() {
        return this.batch;
    }

    public Viewport getViewport() {
        return this.viewport;
    }

    public AssetService getAssetService() {
        return this.assetService;
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }
    /**
     * setInputPrcessors setzt die Input-Prozessoren
     * @param processors 
     */
    public void setInputProcessors(InputProcessor... processors) {
        inputMultiplexer.clear();
        if (processors == null) return;

        for (InputProcessor processor : processors) {
            inputMultiplexer.addProcessor(processor);
        }
    }

    public AudioService getAudioService(){
        return audioService;
    }
}
