package io.github.lqqqqqq69.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


/**
 * MapAsset definiert alle Arten von Maps und gibt Descriptoren diese auf Anfrage wieder
 */
public enum MapAsset implements Asset<TiledMap> {
   
    MAP1("Map1.tmx"),
    MAP2("Map2.tmx"),
    MAP3("Map3.tmx");
    private final AssetDescriptor<TiledMap> descriptor;
    

    /**
     * Der Konstruktor erzeugt einen neuen AssetDescriptor für die übergebene Map
     * 
     * @param mapName Name der Asset-Datei
     */
    MapAsset(String mapName) {
        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        parameters.projectFilePath = "maps/map.tiled-project";
        this.descriptor = new AssetDescriptor<>("maps/" + mapName, TiledMap.class, parameters);
    }
    
    @Override
    public AssetDescriptor<TiledMap> getAssetDescriptor() {
        return this.descriptor;
    }



}
