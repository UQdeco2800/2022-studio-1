package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Text;

public class MainGameTutorials extends UIComponent {

    private Table objective;
    private Table hints;
    private final Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
    private boolean woodObjComp = false;
    private boolean stoneObjComp = false;
    private Image woodObjective;
    private Image stoneObjective;
    private Image treeInteract;
    private Image stoneInteract;
    private Image enemyInteract;


    @Override
    public void create() {
        super.create();
        player.getEvents().addListener("showHints", this::displayHints);
        player.getEvents().addListener("showObjective", this::displayObjective);
        addActors();
    }

    private void addActors() {

        objective = new Table();
        objective.top();
        objective.padTop(50);
        objective.setFillParent(true);

        hints = new Table();
        hints.bottom();
        hints.padBottom(20);
        hints.setFillParent(true);

        //objective images
        Texture woodObjectiveImage = new Texture(Gdx.files.internal("images/tutorials/woodObjective.png"));
        woodObjective = new Image(woodObjectiveImage);
        Texture stoneObjectiveImage = new Texture(Gdx.files.internal("images/tutorials/stoneObjective.png"));
        stoneObjective = new Image(stoneObjectiveImage);

        //action button prompts
        Texture treeInteractImage = new Texture(Gdx.files.internal("images/tutorials/treeDialogue.png"));
        treeInteract = new Image(treeInteractImage);
        Texture stoneInteractImage = new Texture(Gdx.files.internal("images/tutorials/stoneDialogue.png"));
        stoneInteract = new Image(stoneInteractImage);
        Texture enemyInteractImage = new Texture(Gdx.files.internal("images/tutorials/enemyDialogue.png"));
        enemyInteract = new Image(enemyInteractImage);

        objective.add(woodObjective);
        objective.row();
        objective.add(stoneObjective);

        stage.addActor(objective);
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

    private void displayObjective() {
        int currentWood = player.getComponent(InventoryComponent.class).getWood();
        int currentStone = player.getComponent(InventoryComponent.class).getStone();

        if (!woodObjComp && currentWood > 100 ){
            woodObjective.remove();
            woodObjComp = true;
        }
        if (!stoneObjComp && currentStone > 50) {
            stoneObjective.remove();
            stoneObjComp = true;
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
