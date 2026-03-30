package io.github.lqqqqqq69.asset;
import com.badlogic.gdx.assets.AssetDescriptor;

/**
 * Asset ist ein Interface, welches von allen Asset-Typen implementiert wird 
 * und den passenden Asset-Descriptor (zum Laden eines Asset) bereit stellt
 */
public interface Asset<T> {
    AssetDescriptor<T> getAssetDescriptor();
}
