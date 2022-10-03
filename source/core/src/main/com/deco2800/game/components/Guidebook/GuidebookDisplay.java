package com.deco2800.game.components.Guidebook;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.configs.Page;
import com.deco2800.game.ui.UIComponent;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;


public class GuidebookDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(GuidebookDisplay.class);
    private static final float Z_INDEX = 2f;

    public static boolean isClosed = true;

    private Table content;
    private Table book;

    private Table controls;

    private static final Page[] pages = parseGuidebookContentJson();
    public static final int maxPages = pages.length;

    public static int currentPage = 0;

    @Override
    public void create() {
        super.create();
        addActors();
    }


    private void addActors() {
//        bookContainer = new Table();
//        bookContainer.setFillParent(true);
//        bookContainer.center();
//
//        Table book = new Table();
//        book.setFillParent(true);
//        book.center();
//
//        float screenHeight = Gdx.graphics.getHeight();
//        float screenWidth = Gdx.graphics.getWidth();
//
//        float bookHeight = screenHeight * 0.8f;
//        float bookWidth = bookHeight * 1.618f;
//
//        float pageHeight = 0.8f * bookHeight;
//
//        //Background Colour
////      Texture colour = new Texture(Gdx.files.internal("images/guidebook-open.png"));
////      Drawable backgroundColour = new TextureRegionDrawable(colour);
//
//        Image bookImage = new Image(ServiceLocator.getResourceService().getAsset("images/guidebook-open.png", Texture.class));
//
//        Texture titleTexture = new Texture(Gdx.files.internal("images/uiElements/exports/guidebook-heading-frame.png"));
//        TextureRegionDrawable titleUp = new TextureRegionDrawable(titleTexture);
//        TextureRegionDrawable titleDown = new TextureRegionDrawable(titleTexture);
//        TextButton title = ShopUtils.createImageTextButton(
//                "Enter Page Title",
//                skin.getColor("black"),
//                "button", 1f, titleUp, titleDown, skin, true);
//
//        String labelContent = "This is just random words to fill the page\n, hopefully in the future it will be content for the game :) This is just random words to fill the page, hopefully in the future it will be content for the game :) This is just random words to fill the page, hopefully in the future it will be content for the game :)This is just random words to fill the page, hopefully in the future it will be content for the game :)";
//        int pixelsPerCharacter = 10;
//        int charactersPerLine = (int) (0.7 * 0.5 * bookWidth / pixelsPerCharacter);
//        labelContent = format(labelContent, charactersPerLine);
//        Label leftContent = new Label(labelContent, skin, "small");
//        Label rightContent = new Label(labelContent, skin, "small");
//
//        leftContent.setAlignment(Align.topLeft);
//        rightContent.setAlignment(Align.topLeft);
//
//        Table page1 = new Table();
//        page1.debug();
//        page1.setSize(bookWidth, pageHeight);
//        page1.setPosition((screenWidth - bookWidth) / 2f, (screenHeight - pageHeight) / 2f);
//
//        Table leftPage = new Table();
//        leftPage.debug();
//        leftPage.setSize(0.5f * bookWidth, bookHeight);
//        leftPage.add(leftContent).expandX().fillX().expandY().fillY().pad(0.07f * bookWidth).padTop(0f).padBottom(0f);
//
//        Table rightPage = new Table();
//        rightPage.debug();
//        rightPage.setSize(0.5f * bookWidth, bookHeight);
//        rightPage.add(rightContent).expandX().fillX().expandY().fillY().top().pad(0.07f * bookWidth).padTop(0f).padBottom(0f);
//
//        page1.row().fillX().expandX().fillY().expandY();
//        page1.add(leftPage);
//        page1.add(rightPage);
//
//        book.add(bookImage).center().size(bookWidth, bookHeight);
//
//        bookContainer.stack(book, page1.padTop(50f).padBottom(80f));
//        stage.addActor(bookContainer);
    }

    public Table[] getGuidebook() {
        return new Table[]{book, content, controls};
    }

    public Table[] displayBook() {
        book = new Table();
        book.setFillParent(true);
        book.center();

        float screenHeight = Gdx.graphics.getHeight();
        float screenWidth = Gdx.graphics.getWidth();

        float bookHeight = screenHeight * 0.8f;
        float bookWidth = bookHeight * 1.618f;

        float pageHeight = 0.8f * bookHeight;

        Image bookImage = new Image(ServiceLocator.getResourceService().getAsset("images/guidebook-open.png", Texture.class));

        Texture backPageTexture = new Texture(Gdx.files.internal("images/back_page.png"));
        TextureRegionDrawable upBack = new TextureRegionDrawable(backPageTexture);
        TextureRegionDrawable downBack = new TextureRegionDrawable(backPageTexture);
        ImageButton backPageButton = new ImageButton(upBack, downBack);

        Texture nextPageTexture = new Texture(Gdx.files.internal("images/next_page.png"));
        TextureRegionDrawable upNext = new TextureRegionDrawable(nextPageTexture);
        TextureRegionDrawable downNext = new TextureRegionDrawable(nextPageTexture);
        ImageButton nextPageButton = new ImageButton(upNext, downNext);

        String leftPageContent = pages[currentPage].content;
        String rightPageContent = pages[currentPage + 1].content;

        nextPageButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("next page clicked");
                        entity.getEvents().trigger("nextPage");
                    }
                });

        backPageButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("back page clicked");
                        entity.getEvents().trigger("backPage");
                    }
                });

        int pixelsPerCharacter = 10;
        int charactersPerLine = (int) (0.7 * 0.5 * bookWidth / pixelsPerCharacter);
        leftPageContent = format(leftPageContent, charactersPerLine);
        rightPageContent = format(rightPageContent, charactersPerLine);
        Label leftContent = new Label(leftPageContent, skin, "small");
        Label rightContent = new Label(rightPageContent, skin, "small");

        leftContent.setAlignment(Align.topLeft);
        rightContent.setAlignment(Align.topLeft);

        content = new Table();
        content.debug();
        content.setSize(bookWidth, pageHeight);
        content.setPosition((screenWidth - bookWidth) / 2f, (screenHeight - pageHeight) / 2f);

        controls = new Table();
        controls.setSize(bookWidth, pageHeight);
        controls.setPosition((screenWidth - bookWidth) / 2f, (screenHeight - pageHeight) / 2f);

        Table leftPage = new Table();
        leftPage.debug();
        leftPage.setSize(0.5f * bookWidth, bookHeight);
        leftPage.add(leftContent).expandX().fillX().expandY().fillY();

        Table rightPage = new Table();
        rightPage.debug();
        rightPage.setSize(0.5f * bookWidth, bookHeight);
        rightPage.add(rightContent).expandX().fillX().expandY().fillY();

        content.row().fillX().expandX().fillY().expandY().pad(0.07f * bookWidth).padTop(0f).padBottom(0f);
        content.add(leftPage).align(Align.left);
        content.add(rightPage).align(Align.right);

        book.add(bookImage).expandX().expandY().center().size(bookWidth, bookHeight);
        controls.add(backPageButton).expandX().expandY().bottom().left().size(0.05f * bookWidth).padLeft(0.07f * bookWidth);
        controls.add(nextPageButton).expandX().expandY().bottom().right().size(0.05f * bookWidth).padRight(0.07f * bookWidth);

        stage.addActor(book);
        stage.addActor(content);
        stage.addActor(controls);

        return new Table[]{book, content, controls};
    }

