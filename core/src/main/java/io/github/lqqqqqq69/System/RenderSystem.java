package io.github.lqqqqqq69.System;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.lqqqqqq69.InputService.InputService;
import io.github.lqqqqqq69.Main;
import io.github.lqqqqqq69.component.Graphic;
import io.github.lqqqqqq69.component.PreviewTowerRange;
import io.github.lqqqqqq69.component.TowerRange;
import io.github.lqqqqqq69.component.Transform;

/**
 * RenderSystem rendert die Map, die Entitäten auf dieser und Turmreichweiten
 */
public class RenderSystem extends SortedIteratingSystem implements Disposable {
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Batch batch;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final InputService inputService;

    // Groesse der Map
    private static final int MAP_WIDTH = 30;
    private static final int MAP_HEIGHT = 20;

    // ShapeRender zum Anzeigen der Reichweiten
    private final ShapeRenderer shapeRenderer;

    public RenderSystem(Batch batch, Viewport viewport, OrthographicCamera camera, InputService inputService) {
        super(Family.all(Transform.class, Graphic.class).get(),Comparator.comparing(Transform.MAPPER::get));

        this.inputService = inputService;
        this.batch = batch;
        this.viewport = viewport;
        this.camera = camera;
        this.mapRenderer = new OrthogonalTiledMapRenderer(null, Main.UNIT_SCALE, this.batch);

        this.shapeRenderer = new ShapeRenderer();
    }

    /**
     * dispose gibt die Ressouren des MapRenders und des ShapeRenderers frei
     */
    @Override
    public void dispose() {
        mapRenderer.dispose();
        shapeRenderer.dispose();
    }

    /**
     * setMap setzt die Map und die Kamera auf die Mitte dieser
     * @param map zu setzende TiledMap
     */
    public void setMap(TiledMap map) {
        this.mapRenderer.setMap(map);

        // Kamera auf die Mitte der Map setzen
        camera.position.set(MAP_WIDTH / 2f, MAP_HEIGHT / 2f, 0);
        camera.update();
    }

    /**
     * update rendert pro Frame alles Anzuzeigende
     * 
     * - Anwenden Viewport
     * - Rendern der Map
     * - Rendern der Enitities in Reihenfolge
     * - Anzeigen der PreviewTowerRange von Tuermen
     * - Falls Enter gedrueckt: Anzeige aller normalen Turmreichweiten
     * 
     */
    @Override
    public void update(float deltaTime) {
        viewport.apply();

        batch.setColor(Color.WHITE);

        // Map rendern
        mapRenderer.setView(camera);
        mapRenderer.render();

        // Entities rendern
        forceSort();
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        super.update(deltaTime); // Aufruf processEntity in (durch den Comperator erstellten) Reihenfolge
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined); 
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        
        for (Entity entity : getEngine().getEntitiesFor(Family.all(Graphic.class).get())) {
            // Anzeige PreviewTowerRange
            PreviewTowerRange previewRange = PreviewTowerRange.MAPPER.get(entity);
            if (previewRange != null) {
                shapeRenderer.setColor(0, 1, 0, 1);
                shapeRenderer.circle(
                    previewRange.getRange().x,
                    previewRange.getRange().y,
                    previewRange.getRange().radius,
                    40
                );
            }

            // Anzeige TowerRange, wenn Enter gedrueckt
            TowerRange towerRange = TowerRange.MAPPER.get(entity);
            if (inputService.KEY_ENTER && towerRange != null){
                    shapeRenderer.setColor(0, 0, 1, 1);
                    shapeRenderer.circle(
                    towerRange.getRange().x,
                    towerRange.getRange().y,
                    towerRange.getRange().radius,
                    40
                );
            }

        }
        shapeRenderer.end();
    }
    
    /**
     * processEntity enthält die "Routine" zum Rendern von Entitäten
     * 
     * - Falls Graphic-Komponente der Entity = null -> return
     * - Zeichnen der Entität
     */
    @Override
    public void processEntity(Entity entity, float deltaTime) {
        Transform transform = Transform.MAPPER.get(entity);
        Graphic graphic = Graphic.MAPPER.get(entity);

        if (graphic.getRegion() == null) {
            return; // Kein Grafik-Asset vorhanden
        }

        Vector2 position = transform.getPosition();
        Vector2 scaling = transform.getScaling();
        Vector2 size = transform.getSize(); 

        this.batch.setColor(graphic.getColor());
        this.batch.draw(
            graphic.getRegion(),
            position.x - size.x / 2f, 
            position.y - size.y / 2f,
            size.x / 2f,         
            size.y / 2f,              
            size.x,
            size.y,
            scaling.x,
            scaling.y,
            transform.getRotationDeg()
        );
    }

}
