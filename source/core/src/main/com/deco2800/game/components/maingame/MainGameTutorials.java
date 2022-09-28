package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

public class MainGameTutorials extends UIComponent {
    private Table hints;
    private Table objective;
    private final Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
    private Image uncheckedWoodTickBox;
    private Image uncheckedStoneTickBox;
    private Texture tickBoxImage;
    private boolean woodObjComp = false;
    private boolean stoneObjComp = false;
    private static Label woodDisplay;
    private static Label stoneDisplay;
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
        objective.padTop(20);
        objective.setFillParent(true);

        hints = new Table();
        hints.bottom();
        hints.padBottom(20);
        hints.setFillParent(true);

        Texture objectiveImage = new Texture(Gdx.files.internal("images/tutorials/objectiveImage.png"));
        Image objectiveImg = new Image(objectiveImage);

        int woodCountInt = player.getComponent(InventoryComponent.class).getWood();
        CharSequence woodCount = String.format("Gather 100 wood:   %d / 100  ", woodCountInt);
        woodDisplay = new Label(String.valueOf(woodCount), skin, "large");

        //objective mine stone
        int stoneCountInt = player.getComponent(InventoryComponent.class).getStone();
        CharSequence stoneCount = String.format("Mine 60 stone:   %d / 60  ", stoneCountInt);
        stoneDisplay = new Label(String.valueOf(stoneCount), skin, "large");

        //tickBoxes
        Texture emptyTickBoxImage = new Texture(Gdx.files.internal("images/tutorials/uncheckedTickBox.png"));
        tickBoxImage = new Texture(Gdx.files.internal("images/tutorials/checkedTickBox.png"));
        uncheckedWoodTickBox = new Image(emptyTickBoxImage);
        uncheckedStoneTickBox = new Image(emptyTickBoxImage);

        //action button prompts
        Texture treeInteractImage = new Texture(Gdx.files.internal("images/tutorials/treeDialogue.png"));
        treeInteract = new Image(treeInteractImage);
        Texture stoneInteractImage = new Texture(Gdx.files.internal("images/tutorials/stoneDialogue.png"));
        stoneInteract = new Image(stoneInteractImage);
        Texture enemyInteractImage = new Texture(Gdx.files.internal("images/tutorials/enemyDialogue.png"));
        enemyInteract = new Image(enemyInteractImage);

        objective.add(objectiveImg).width(425).height(138).top().center();
        objective.row();
        objective.add(woodDisplay);
        objective.add(uncheckedWoodTickBox).width(25).height(25);
        objective.row();
        objective.add(stoneDisplay);
        objective.add(uncheckedStoneTickBox).width(25).height(25);

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

        CharSequence currWoodCount = String.format("Gather 100 wood:   %d / 100  ", currentWood);
        woodDisplay.setText(currWoodCount);
        CharSequence currStoneCount = String.format("Mine 60 stone:   %d / 60  ", currentStone);
        stoneDisplay.setText(currStoneCount);

        if (!woodObjComp && currentWood >= 100 ){
            uncheckedWoodTickBox.setDrawable(new SpriteDrawable(new Sprite(tickBoxImage)));
            woodObjComp = true;
        }
        if (!stoneObjComp && currentStone >= 60) {
            uncheckedStoneTickBox.setDrawable(new SpriteDrawable(new Sprite(tickBoxImage)));
            stoneObjComp = true;
        }
        if (stoneObjComp && woodObjComp) {
            objective.clear();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        hints.clear();
        objective.clear();
        super.dispose();
    }
}
