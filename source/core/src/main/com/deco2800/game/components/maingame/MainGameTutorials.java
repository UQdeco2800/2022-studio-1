package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.Environmental.EnvironmentalComponent;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

public class MainGameTutorials extends UIComponent {
    private Table prompts;
    private Table objectiveHeader;
    private Table objective;
    private Table control;
    private Table shopArrow;
    private final Entity player = ServiceLocator.getEntityService().getNamedEntity("player");
    private Image objectiveImg;
    private Image uncheckedWoodTickBox;
    private Image uncheckedStoneTickBox;
    private Image uncheckedEnemyTickBox;
    private Texture tickBoxImage;
    private static boolean objectiveStatus = true;
    private static boolean woodObjComp = false;
    private static boolean stoneObjComp = false;
    private static boolean enemyObjComp = false;
    private static int enemyCountInt;
    private static Label woodDisplay;
    private static Label stoneDisplay;
    private static Label enemyDisplay;
    private Image treeInteract;
    private Image stoneInteract;
    private Image enemyInteract;
    private static boolean playerControlComp = true;

    private static boolean up = false;
    private static boolean down = false;
    private static boolean left = false;
    private static boolean right = false;
    private static boolean space = false;


    @Override
    public void create() {
        super.create();
        player.getEvents().addListener("showPrompts", this::displayPrompts);
        player.getEvents().addListener("updateObjective", this::updateObjective);
        player.getEvents().addListener("enemyKill", this::onEnemyKill);
        player.getEvents().addListener("playerControlCompTut", this::onPlayerControl);
        ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
                this::onNight);
        addActors();
    }


    private void addActors() {

        shopArrow = new Table();
        shopArrow.setFillParent(true);

        objectiveHeader = new Table();
        objectiveHeader.top();
        objectiveHeader.padTop(10);
        objectiveHeader.setFillParent(true);

        objective = new Table();
        objective.top();
        objective.padTop(150);
        objective.setFillParent(true);

        control = new Table();
        control.bottom();
        control.padBottom(50);
        control.setFillParent(true);

        prompts = new Table();
        prompts.bottom();
        prompts.padBottom(150);
        prompts.setFillParent(true);

        //arrowDirection background
/*        Texture shopArrowTexture = new Texture(Gdx.files.internal("images/tutorials/shopArrow.png"));
        TextureRegionDrawable shopArrowBackground = new TextureRegionDrawable(shopArrowTexture);
        shopArrow.setBackground(shopArrowBackground);*/

        //objective Header image
        Texture objectiveImage = new Texture(Gdx.files.internal("images/tutorials/objectiveImage.png"));
        objectiveImg = new Image(objectiveImage);

        //player control prompt:
        //only shows the first time game plays
        Texture playControl = new Texture(Gdx.files.internal("images/tutorials/playerControlComps.png"));
        Image playerControlComps = new Image(playControl);
        if (playerControlComp) {
            control.add(playerControlComps).width(461).height(187);
        }

        //objective chop tree
        int woodCountInt = player.getComponent(InventoryComponent.class).getWood();
        CharSequence woodCount = String.format("Gather 100 wood:   %d / 100  ", woodCountInt);
        woodDisplay = new Label(String.valueOf(woodCount), skin, "large");

        //objective mine stone
        int stoneCountInt = player.getComponent(InventoryComponent.class).getStone();
        CharSequence stoneCount = String.format("Mine 60 stone:   %d / 60  ", stoneCountInt);
        stoneDisplay = new Label(String.valueOf(stoneCount), skin, "large");

        //objective kill enemies
        enemyCountInt = 0;
        CharSequence enemyCount = String.format("Kill 3 enemies:   %d / 3  ", enemyCountInt);
        enemyDisplay = new Label(String.valueOf(enemyCount), skin, "large");

        //tickBoxes
        Texture emptyTickBoxImage = new Texture(Gdx.files.internal("images/tutorials/uncheckedTickBox.png"));
        tickBoxImage = new Texture(Gdx.files.internal("images/tutorials/checkedTickBox.png"));
        uncheckedWoodTickBox = new Image(emptyTickBoxImage);
        uncheckedStoneTickBox = new Image(emptyTickBoxImage);
        uncheckedEnemyTickBox = new Image(emptyTickBoxImage);

        //action button prompts
        Texture treeInteractImage = new Texture(Gdx.files.internal("images/tutorials/treeDialogue.png"));
        treeInteract = new Image(treeInteractImage);
        Texture stoneInteractImage = new Texture(Gdx.files.internal("images/tutorials/stoneDialogue.png"));
        stoneInteract = new Image(stoneInteractImage);
        Texture enemyInteractImage = new Texture(Gdx.files.internal("images/tutorials/enemyDialogue.png"));
        enemyInteract = new Image(enemyInteractImage);

        //show objective Header image only when objectives are active
        if (objectiveStatus) {
            objectiveHeader.add(objectiveImg).width(425).height(138).top().center();
        }

        //only renders the objective if it's incomplete
        if (!stoneObjComp) {
            objective.add(stoneDisplay);
            objective.add(uncheckedStoneTickBox).width(25).height(25);
            objective.row();
        }
        if (!woodObjComp) {
            objective.add(woodDisplay);
            objective.add(uncheckedWoodTickBox).width(25).height(25);
            objective.row();
        }

        stage.addActor(objectiveHeader);
        stage.addActor(objective);
        stage.addActor(prompts);
        stage.addActor(control);
    }

    private void displayPrompts() {
        Entity currentPlayer = MainArea.getInstance().getGameArea().getPlayer();
        Entity closestEnemy = ServiceLocator.getEntityService().findClosestEnemy((int) currentPlayer.getPosition().x,
                (int) currentPlayer.getPosition().y);
        Entity closestEntity = ServiceLocator.getEntityService().findClosetEntity((int) currentPlayer.getPosition().x,
                (int) currentPlayer.getPosition().y);

        prompts.clear();
        if (ServiceLocator.getDayNightCycleService().getCurrentCycleStatus() != DayNightCycleStatus.NIGHT) {
            if (closestEntity != null && closestEntity.isCollectable()) {
                ResourceType getResource = closestEntity.getResourceType();
                switch (getResource) {
                    case WOOD -> prompts.add(treeInteract);
                    case STONE -> prompts.add(stoneInteract);
                    case GOLD -> { //TODO: add gold dialogue
                    }
                }
            }
        } else if (closestEnemy != null) {
            prompts.add(enemyInteract);
        }
    }

    private void updateObjective() {
        int currentWood = player.getComponent(InventoryComponent.class).getWood();
        int currentStone = player.getComponent(InventoryComponent.class).getStone();

        CharSequence currWoodCount = String.format("Gather 100 wood:   %d / 100  ", currentWood);
        woodDisplay.setText(currWoodCount);
        CharSequence currStoneCount = String.format("Mine 60 stone:   %d / 60  ", currentStone);
        stoneDisplay.setText(currStoneCount);

        if (!woodObjComp && currentWood >= 100 ){
            uncheckedWoodTickBox.setDrawable(new SpriteDrawable(new Sprite(tickBoxImage)));
            woodObjComp = true;
        } if (!stoneObjComp && currentStone >= 60) {
            uncheckedStoneTickBox.setDrawable(new SpriteDrawable(new Sprite(tickBoxImage)));
            stoneObjComp = true;
        } if (stoneObjComp && woodObjComp) {
            objectiveStatus = false;
            objective.clear();
            objectiveHeader.clear();
            //shopObjective();
        }
    }

    //remove the player control prompt after the user has pressed the buttons
    private void onPlayerControl(String controlType) {
        switch (controlType) {
            case "UP": up = true;
            case "DOWN": down = true;
            case "LEFT": left = true;
            case "RIGHT": right = true;
            case "SPACE": space = true;
        }
        if (up && down && right && left && space) {
            control.clear();
            playerControlComp = false;
        }
    }

    //displays the "attack tower" objective and adds the arrow screen overlay
    private void shopObjective() {
        Label towerDisplay = new Label("Build an attack tower", skin, "large");

        objective.add(towerDisplay);
        stage.addActor(shopArrow);
    }

    //clears the objective and display the "enemy kill" objective
    private void onNight(DayNightCycleStatus partOfDay) {
        if (partOfDay == DayNightCycleStatus.NIGHT) {
            objective.clear();
            objectiveStatus = true;
            objectiveHeader.add(objectiveImg).width(425).height(138).top().center();
            objective.add(enemyDisplay);
            objective.add(uncheckedEnemyTickBox).width(25).height(25);
            objective.row();
            Label surviveNight = new Label("Survive the night", skin, "large");
            objective.add(surviveNight);
        }
    }

    //counts and updates the enemy kill number
    private void onEnemyKill() {
        enemyCountInt += 1;
        CharSequence currEnemyCount = String.format("Kill 3 enemies:   %d / 3  ", enemyCountInt);
        enemyDisplay.setText(currEnemyCount);

        if (!enemyObjComp && enemyCountInt >= 3) {
            uncheckedEnemyTickBox.setDrawable(new SpriteDrawable(new Sprite(tickBoxImage)));
            enemyObjComp = true;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        prompts.clear();
        control.clear();
        objective.clear();
        super.dispose();
    }
}
