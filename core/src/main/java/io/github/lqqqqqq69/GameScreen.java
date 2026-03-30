package io.github.lqqqqqq69;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.lqqqqqq69.AudioService.AudioService;
import io.github.lqqqqqq69.InputService.InputService;
import io.github.lqqqqqq69.System.AnimationSystem;
import io.github.lqqqqqq69.System.CashSystem;
import io.github.lqqqqqq69.System.CastleCollisionSystem;
import io.github.lqqqqqq69.System.CreateMouseCollision;
import io.github.lqqqqqq69.System.DoorStageSystem;
import io.github.lqqqqqq69.System.EntityFailRemovalSystem;
import io.github.lqqqqqq69.System.FacingSystem;
import io.github.lqqqqqq69.System.FailureCheckerSystem;
import io.github.lqqqqqq69.System.HealthSystem;
import io.github.lqqqqqq69.System.HitboxUpdateSystem;
import io.github.lqqqqqq69.System.HoverSystem;
import io.github.lqqqqqq69.System.MoveSystem;
import io.github.lqqqqqq69.System.PathCollisionCreation;
import io.github.lqqqqqq69.System.PathingSystem;
import io.github.lqqqqqq69.System.PlacementValidationSystem;
import io.github.lqqqqqq69.System.PreviewTowerRangeUpdateSystem;
import io.github.lqqqqqq69.System.ProjectileCollisionSystem;
import io.github.lqqqqqq69.System.RenderSystem;
import io.github.lqqqqqq69.System.TargetingSystem;
import io.github.lqqqqqq69.System.TowerCreationSystem;
import io.github.lqqqqqq69.System.TowerPlacementSystem;
import io.github.lqqqqqq69.System.TowerRangeUpdateSystem;
import io.github.lqqqqqq69.System.UpgradeCollisionSystem;
import io.github.lqqqqqq69.System.WaveSystem;
import io.github.lqqqqqq69.asset.AssetService;
import io.github.lqqqqqq69.asset.MapAsset;
import io.github.lqqqqqq69.asset.MusicAsset;
import io.github.lqqqqqq69.asset.SkinAsset;
import io.github.lqqqqqq69.tiled.TiledService;
import io.github.lqqqqqq69.ui.model.CastleHPViewModel;
import io.github.lqqqqqq69.ui.model.FinishViewModel;
import io.github.lqqqqqq69.ui.model.HudViewModel;
import io.github.lqqqqqq69.ui.model.UpgradeViewModel;
import io.github.lqqqqqq69.ui.view.CastleHPView;
import io.github.lqqqqqq69.ui.view.FinishView;
import io.github.lqqqqqq69.ui.view.HudView;
import io.github.lqqqqqq69.ui.view.UpgradeView;


/**
 * GameScreen repraesentiert den eigentlichen Screen fuer ein Level
 */
