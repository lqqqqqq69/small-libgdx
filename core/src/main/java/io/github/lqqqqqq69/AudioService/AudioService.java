package io.github.lqqqqqq69.AudioService;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;

import io.github.lqqqqqq69.asset.AssetService;
import io.github.lqqqqqq69.asset.MusicAsset;
import io.github.lqqqqqq69.asset.SoundAsset;

/**
 * AudioService verwaltet Musik und Soundeffekte inklusive ihrer Lautstärke
 */
public class AudioService {

    private final AssetService assetService;  
    private Music currentMusic;             // aktuell spielende Musik
    private MusicAsset currentMusicAsset;   // aktuelles Musik-Asset
    private float musicVolume;              // Musiklautstärke 
    private float soundVolume;              // Soundlautstärke 

    public AudioService(AssetService assetService){
        this.assetService = assetService;
        this.currentMusic = null;
        this.currentMusicAsset = null;
        this.musicVolume = 0.2f;
        this.soundVolume = 0.3f;
    }

    /**
     * setMusicVolume setzt die Musiklautstärke
     *
     * @param musicVolume gewünschte Lautstärke
     */
    public void setMusicVolume(float musicVolume) {
        this.musicVolume = MathUtils.clamp(musicVolume, 0f, 1f);
        if (this.currentMusic != null){
            this.currentMusic.setVolume(this.musicVolume);
        }
    }

    /**
     * setSoundVolume setzt die Lautstärke für Soundeffekte
     *
     * @param soundVolume gewünschte Lautstärke
     */
    public void setSoundVolume(float soundVolume) {
        this.soundVolume = MathUtils.clamp(soundVolume, 0f, 1f);
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    /**
     * playMusic startet einen neuen "Musik-Track"
     *
     * - Falls derselbe Track bereits läuft -> kein Wechsel
     * - Sonst alten Track stoppen und entladen, neuen laden und starten
     *
     * @param musicAsset zu spielendes Musik-Asset
     */

    public void playMusic(MusicAsset musicAsset){
        if (this.currentMusicAsset == musicAsset) return; // bereits aktiv

        // Vorherige Musik stoppen und entladen
        if (this.currentMusic != null){
            this.currentMusic.stop();
            this.assetService.unload(this.currentMusicAsset);
        }

        // Neue Musik laden & starten
        this.currentMusic = this.assetService.load(musicAsset);
        this.currentMusic.setLooping(true);
        this.currentMusic.setVolume(musicVolume);
        this.currentMusic.play();
        this.currentMusicAsset = musicAsset;
    }

    /**
     * playSound spielt einen Soundeffekt mit der gewählten Lautstärke
     *
     * @param soundAsset zu spielender Sound
     */
    public void playSound(SoundAsset soundAsset){
        this.assetService.get(soundAsset).play(soundVolume);
    }
}