package com.deco2800.game.components.shop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.shop.artefacts.BestLog;
import com.deco2800.game.components.shop.artefacts.BetterLog;
import com.deco2800.game.components.shop.artefacts.ShopBuilding;
import com.deco2800.game.components.shop.artefacts.StandardLog;
import com.deco2800.game.ui.UIComponent;

/**
 * Displays UI specific to the ShopArtefactScreen
 */
public class ShopEquipmentDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(
            ShopEquipmentDisplay.class);
    private static final float Z_INDEX = 2f;

    Table table1;
    Table table2;
    Table table3;
    Table table4;
    Table table5;
    Table table6;
    Table table7;
    Table table8;

    private CircularLinkedList<ShopBuilding> stock;
    private Node<ShopBuilding> current;

    Label subtitle;

    private Texture leftTexture;
    private TextureRegionDrawable left;
    private Button leftButton;

    private Texture rightTexture;
    private TextureRegionDrawable right;
    private Button rightButton;

    private Image currentItem;
    private Texture currentTexture;

    private Texture goldenCategoryTexture;
    private TextureRegionDrawable goldenDrawable;
    private Texture brownCategoryTexture;
    private TextureRegionDrawable brownDrawable;

    private TextButton descriptionDisplay;
    private TextButton buyButton;
    private TextButton priceDisplay;

    private Texture backTexture;
    private TextureRegionDrawable upBack;
    private ImageButton backButton;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {

        table1 = new Table();
        table1.setFillParent(true);
        table1.center().bottom().padBottom(-75);

        table2 = new Table();
        table2.setFillParent(true);
        table2.center().padTop(100);

        table3 = new Table();
        table3.setFillParent(true);
        table3.center().left().padLeft(350).padTop(100);

        table4 = new Table();
        table4.setFillParent(true);
        table4.center().right().padRight(350).padTop(100);

        table5 = new Table();
        table5.setFillParent(true);
        table5.left().bottom().padLeft(250);

        table6 = new Table();
        table6.setFillParent(true);
        table6.right().bottom().padRight(250);

        table7 = new Table();
        table7.setFillParent(true);
        table7.top().left().padLeft(10).padTop(40);

        table8 = new Table();
        table8.setFillParent(true);
        table8.top().left().padLeft(75).padTop(115);

        // Create linked list of the available shop stock
        stock = new CircularLinkedList<ShopBuilding>();
        stock.add(new StandardLog());
        stock.add(new BetterLog());
        stock.add(new BestLog());
        current = stock.head;

        // Create the current artefact to display
        currentTexture = new Texture(Gdx.files.internal(current.t.getCategoryTexture()));
        currentItem = new Image(currentTexture);

        // Create textures for arrows, price, descrition and buy button
        brownCategoryTexture = new Texture(Gdx.files.internal("images/shop-description.png"));
        leftTexture = new Texture(Gdx.files.internal("images/left_arrow.png"));
        rightTexture = new Texture(Gdx.files.internal("images/right_arrow.png"));
        goldenCategoryTexture = new Texture(Gdx.files.internal("images/shop-buy-button.png"));
        goldenDrawable = new TextureRegionDrawable(goldenCategoryTexture);
        brownDrawable = new TextureRegionDrawable(brownCategoryTexture);
        left = new TextureRegionDrawable(leftTexture);
        right = new TextureRegionDrawable(rightTexture);

        // create left button
        leftButton = new Button(left);
        leftButton.setTransform(true);

        // create right button
        rightButton = new Button(right);
        rightButton.setTransform(true);

        // create price sticker
        priceDisplay = ShopUtils.createImageTextButton(
                Integer.toString(current.t.getPrice()), skin.getColor("black"),
                "button", 1f,
                goldenDrawable, goldenDrawable,
                skin,
                true);

        // create description sticker
        descriptionDisplay = ShopUtils.createImageTextButton(
                current.t.getName() + "\n" + current.t.getDescription(),
                skin.getColor("black"),
                "button", 1f,
                brownDrawable, brownDrawable, skin,
                true);

        // create buy button
        buyButton = ShopUtils.createImageTextButton("BUY", skin.getColor("black"), "button", 1f,
                brownDrawable, goldenDrawable,
                skin,
                false);

        // create the back button
        backTexture = new Texture(Gdx.files.internal("images/backButton.png"));
        upBack = new TextureRegionDrawable(backTexture);
        backButton = new ImageButton(upBack, upBack);

        // Add listeners to relevant buttons
        rightButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Right button clicked");
                        Node<ShopBuilding> temp = current;
                        current = stock.head.next;
                        stock.head = stock.head.next;
                        stock.tail = temp;

                        priceDisplay.setText(Integer.toString(current.t.getPrice()));
                        descriptionDisplay
                                .setText(current.t.getName() + "\n" + current.t.getDescription());
                        currentItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(current.t.getCategoryTexture()))));
                    }
                });

        leftButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Left button clicked");
                        Node<ShopBuilding> temp = current;
                        current = stock.head.prev;
                        stock.head = stock.head.prev;
                        stock.tail = temp.prev;

                        priceDisplay.setText(Integer.toString(current.t.getPrice()));
                        descriptionDisplay
                                .setText(current.t.getName() + "\n" + current.t.getDescription());
                        currentItem.setDrawable(new TextureRegionDrawable(
                                new Texture(Gdx.files.internal(current.t.getCategoryTexture()))));
                    }
                });

        buyButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.info("Buy button clicked");

                        if (entity.getComponent(InventoryComponent.class).hasGold(current.t.getPrice())) {
                            logger.info("Sufficient stone");
                            entity.getComponent(InventoryComponent.class).addStone(-1 * current.t.getPrice());
                            Sound rockSound = Gdx.audio.newSound(Gdx.files.internal("sounds/rock.mp3"));
                            rockSound.play();
                        } else {
                            logger.info("Insufficient stone!");
                        }
                        entity.getComponent(CommonShopComponents.class).getStoneButton().setText(
                                Integer.toString(entity.getComponent(InventoryComponent.class).getStone()) + "    ");
                    }
                });

        backButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Back button clicked");
                        entity.getEvents().trigger("mainShop");
                    }
                });

        subtitle = new Label("BUILDINGS", skin, "title");
        subtitle.setFontScale(1f);
        subtitle.setColor(skin.getColor("black"));

        // Add items to the stage
        table3.add(leftButton).width(100).height(100);
        table2.add(currentItem).width(450).height(450);
        table4.add(rightButton).width(100).height(100);
        table5.add(priceDisplay).width(300).height(300);
        table1.add(descriptionDisplay).width(450).height(450);
        table6.add(buyButton).width(300).height(300);
        table7.add(backButton).width(50).height(50);
        table8.add(subtitle);
        stage.addActor(table1);
        stage.addActor(table2);
        stage.addActor(table3);
        stage.addActor(table4);
        stage.addActor(table5);
        stage.addActor(table6);
        stage.addActor(table7);
        stage.addActor(table8);

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
        table2.clear();
        table3.clear();
        table4.clear();
        table5.clear();
        table6.clear();
        table7.clear();
        table8.clear();
        stage.clear();
        super.dispose();
    }
}
