package io.github.lqqqqqq69.tiled;

import java.util.function.Consumer;

import com.badlogic.gdx.maps.tiled.TiledMap;

import io.github.lqqqqqq69.asset.AssetService;
import io.github.lqqqqqq69.asset.MapAsset;

/**
 * TiledService laedt und verwaltet die aktuelle Tiled-Map
 */
public class TiledService {
    private final AssetService assetService;
    private TiledMap currentMap;            // aktuelle Map
    private Consumer<TiledMap> mapChangeConsumer;

    public TiledService(AssetService assetService) {
        this.assetService = assetService;
    }

    /**
     * loadMap lädt eine Map über den AssetService
     * 
     * @param mapAsset zu ladende Map
     * @return geladen Map
     */
    public TiledMap loadMap(MapAsset mapAsset) {
        TiledMap tiledMap = this.assetService.load(mapAsset);
        tiledMap.getProperties().put("mapAsset", mapAsset);
        return tiledMap;
    }

    /**
     * setMap setzt die aktuelle Map und entlädt gegebenenfalls die alte Map
     * 
     * @param map aktuelle Map
     */
    public void setMap(TiledMap map) {
        if (this.currentMap != null) {
            // alte Map entladen
            MapAsset old = this.currentMap.getProperties().get("mapAsset", MapAsset.class);
            if (old != null) {
                this.assetService.unload(old);
            }
        }

        // (neue) Map setzen
        this.currentMap = map;


        if (this.mapChangeConsumer != null) {
            this.mapChangeConsumer.accept(map);
        }
    }

    public void setMapChangeConsumer(Consumer<TiledMap> mapChangeConsumer) {
        this.mapChangeConsumer = mapChangeConsumer;
    }

    public TiledMap getCurrentMap() {
        return this.currentMap;
    }

    public AssetService getAssetService() {
        return this.assetService;
    }
}
