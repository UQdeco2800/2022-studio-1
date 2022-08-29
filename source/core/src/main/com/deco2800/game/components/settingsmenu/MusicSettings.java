package com.deco2800.game.components.settingsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MusicSettings extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MusicSettings.class);

    private Table musicTable;
    private Boolean musicStatus;

    public MusicSettings(AtlantisSinks game) {
        super();
    }


    @Override
    public void create() {
        super.create();
        addActors();
    }


    //Making buttons and event handlers
    private void addActors() {

        musicTable = new Table();
        musicTable.top();
        musicTable.setFillParent(true);

        musicTable.add(musicButton());

        stage.addActor(musicTable);
    }

    private Table musicButton() {
        Table table = new Table();
        table.setFillParent(true);

        //Sound Button
        Texture soundTexture = new Texture(Gdx.files.internal("images/uiElements/exports/music_on.png"));
        TextureRegionDrawable upSound = new TextureRegionDrawable(soundTexture);
        TextureRegionDrawable downSound = new TextureRegionDrawable(soundTexture);
        ImageButton musicButton = new ImageButton(upSound,downSound);

        table.add(musicButton);

        musicButton.addListener((EventListener) event -> {
        musicStatus = false;
        Texture soundOffTexture = new Texture(Gdx.files.internal("images/uiElements/exports/music_off.png"));
        TextureRegionDrawable upOffSound = new TextureRegionDrawable(soundTexture);
        TextureRegionDrawable downOffSound = new TextureRegionDrawable(soundTexture);
        ImageButton soundOffButton = new ImageButton(upSound,downSound);

        logger.debug("Music turned off");
        return musicStatus;
    });

        return table;
    }


    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void update() {
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public void dispose() {
        musicTable.clear();
        super.dispose();
    }
}

