package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.ui.UIComponent;


public class MainGameNpcInterface extends UIComponent{

        private Table ConversationUI;

        private boolean isVisible;


        @Override
        public void create() {
            super.create();
            addActors();
        }

        public void addActors() {

        }

        public Table makeUIPopUp(Boolean value, float x, float y) {
            float uiWidth = 800f;
            float uiHeight = 300f;
            float screenHeight = Gdx.graphics.getHeight();
            float screenWidth = Gdx.graphics.getWidth();

            x = screenWidth - uiWidth;
            y = screenHeight - uiHeight;

            isVisible = value;

            ConversationUI = new Table();
            ConversationUI.setSize(uiWidth, uiHeight);
            ConversationUI.setPosition(x, y);

            ConversationUI.setVisible(isVisible);

            Texture colour = new Texture(Gdx.files.internal("images/pop-up background.png"));
            Drawable backgroundColour = new TextureRegionDrawable(colour);

            Label npcLabel = new Label("Atlantis NPC Talking...", skin, "large");


            ConversationUI.add(npcLabel);
            ConversationUI.setBackground(backgroundColour);
            stage.addActor(ConversationUI);

            return ConversationUI;
        }



        @Override
        public void draw(SpriteBatch batch) {
            // draw is handled by the stage
        }


        @Override
        public void dispose() {
            super.dispose();
        }

}