public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final AssetService assetService;
    private final Batch batch;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Engine engine;
    private final int level;

    // Services & Systeme
    private final TiledService tiledService;
    private final TowerCreationSystem towerCreationSystem;
    private final AudioService audioService;
    private final CashSystem cashSystem;
    private final UpgradeCollisionSystem upgradeCollisionSystem;
    private final DoorStageSystem doorStageSystem;
    private final InputService inputService;
    private final FailureCheckerSystem failureCheckerSystem;
    private final WaveSystem waveSystem;

    // UI
    private final Stage uiStage;
    private final Viewport uiViewport;
    private final Skin uiSkin;
    private final HudViewModel hudViewModel;
    private final CastleHPViewModel castleHPViewModel;
    private final UpgradeViewModel upgradeViewModel;
    private final FinishViewModel finishViewModel;

    /**
     * Der Konstruktor erstellt einen neuen GameScreen fuer das angegebene Level
     * Er initialisiert Engine, Services, Systeme und UI-ViewModels
     * 
     * @param game Referenz auf die Hauptklasse
     * @param level aktuelles Level
     */
    public GameScreen(Main game, int level) {
        this.level = level;
        this.game = game;
        this.assetService = game.getAssetService();
        this.batch = game.getBatch();
        this.viewport = game.getViewport();
        this.camera = game.getCamera();
        this.engine = new Engine();

        // Services
        inputService = new InputService();
        engine.addSystem(inputService);
        tiledService = new TiledService(assetService);
        audioService = game.getAudioService();
        
        // Systeme vorbereiten
        towerCreationSystem = new TowerCreationSystem(engine, viewport, assetService, inputService);
        cashSystem = new CashSystem(level);
        PlacementValidationSystem placementValidationSystem = new PlacementValidationSystem(cashSystem);
        TowerPlacementSystem towerPlacementSystem = new TowerPlacementSystem(towerCreationSystem, engine, assetService, placementValidationSystem, cashSystem, inputService, audioService);
        CreateMouseCollision mouseCollision = new CreateMouseCollision(engine, viewport);
        upgradeCollisionSystem = new UpgradeCollisionSystem(mouseCollision, inputService, audioService);

        // UI vorbereiten
        uiViewport = new FitViewport(Main.UNIT_TO_UI * 30f, Main.UNIT_TO_UI * 20f);
        uiStage = new Stage(uiViewport, batch);
        uiSkin = assetService.get(SkinAsset.DEFAULT);
        hudViewModel = new HudViewModel(game);
        castleHPViewModel = new CastleHPViewModel(game);
        upgradeViewModel = new UpgradeViewModel(game, engine, cashSystem, towerCreationSystem, towerPlacementSystem);
        finishViewModel = new FinishViewModel(game);

        // Systeme hinzufuegen
        engine.addSystem(new HitboxUpdateSystem());
        engine.addSystem(new ProjectileCollisionSystem());
        engine.addSystem(placementValidationSystem);
        waveSystem = new WaveSystem(engine, assetService, inputService, level, game, audioService);
        engine.addSystem(waveSystem);
        engine.addSystem(new CastleCollisionSystem(audioService, waveSystem));
        engine.addSystem(new TargetingSystem(assetService, waveSystem, audioService));
        engine.addSystem(upgradeCollisionSystem);
        engine.addSystem(new HealthSystem(engine, cashSystem, waveSystem, audioService));
        engine.addSystem(new AnimationSystem(assetService));
        engine.addSystem(towerCreationSystem);
        engine.addSystem(towerPlacementSystem);
        engine.addSystem(new TowerRangeUpdateSystem());
        engine.addSystem(new PreviewTowerRangeUpdateSystem());
        engine.addSystem(new PathingSystem(level));
        engine.addSystem(new HoverSystem());
        doorStageSystem = new DoorStageSystem(assetService, engine, level);
        failureCheckerSystem = new FailureCheckerSystem(doorStageSystem);
        engine.addSystem(failureCheckerSystem);
        engine.addSystem(doorStageSystem);
        engine.addSystem(new MoveSystem());
        engine.addSystem(new FacingSystem());
        engine.addSystem(new RenderSystem(batch, viewport, camera, inputService));
        engine.addSystem(new EntityFailRemovalSystem(engine));
        PathCollisionCreation pathCollision = new PathCollisionCreation(engine, level);
    }

    /**
     * show bereitet alles fuer die eigentliche Anzeige und Eingabe vor
     * 
     * - Setzt den Input-Prozessor
     * - Laedt die Map
     * - Startet die Musik (fuer die jeweilige Map)
     * - Fuegt das UI hinzu
     */
    @Override
    public void show() {
        // Input-Prozessor
        InputMultiplexer mux = new InputMultiplexer(uiStage);
        Gdx.input.setInputProcessor(mux);

        // Map laden & Musik starten
        Consumer<TiledMap> renderConsumer = engine.getSystem(RenderSystem.class)::setMap;
        tiledService.setMapChangeConsumer(renderConsumer);
        TiledMap map;
        switch (level) {
            case 1: 
                map = tiledService.loadMap(MapAsset.MAP1);
                tiledService.setMap(map);
                game.getAudioService().playMusic(MusicAsset.LEVEL1MUSIC);
                break;
            case 2 : 
                map = tiledService.loadMap(MapAsset.MAP2);
                tiledService.setMap(map);   
                game.getAudioService().playMusic(MusicAsset.LEVEL2MUSIC);
                break;
            default : 
                map = tiledService.loadMap(MapAsset.MAP3);
                tiledService.setMap(map);    
                game.getAudioService().playMusic(MusicAsset.LEVEL3MUSIC);
                break;
        }

        // UI hinzufuegen
        uiStage.addActor(new UpgradeView(uiStage, uiSkin, upgradeViewModel, upgradeCollisionSystem));
        uiStage.addActor(new CastleHPView(uiStage, uiSkin, castleHPViewModel, doorStageSystem.getDoor(), cashSystem, waveSystem, level));
        uiStage.addActor(new HudView(uiStage, uiSkin, hudViewModel, inputService, failureCheckerSystem, waveSystem, level, audioService));
        uiStage.addActor(new FinishView(uiStage, uiSkin, finishViewModel, waveSystem, inputService, failureCheckerSystem, level));
    }

    /**
     * render aktualisiert den Screen pro Frame
     * 
     * - Leeren des Screens
     * - Update der Engine
     * - Update des UIs
     */
    @Override
    public void render(float delta) {
        // Screen leeren - verhindert Grafik-Bugs
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update der Engine
        delta = Math.min(delta, 1 / 60f); // falls man zu wenig FPS haben sollte, damit das Spiel nicht zu sehr stockt
        engine.update(delta);

        // Update des UIs
        uiViewport.apply();
        uiStage.getBatch().setColor(Color.WHITE);
        uiStage.act(delta);
        uiStage.draw();
    }

    /**
     * resize passt die Viewports auf neue Screengroeßen an
     * 
     * @param width neue Breite
     * @param height neue Hoehe
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    /**
     * hide entfernt alle Entities aus der Engine und leert die UI-STage
     */
    @Override
    public void hide() {
        engine.removeAllEntities();
        uiStage.clear();
    }

    /**
     * dispose gibt (bspw. bei Neustart eines Levels) alle Ressourcen frei
     */
    @Override
    public void dispose() {
        for (EntitySystem system : engine.getSystems()) {
            if (system instanceof Disposable disposable) {
                disposable.dispose();
            }
        }
        uiStage.dispose();
    }
}
