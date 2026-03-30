package io.github.lqqqqqq69.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.ray3k.stripe.FreeTypeSkinLoader;

/**
 * AssetService verwaltet das Laden, Abrufen und Entladen von Assets
 */
public class AssetService implements Disposable {

    private final AssetManager assetManager;

    /**
     * Der Konstruktor initialisiert den AssetManager und setzt die Loader für die TiledMaps und Skins
     * 
     * @param fileHandleResolver Resolver zum Auffinden der Assets
     */
    public AssetService(FileHandleResolver fileHandleResolver) {
        this.assetManager = new AssetManager(fileHandleResolver);

        this.assetManager.setLoader(TiledMap.class, new TmxMapLoader(fileHandleResolver));

        this.assetManager.setLoader(Skin.class, new FreeTypeSkinLoader(fileHandleResolver));
    }

    /**
     * load lädt ein Asset und gibt es wieder zurück
     * @param <T> Typ des Assets
     * @param asset Asset das geladen werden soll
     * @return das geladene Asset
     */
    public <T> T load(Asset<T> asset) {
        this.assetManager.load(asset.getAssetDescriptor());
        this.assetManager.finishLoading(); 
        return this.assetManager.get(asset.getAssetDescriptor());
    }
    
    /**
     * unlaod entlädt ein Asset und gibt so Ressourcen frei
     * @param <T> Typ des Assets 
     * @param asset Asset das entladen werden soll
     */
    public <T> void unload(Asset<T> asset) {
        this.assetManager.unload(asset.getAssetDescriptor().fileName);
    }
   
    /**
     * queue fügt Assets zur Lade-Warteschlange hinzu
     * @param <T> Typ des Assets
     * @param asset Asset das geladen werden soll
     */
    public <T> void queue(Asset<T> asset) {
        this.assetManager.load(asset.getAssetDescriptor());
    }

    
    public <T> T get(Asset<T> asset) {
        return this.assetManager.get(asset.getAssetDescriptor());
    }

    /**
     * update aktualisiert den AssetManager
     * @return aktualisierten AssetManager
     */
    public boolean update() {
        return this.assetManager.update();
    }


    /**
     * dipose gibt alle Ressourcen des AssetManagers frei
     */
    @Override
    public void dispose() {
        this.assetManager.dispose();
    }

    public AssetManager getAssetManager() {
        return this.assetManager;
    }
    
}