//    public Table openBook() {
//        bookContainer = new Table();
//        bookContainer.setFillParent(true);
//        bookContainer.center();
//
//        Table book = new Table();
//        book.setFillParent(true);
//        book.center();
//
//        float screenHeight = Gdx.graphics.getHeight();
//
//        float bookHeight = screenHeight * 0.8f;
//        float bookWidth = bookHeight * 0.809f;
//
//        Image bookImage = new Image(ServiceLocator.getResourceService().getAsset("images/uiElements/exports/guidebook-cover.png", Texture.class));
//
//        book.add(bookImage).center().size(bookWidth, bookHeight);
//        bookContainer.add(book).center();
//
//        stage.addActor(bookContainer);
//
//        return bookContainer;
//    }

    public String format(String labelContent, int numberPerLine) {
        StringBuilder formattedString = new StringBuilder();
        int count = 0;
        for (String word: labelContent.split(" ")) {
            if (count + word.length() >= numberPerLine) {
                if (!word.contains("\n")) formattedString.append('\n');
                count = 0;
            } else if (word.contains("\n")) {
                count = 0;
            }
            formattedString.append(word).append(" ");
            count += word.length() + 1;
        }
        return formattedString.toString();
    }

    private static Page[] parseGuidebookContentJson() {
        Gson gson = new Gson();
        BufferedReader buffer;
        try {
            buffer = Files.newBufferedReader(Paths.get("configs/guidebookcontent.json"));
        } catch (IOException e) {
            System.out.println("File not valid");
            return null;
        }
        return gson.fromJson((Reader) buffer, Page[].class);
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
        stage.clear();
        super.dispose();
    }
}
