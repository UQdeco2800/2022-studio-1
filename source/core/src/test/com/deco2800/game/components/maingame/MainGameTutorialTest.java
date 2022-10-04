package com.deco2800.game.components.maingame;

import static org.mockito.Mockito.verify;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.areas.MainArea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;


@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class MainGameTutorialTest {
    @Mock MainGameTutorials actions;
    @Mock EntityService service;
    @Mock DayNightCycleService cycleService;
    @Mock Entity entity; 

    @Test
    void movementPromptTest() {

        Entity player = service.getNamedEntity("player");
        player.getEvents().trigger("playerControlTut", "UP");
        player.getEvents().trigger("playerControlTut", "DOWN");
        player.getEvents().trigger("playerControlTut", "LEFT");
        player.getEvents().trigger("playerControlTut", "RIGHT");
        player.getEvents().trigger("playerControlTut", "SPACE");

        Table control = actions.getControlTable();

        verify(control).clear();
    }

    @Test 
    void woodPromptTest() {

        Entity currentPlayer = MainArea.getInstance().getGameArea().getPlayer();
        Entity closestEntity = ServiceLocator.getEntityService().findClosetEntity((int) currentPlayer.getPosition().x,
                (int) currentPlayer.getPosition().y);

        closestEntity.setResourceType(ResourceType.WOOD);
        closestEntity.setCollectable(true);

        Table promptTable = actions.getPromptsTable();
        Texture stoneInteractImage = new Texture(Gdx.files.internal("images/tutorials/stoneDialogue_revised.png"));
        Image stoneInteract = new Image(stoneInteractImage);

        verify(promptTable).add(stoneInteract);
    }

    @Test
    void stonePromptTest() {

        Entity player = service.getNamedEntity("player");
        Entity closestEntity = service.findClosetEntity((int) player.getPosition().x,
                (int) player.getPosition().y);

        closestEntity.setResourceType(ResourceType.STONE);
        closestEntity.setCollectable(true);

        Table promptTable = actions.getPromptsTable();

        Texture treeInteractImage = new Texture(Gdx.files.internal("images/tutorials/woodDialogue_revised.png"));
        Image treeInteract = new Image(treeInteractImage);

        verify(promptTable).add(treeInteract);


    }

    @Test
    void enemiePromtTest() {

        cycleService.setPartOfDayTo(DayNightCycleStatus.NIGHT);
        Table prompts = actions.getPromptsTable();

        Entity player = service.getNamedEntity("player");
        Entity closestEnemy = ServiceLocator.getEntityService().findClosestEnemy((int) currentPlayer.getPosition().x,
                (int) player.getPosition().y);

        Texture enemyInteractImage = new Texture(Gdx.files.internal("images/tutorials/enemyDialogue_revised.png"));
        Image enemyInteract = new Image(enemyInteractImage);

        if (closestEnemy != null) {
            verify(prompts).add(enemyInteract);
        }
    }

    // @Test
    // void resourceObjectiveShows() {
    //     MainGameTutorials actions = new MainGameTutorials();
    // }

    @Test
    void resourceObjectiveDisposes() {

        Entity player = MainArea.getInstance().getGameArea().getPlayer();
        player.getComponent(InventoryComponent.class).setWood(100);
        player.getComponent(InventoryComponent.class).setStone(60);

        player.getEvents().trigger("updateObjective");

        Table objective = actions.getObjectiveTable();

        verify(objective).clear();
    }

    // @Test
    // void objectiveTicks() {
    //     MainGameTutorials actions = new MainGameTutorials();
    //     Entity player = ServiceLocator.getEntityService().getNamedEntity("player");

    //     player.getEvents().trigger("updateObjective");

    // }
}
