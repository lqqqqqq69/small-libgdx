package io.github.lqqqqqq69.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;

/**
 * MusicAsset definiert alle Arten von Musik/ Songs und gibt diese auf Anfrage aus
 */
public enum MusicAsset implements Asset<Music>{
    MENUMUSIC("Chai Kingdom - Super Mario Land.mp3"),
    LEVEL1MUSIC("Clash of Clans music ost - Home Village 1.mp3"),
    LEVEL2MUSIC("Clash of Clans music ost - Halloween Home Village 1.mp3"),
    LEVEL3MUSIC("Clash of Clans music ost - Winter Home Village 1.mp3"),
    FINALBOSSMUSIC("SneakyGolemSong - Dark is the Night.mp3"); // Dieser Song ist Teil eines Memes und per se unproblematisch - Notfalls googeln Sie gerne die Lyrics :)


    private final AssetDescriptor<Music> descriptor;

    /**
     * Der Konstruktor erzeugt einen neuen AssetDescriptor für die übergebene Musik
     * 
     * @param musicFile Name der Asset-Datei
     */
    MusicAsset(String musicFile){
        this.descriptor = new AssetDescriptor<>("audio/"+ musicFile, Music.class);
    }

    @Override
    public AssetDescriptor<Music> getAssetDescriptor(){
        return descriptor;
    }

}
