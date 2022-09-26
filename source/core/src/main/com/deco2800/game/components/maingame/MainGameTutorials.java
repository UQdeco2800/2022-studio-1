package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainGameTutorials extends UIComponent {

    private Table hints;
    private final Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
    private Image treeInteract;
    private Image stoneInteract;
    private Image enemyInteract;


    @Override
    public void create() {
        super.create();
        player.getEvents().addListener("showHints", this::displayHints);
        addActors();
    }

    private void addActors() {
        hints = new Table();
        hints.bottom();
        hints.padBottom(100);
        hints.setFillParent(true);

        Texture treeInteractImage = new Texture(Gdx.files.internal("images/tutorials/treeDialogue.png"));
        treeInteract = new Image(treeInteractImage);
        Texture stoneInteractImage = new Texture(Gdx.files.internal("images/tutorials/stoneDialogue.png"));
        stoneInteract = new Image(stoneInteractImage);
        Texture enemyInteractImage = new Texture(Gdx.files.internal("images/tutorials/enemyDialogue.png"));
        enemyInteract = new Image(enemyInteractImage);

        stage.addActor(hints);
    }

    private void displayHints() {
        Entity currentPlayer = MainArea.getInstance().getGameArea().getPlayer();
        Entity closestEnemy = ServiceLocator.getEntityService().findClosestEnemy((int) currentPlayer.getPosition().x,
                (int) currentPlayer.getPosition().y);
        Entity closestEntity = ServiceLocator.getEntityService().findClosetEntity((int) currentPlayer.getPosition().x,
                (int) currentPlayer.getPosition().y);

        hints.clear();
        if (ServiceLocator.getDayNightCycleService().getCurrentCycleStatus() != DayNightCycleStatus.NIGHT) {
            if (closestEntity != null && closestEntity.isCollectable()) {
                ResourceType getResource = closestEntity.getResourceType();
                switch (getResource) {
                    case WOOD -> hints.add(treeInteract);
                    case STONE -> hints.add(stoneInteract);
                    case GOLD -> { //TODO: add gold dialogue
                    }
                }
            }
        } else if (closestEnemy != null) {
            hints.add(enemyInteract);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        hints.clear();

        super.dispose();
    }
}
