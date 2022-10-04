package com.deco2800.game.components.maingame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.components.storyline.storyLineAction;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.screens.PrologueScreen;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.areas.MainArea;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.deco2800.game.areas.MainArea;
import com.deco2800.game.components.achievements.AchievementPopupComponent;
import com.deco2800.game.components.infrastructure.ResourceType;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

@ExtendWith(GameExtension.class)
public class MainGameTutorialTest {
    

    @Test
    void movementPromptTest() {

    }

    @Test 
    void woodPromptTest() {

        MainGameTutorials actions = new MainGameTutorials();


        Entity currentPlayer = MainArea.getInstance().getGameArea().getPlayer();
        Entity closestEntity = ServiceLocator.getEntityService().findClosetEntity((int) currentPlayer.getPosition().x,
                (int) currentPlayer.getPosition().y);

        closestEntity.setResourceType(ResourceType.WOOD);
        closestEntity.setCollectable(true);

        //detect wood prompt
        Table promptTable = actions.getPromptsTable();
        Texture stoneInteractImage = new Texture(Gdx.files.internal("images/tutorials/stoneDialogue_revised.png"));
        Image stoneInteract = new Image(stoneInteractImage);

        verify(promptTable).add(stoneInteract);
    }

    @Test
    void stonePromptTest() {

        MainGameTutorials actions = new MainGameTutorials();


        Entity currentPlayer = MainArea.getInstance().getGameArea().getPlayer();
        Entity closestEntity = ServiceLocator.getEntityService().findClosetEntity((int) currentPlayer.getPosition().x,
                (int) currentPlayer.getPosition().y);

        closestEntity.setResourceType(ResourceType.STONE);
        closestEntity.setCollectable(true);

        //detect stone prompt
        Table promptTable = actions.getPromptsTable();


        Texture treeInteractImage = new Texture(Gdx.files.internal("images/tutorials/woodDialogue_revised.png"));
        Image treeInteract = new Image(treeInteractImage);

        verify(promptTable).add(treeInteract);


    }

    @Test
    void objectiveShows() {

    }

    @Test
    void objectiveDisposes() {
        
    }

    @Test
    void objectiveTicks() {
        
    }
}
