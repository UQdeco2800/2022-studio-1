package com.deco2800.game.components.settingsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.AtlantisSinks;
import com.deco2800.game.AtlantisSinks.ScreenType;
import com.deco2800.game.components.shop.ShopUtils;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.files.UserSettings.DisplaySettings;
import com.deco2800.game.memento.CareTaker;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.StringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * DECO2800Game/settings.json under your home directory (This is C:/users/[username] on Windows).
 */
public class SettingsMenuDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(SettingsMenuDisplay.class);
  private final AtlantisSinks game;

  private Table rootTable;
  private TextField fpsText;
  private CheckBox fullScreenCheck;
  private CheckBox vsyncCheck;
  private Slider uiScaleSlider;
  private SelectBox<StringDecorator<DisplayMode>> displayModeSelect;
  private ScreenType backScreen;
  private Boolean musicStatus = true;
  private Boolean soundEffectStatus = true;


  public SettingsMenuDisplay(AtlantisSinks game, ScreenType prevScreen) {
    super();
    this.game = game;
    backScreen = prevScreen;
  }

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    Label title = new Label("Settings", skin,"title");
    title.setFontScale(2f);

    Table settingsTable = makeSettingsTable();
    Table menuBtns = makeMenuBtns();


    rootTable = new Table();
    rootTable.setFillParent(true);

    rootTable.add(title).expandX().align(1);

    rootTable.row();
    rootTable.add(settingsTable).expandX().expandY().center();

    rootTable.row();
    rootTable.add(menuBtns).fillX();



    // Background Colour
    Texture colour = new Texture(Gdx.files.internal("images/uiElements/startscreen/settingsbackgroundsprint2.png"));
    Drawable backgroundColour = new TextureRegionDrawable(colour);
    rootTable.setBackground(backgroundColour);

    stage.addActor(rootTable);
  }

  private Table makeSettingsTable() {
    // Get current values
    UserSettings.Settings settings = UserSettings.get();

    // Create components
    Label fpsLabel = new Label("FPS Cap:", skin);
    fpsText = new TextField(Integer.toString(settings.fps), skin);

    Label fullScreenLabel = new Label("Fullscreen:", skin);
    fullScreenCheck = new CheckBox("", skin);
    fullScreenCheck.setChecked(settings.fullscreen);

    Label vsyncLabel = new Label("VSync:", skin);
    vsyncCheck = new CheckBox("", skin);
    vsyncCheck.setChecked(settings.vsync);

    Label uiScaleLabel = new Label("ui Scale (Unused):", skin);
    uiScaleSlider = new Slider(0.2f, 2f, 0.1f, false, skin);
    uiScaleSlider.setValue(settings.uiScale);
    Label uiScaleValue = new Label(String.format("%.2fx", settings.uiScale), skin);

    Label displayModeLabel = new Label("Resolution:", skin);
    displayModeSelect = new SelectBox<>(skin);
    Monitor selectedMonitor = Gdx.graphics.getMonitor();
    displayModeSelect.setItems(getDisplayModes(selectedMonitor));
    displayModeSelect.setSelected(getActiveMode(displayModeSelect.getItems()));


    // Position Components on table
    Table table = new Table();

    table.add(fpsLabel).right().padRight(15f);
    table.add(fpsText).width(100).left();

    table.row().padTop(10f);
    table.add(fullScreenLabel).right().padRight(15f);
    table.add(fullScreenCheck).left();

    table.row().padTop(10f);
    table.add(displayModeLabel).right().padRight(15f);
    table.add(displayModeSelect).left();

    table.row().padTop(15f);

    // Events on inputs
    uiScaleSlider.addListener(
        (Event event) -> {
          float value = uiScaleSlider.getValue();
          uiScaleValue.setText(String.format("%.2fx", value));
          return true;
        });

    return table;
  }

  private StringDecorator<DisplayMode> getActiveMode(Array<StringDecorator<DisplayMode>> modes) {
    DisplayMode active = Gdx.graphics.getDisplayMode();

    for (StringDecorator<DisplayMode> stringMode : modes) {
      DisplayMode mode = stringMode.object;
      if (active.width == mode.width
          && active.height == mode.height
          && active.refreshRate == mode.refreshRate) {
        return stringMode;
      }
    }
    return null;
  }

  private Array<StringDecorator<DisplayMode>> getDisplayModes(Monitor monitor) {
    DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(monitor);
    Array<StringDecorator<DisplayMode>> arr = new Array<>();

    for (DisplayMode displayMode : displayModes) {
      arr.add(new StringDecorator<>(displayMode, this::prettyPrint));
    }

    return arr;
  }

  private String prettyPrint(DisplayMode displayMode) {
    return displayMode.width + "x" + displayMode.height + ", " + displayMode.refreshRate + "hz";
  }

  private Table makeMenuBtns() {
    //TextButton exitBtn = new TextButton("Exit", skin);
    Texture backTexture = new Texture(Gdx.files.internal("images/backButton.png"));
    Texture backTextureHover = new Texture(Gdx.files.internal("images/backButton_hover.png"));
    TextureRegionDrawable back = new TextureRegionDrawable(backTexture);
    TextureRegionDrawable backHover = new TextureRegionDrawable(backTextureHover);
    ImageButton backButton = new ImageButton(back,back,backHover);

    Texture applyTexture = new Texture(Gdx.files.internal("images/applyButton.png"));
    Texture applyHoeverTexture = new Texture(Gdx.files.internal("images/applyButtonCheckOut.png"));
    TextureRegionDrawable applyBack = new TextureRegionDrawable(applyTexture);
    TextureRegionDrawable applyHover = new TextureRegionDrawable(applyHoeverTexture);
    ImageButton applyButton= new ImageButton(applyBack,applyBack,applyHover);


    backButton.addListener(
            new ClickListener() {
              @Override
              public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                logger.debug("Exit button clicked");
                exitMenu();
                return true;
              }
        });
    backButton.addListener(
            new InputListener() {
              @Override
              public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                backButton.setChecked(true);
              }

              @Override
              public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                backButton.setChecked(false);
              }
            });

    applyButton.addListener(
            new ClickListener() {
              @Override
              public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            logger.debug("Apply button clicked");
            applyChanges();
            return true;
          }
        });

    applyButton.addListener(
            new InputListener() {
              @Override
              public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                applyButton.setChecked(true);
              }

              @Override
              public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                applyButton.setChecked(false);
              }
            });

    Table table = new Table();
    table.add(backButton).expandX().left().padLeft(10f).size(80f);
    table.add(applyButton).expandX().right().size(300f);
    return table;
  }

  private void applyChanges() {
    UserSettings.Settings settings = UserSettings.get();

    Integer fpsVal = parseOrNull(fpsText.getText());
    if (fpsVal != null) {
      settings.fps = fpsVal;
    }
    settings.fullscreen = fullScreenCheck.isChecked();
    settings.uiScale = uiScaleSlider.getValue();
    settings.displayMode = new DisplaySettings(displayModeSelect.getSelected().object);
    settings.vsync = vsyncCheck.isChecked();

    UserSettings.set(settings, true);
  }

  private void exitMenu() {
    if (backScreen == ScreenType.MAIN_MENU) {
      CareTaker.deleteAll();
      game.setScreen(ScreenType.MAIN_MENU);
      logger.getName();
    } else if (backScreen == ScreenType.MAIN_GAME) {
      game.setScreen(ScreenType.MAIN_GAME);
      logger.getName();
    }
  }

  private Integer parseOrNull(String num) {
    try {
      return Integer.parseInt(num, 10);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Override
  protected void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  @Override
  public void update() {
    //stage.act(ServiceLocator.getTimeSource().getDeltaTime());
  }

  @Override
  public void dispose() {
    rootTable.clear();
    super.dispose();
  }
}
