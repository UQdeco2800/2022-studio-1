package com.deco2800.game.components.Guidebook;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.screens.GuidebookStatus;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.configs.Page;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.TextUtil;
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

    public static GuidebookStatus bookStatus = GuidebookStatus.CLOSED;

    private Table content;
    private Table book;

    private Table controls;

    private static final Page[] pages = parseGuidebookContentJson("configs/guidebookcontent.json");
    public static final int MAX_PAGES;

    static {
        assert pages != null;
        MAX_PAGES = pages.length;
    }

    public static int currentPage = 0;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        // Handled outside
    }

    public Table[] getGuidebook() {
        return new Table[] { book, content, controls };
    }

    /**
     * Displays the guidebook according to the state defined in
     * GuidebookDisplay.bookStatus
     */
    public Table[] displayBook() {
        switch (bookStatus) {
            case CLOSED, OPENING, FLICK_NEXT, FLICK_PREVIOUS -> {
                return displayBookTransition();
            }
            case OPEN -> {
                return displayOpenBook();
            }
            default -> {}
        }
        return new Table[] { book, content, controls };
    }

    /**
     * Creates and return tables for a Guidebook for any given transitional state
     */
    private Table[] displayBookTransition() {
        book = new Table();
        book.setFillParent(true);
        book.center();

        float screenHeight = Gdx.graphics.getHeight();

        String imagePath = "";

        float bookHeight = 0;
        float bookWidth = 0;

        switch (bookStatus) {
            case CLOSED -> {
                imagePath = "images/uiElements/exports/guidebook-cover.png";
                bookHeight = screenHeight * 0.8f;
                bookWidth = bookHeight * 0.809f;
            }
            case OPENING -> {
                imagePath = "images/uiElements/exports/guidebook-opening.png";
                bookHeight = screenHeight * 0.8f;
                bookWidth = bookHeight * 0.809f;
            }
            case FLICK_NEXT -> {
                imagePath = "images/uiElements/exports/guidebook-next.png";
                bookHeight = screenHeight * 0.85f;
                bookWidth = bookHeight * 1.52f;
            }
            case FLICK_PREVIOUS -> {
                imagePath = "images/uiElements/exports/guidebook-previous.png";
                bookHeight = screenHeight * 0.85f;
                bookWidth = bookHeight * 1.52f;
            }
            default -> {}
        }

        Image bookImage = new Image(ServiceLocator.getResourceService().getAsset(imagePath, Texture.class));

        book.add(bookImage).center().size(bookWidth, bookHeight);

        stage.addActor(book);

        return new Table[] { book, content, controls };
    }

    /**
     * Creates and return tables for a Guidebook in its open position
     */
    private Table[] displayOpenBook() {
        book = new Table();
        book.setFillParent(true);
        book.center();

        float screenHeight = Gdx.graphics.getHeight();
        float screenWidth = Gdx.graphics.getWidth();

        float bookHeight = screenHeight * 0.8f;
        float bookWidth = bookHeight * 1.618f;

        float pageHeight = 0.8f * bookHeight;
        float maxImageHeight = 0.3f * pageHeight;

        Image bookImage = new Image(
                ServiceLocator.getResourceService().getAsset("images/guidebook-open.png", Texture.class));
        Image leftHeaderFrame = new Image(ServiceLocator.getResourceService()
                .getAsset("images/uiElements/exports/guidebook-heading-frame.png", Texture.class));
        Image rightHeaderFrame = new Image(ServiceLocator.getResourceService()
                .getAsset("images/uiElements/exports/guidebook-heading-frame.png", Texture.class));

        Texture backPageTexture = new Texture(Gdx.files.internal("images/back_page.png"));
        TextureRegionDrawable upBack = new TextureRegionDrawable(backPageTexture);
        TextureRegionDrawable downBack = new TextureRegionDrawable(backPageTexture);
        ImageButton backPageButton = new ImageButton(upBack, downBack);

        Texture nextPageTexture = new Texture(Gdx.files.internal("images/next_page.png"));
        TextureRegionDrawable upNext = new TextureRegionDrawable(nextPageTexture);
        TextureRegionDrawable downNext = new TextureRegionDrawable(nextPageTexture);
        ImageButton nextPageButton = new ImageButton(upNext, downNext);

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
        int charactersPerLine = (int) (0.5 * 0.7 * bookWidth / pixelsPerCharacter);

        Table leftPage = new Table();
        Table rightPage = new Table();

        String leftPageHeader = pages[currentPage].header;
        String rightPageHeader = pages[currentPage + 1].header;

        String leftPageContent = pages[currentPage].content;
        String rightPageContent = pages[currentPage + 1].content;

        String leftPageImagePath = pages[currentPage].image;
        String rightPageImagePath = pages[currentPage + 1].image;

        Image leftPageImage = null;
        Image rightPageImage = null;

        if (!leftPageImagePath.equals("")) {
            leftPageImage = new Image(ServiceLocator.getResourceService().getAsset(leftPageImagePath, Texture.class));
        }
        if (!rightPageImagePath.equals("")) {
            rightPageImage = new Image(ServiceLocator.getResourceService().getAsset(rightPageImagePath, Texture.class));
        }

        leftPageContent = TextUtil.lineFormat(leftPageContent, charactersPerLine);
        rightPageContent = TextUtil.lineFormat(rightPageContent, charactersPerLine);

        Label leftHeader = TextUtil.createTextLabel(leftPageHeader, 0xe6bd39ff, 32);
        Label rightHeader = TextUtil.createTextLabel(rightPageHeader, 0xe6bd39ff, 32);

        Label leftContent = TextUtil.createTextLabel(leftPageContent, 0x000000ff, 16);
        Label rightContent = TextUtil.createTextLabel(rightPageContent, 0x000000ff, 16);

        leftHeader.setAlignment(Align.center);
        rightHeader.setAlignment(Align.center);

        leftContent.setAlignment(Align.topLeft);
        rightContent.setAlignment(Align.topLeft);

        leftPage.setSize(0.5f * bookWidth, bookHeight);
        if (!leftPageHeader.equals("")) {
            leftPage.stack(leftHeaderFrame, leftHeader).expandX().fillX().padBottom(0.05f * bookHeight);
            leftPage.row();
        }
        if (leftPageImage != null && pages[currentPage].imagePosition.equals("UP")) {
            leftPage.add(leftPageImage)
                    .size(leftPageImage.getWidth() * maxImageHeight / leftPageImage.getHeight(), maxImageHeight)
                    .padBottom(0.08f * bookHeight);
            leftPage.row();
        }
        leftPage.add(leftContent).fillX().expandX().expandY().fillY().width(0.7f * 0.5f * bookWidth);
        if (leftPageImage != null && pages[currentPage].imagePosition.equals("DOWN")) {
            leftPage.row();
            leftPage.add(leftPageImage)
                    .size(leftPageImage.getWidth() * maxImageHeight / leftPageImage.getHeight(), maxImageHeight)
                    .padBottom(0.08f * bookHeight);
        }

        rightPage.setSize(0.5f * bookWidth, bookHeight);
        if (!rightPageHeader.equals("")) {
            rightPage.stack(rightHeaderFrame, rightHeader).expandX().fillX().padBottom(0.05f * bookHeight);
            rightPage.row();
        }
        if (rightPageImage != null && pages[currentPage + 1].imagePosition.equals("UP")) {
            rightPage.add(rightPageImage)
                    .size(rightPageImage.getWidth() * maxImageHeight / rightPageImage.getHeight(), maxImageHeight)
                    .padBottom(0.08f * bookHeight);
            rightPage.row();
        }
        rightPage.add(rightContent).fillX().expandX().expandY().fillY().width(0.7f * 0.5f * bookWidth);
        if (rightPageImage != null && pages[currentPage + 1].imagePosition.equals("DOWN")) {
            rightPage.row();
            rightPage.add(rightPageImage)
                    .size(rightPageImage.getWidth() * maxImageHeight / rightPageImage.getHeight(), maxImageHeight)
                    .padBottom(0.08f * bookHeight);
        }

        content = new Table();
        // content.debug();
        content.setSize(bookWidth, pageHeight);
        content.setPosition((screenWidth - bookWidth) / 2f, (screenHeight - pageHeight) / 2f);

        controls = new Table();
        controls.setSize(bookWidth, pageHeight);
        controls.setPosition((screenWidth - bookWidth) / 2f, (screenHeight - pageHeight) / 2f);

        content.row().fillX().expandX().fillY().expandY().pad(0.07f * bookWidth).padTop(0f).padBottom(0f);
        content.add(leftPage);
        content.add(rightPage);

        book.add(bookImage).expandX().expandY().center().size(bookWidth, bookHeight);
        controls.add(backPageButton).expandX().expandY().bottom().left().size(0.04f * bookWidth)
                .padLeft(0.07f * bookWidth);
        controls.add(nextPageButton).expandX().expandY().bottom().right().size(0.04f * bookWidth)
                .padRight(0.07f * bookWidth);

        stage.addActor(book);
        stage.addActor(content);
        stage.addActor(controls);

        return new Table[] { book, content, controls };
    }

    /**
     * Formats a String based on the maximum number of character required per line
     * and places a newline character
     * (i.e., \n) in between line blocks of total character counter less than or
     * equal to the provided number per line
     *
     * @param labelContent:  the string to format
     * @param numberPerLine: the maximum character count per line
     *
     * @return a formatted string as per the description above.
     */
    public String format(String labelContent, int numberPerLine) {
        StringBuilder formattedString = new StringBuilder();
        int count = 0;
        for (String word : labelContent.split(" ")) {
            if (count + word.length() >= numberPerLine) {
                if (!word.contains("\n"))
                    formattedString.append('\n');
                count = 0;
            } else if (word.contains("\n")) {
                count = 0;
            }
            formattedString.append(word).append(" ");
            count += word.length() + 1;
        }
        return formattedString.toString();
    }

    /**
     * Parses the JSON file to produce an array of Pages which can be used to
     * retrieve content from
     *
     * @param path: the filepath where the JSON file is located
     *
     * @return an raay of Pages with parsed information as per the Page class
     */
    private static Page[] parseGuidebookContentJson(String path) {
        Gson gson = new Gson();
        BufferedReader buffer;
        try {
            buffer = Files.newBufferedReader(Paths.get(path));
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

    public static Label textLabel(String text, int textColour, float fontSize) {
        Color color = new Color(textColour);

        Label.LabelStyle fontStyle = new Label.LabelStyle();
        fontStyle.font = new BitmapFont(Gdx.files.internal("flat-earth/skin/fonts/pixel_32.fnt"));
        fontStyle.fontColor = color;

        Label formattedText = new Label(text, fontStyle);
        formattedText.setFontScale(fontSize / 32);

        return formattedText;
    }
}
