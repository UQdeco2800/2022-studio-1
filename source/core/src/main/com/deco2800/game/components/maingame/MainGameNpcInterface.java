package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;


public class MainGameNpcInterface extends UIComponent{

        private Table ConversationUI;

        private boolean isVisible;
        private Image NpcImage;
        private Table LeftTable;



    @Override
        public void create() {
            super.create();
            addActors();
        }

        public void addActors() {

        }

        public Table makeUIPopUp(Boolean value, float x, float y) {
            float uiWidth = 950f;
            float uiHeight = 300f;
            float screenWidth = Gdx.graphics.getWidth();
            isVisible = value;

            ConversationUI = new Table();
            ConversationUI.setSize(uiWidth, uiHeight);
            ConversationUI.setPosition(screenWidth/4,0);
            ConversationUI.setVisible(isVisible);

            Texture colour = new Texture(Gdx.files.internal("images/pop-up background.png"));
            Drawable backgroundColour = new TextureRegionDrawable(colour);

            String[] NPC_dialogues = { "The fate of Atlantis, is in your hands",
                    "You are the chosen one....... Chiron" };

            int index = (int) ((Math.random() * (NPC_dialogues.length)));
            
            Label npcLabel = new Label(NPC_dialogues[index], skin, "large");
            NpcImage = new Image(ServiceLocator.getResourceService().getAsset("images/NpcPlaceholder.png", Texture.class ));
            LeftTable = new Table();
            LeftTable.left();
            LeftTable.padLeft(30f);
            //LeftTable.setFillParent(true);

            LeftTable.add(NpcImage).center();
            ConversationUI.add(LeftTable).size(225f, 300f);
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
