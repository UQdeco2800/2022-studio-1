package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuidebookArea extends GameArea {

    private static final Logger logger = LoggerFactory.getLogger(GuidebookArea.class);

    private static final String GUIDEBOOK_MUSIC = "sounds/guidebookMusic.mp3";
    private static final String[] gameMusic = { GUIDEBOOK_MUSIC };

    public GuidebookArea() {
        super();
    }

    @Override
    public void create() {
        playMusic();
    }

    private void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(GUIDEBOOK_MUSIC, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(gameMusic);
    }

    public void stopMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(GUIDEBOOK_MUSIC, Music.class);
        music.stop();
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(GUIDEBOOK_MUSIC, Music.class).stop();
        this.unloadAssets();
    }
}
