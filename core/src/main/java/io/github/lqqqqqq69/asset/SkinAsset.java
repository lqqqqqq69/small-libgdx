package io.github.lqqqqqq69.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * SkinAsset definiert den für die Erstellung des UIs verwendeten Skin und gibt diesen auf Anfrage wieder
 */
public enum SkinAsset implements Asset<Skin> {
    DEFAULT("skin.json");

    private final AssetDescriptor<Skin> descriptor;

    /**
     * Der Konstruktor erzeugt einen neuen AssetDescriptor für die übergebene Skin-Datei
     * 
     * @param skinJsonFile Name der Asset-Datei
     */
    SkinAsset(String skinJsonFile) {
        this.descriptor = new AssetDescriptor<>("ui/" + skinJsonFile, Skin.class);
    }

    @Override
    public AssetDescriptor<Skin> getAssetDescriptor() {
        return descriptor;
    }
}
