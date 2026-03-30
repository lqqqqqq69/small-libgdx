package io.github.lqqqqqq69.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;

/**
 * SoundAsset definiert alle Arten von Sounds und gibt diese auf Anfrage wieder
 */
public enum SoundAsset implements Asset<Sound>{
    GOBLIN_ATTACK("Goblin.mp3"),
    SLIME_ATTACK("Slime.mp3"),
    WOLF_ATTACK("Wolf.wav"),
    LIZARD_ATTACK("Lizard.mp3"),
    GOBLINRIDER_ATTACK("GoblinRider.mp3"),
    SEAGULL_ATTACK("Seagull.mp3"),
    GOLEM_ATTACK("GolemAttack.mp3"),

    GOLEM_SPAWN("GolemSpawn.mp3"),

    GOLEM_DEATH("GolemDeath.mp3"),
    
    ARCHER_ATTACK("Archer.mp3"),
    CATAPULT_ATTACK("Catapult.mp3"),
    WIZARD_ATTACK("Wizard.mp3"),
    
    ARCHERTOWER_SELECT("ArcherTowerSelect.mp3"),
    CATAPULTTOWER_SELECT("CatapultTowerSelect.mp3"),
    WIZARDTOWER_SELECT("WizardTowerSelect.mp3"),
    
    TOWER_PLACEMENT("TowerPlacement.mp3"),
    TOWER_PLACEMENT_INVALID("TowerPlacementInvalid.mp3"),

    LEVEL_WIN("Win.mp3"),
    LEVEL_LOSS("Loss.mp3");

    private final AssetDescriptor<Sound> descriptor;
    
    /**
     * Der Konstruktor erzeugt einen neuen AssetDescriptor für den übergebenen Sound
     * 
     * @param soundFile Name der Asset-Datei
     */
    SoundAsset(String soundFile){
        this.descriptor = new AssetDescriptor<>("audio/"+ soundFile, Sound.class);
    }
    
    @Override
    public AssetDescriptor<Sound> getAssetDescriptor(){
        return descriptor;
    }

}