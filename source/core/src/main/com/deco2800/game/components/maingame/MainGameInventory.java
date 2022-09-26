package com.deco2800.game.components.maingame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameInventory extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table1;
    private Table table2;

    private Texture inventoryTexture;
    private TextureRegionDrawable inventory;
    private ImageButton inventoryFrame;


    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table1 = new Table();
        table1.center();
//        table1.setSize(50f,50f);
        table1.setFillParent(true);

        inventoryTexture = new Texture(Gdx.files.internal("images/popup-border.png"));
        inventory = new TextureRegionDrawable(inventoryTexture);
        inventoryFrame = new ImageButton(inventory,inventory);
        inventoryFrame.setSize(100,100);

        table1.add(inventoryFrame);
//        table1.setVisible(false);
        stage.addActor(table1);

    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table1.clear();
        super.dispose();
    }
}
