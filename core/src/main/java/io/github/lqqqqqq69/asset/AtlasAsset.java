package io.github.lqqqqqq69.asset;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * AtlasAsset definiert den Objektatlas (Atlas, der auf alle Arten von Assets/ Sprites verweist - Zwecks Animation)
 * und gibt einen Deskriptor auf diesen wieder
 */
public enum AtlasAsset implements Asset<TextureAtlas>{
    OBJECTS("Objects.atlas");

    private final AssetDescriptor<TextureAtlas> descriptor;

    /**
     * Der Konstruktor erzeugt einen neuen AssetDescriptor für den übergebenen Atlas
     * 
     * @param fileName Name der Asset-Datei
     */
    AtlasAsset(String fileName) {
        this.descriptor = new AssetDescriptor<>("graphics/" + fileName, TextureAtlas.class);
    }

    @Override
    public AssetDescriptor<TextureAtlas> getAssetDescriptor() {
        return descriptor;
    }

}
