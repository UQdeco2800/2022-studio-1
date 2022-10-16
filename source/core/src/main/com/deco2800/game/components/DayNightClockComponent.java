package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

/**
 * Defines a Clock UI element that displays the current phase of the day/night cycle
 */
public class DayNightClockComponent extends UIComponent {
    private Image clockImage;
    private Table rightTable;
    private long timeLoaded;
    private boolean loaded;

    private final String[] clockSprites = {
            "images/clock_sprites/clock_day1_1.png",
            "images/clock_sprites/clock_day1_2.png",
            "images/clock_sprites/clock_day1_3.png",
            "images/clock_sprites/clock_day1_4.png",
            "images/clock_sprites/clock_day1_5.png",
            "images/clock_sprites/clock_day1_6.png",
            "images/clock_sprites/clock_day1_7.png",
            "images/clock_sprites/clock_day1_8.png",
            "images/clock_sprites/clock_day2_1.png",
            "images/clock_sprites/clock_day2_2.png",
            "images/clock_sprites/clock_day2_3.png",
            "images/clock_sprites/clock_day2_4.png",
            "images/clock_sprites/clock_day2_5.png",
            "images/clock_sprites/clock_day2_6.png",
            "images/clock_sprites/clock_day2_7.png",
            "images/clock_sprites/clock_day2_8.png",
            "images/clock_sprites/clock_day3_1.png",
            "images/clock_sprites/clock_day3_2.png",
            "images/clock_sprites/clock_day3_3.png",
            "images/clock_sprites/clock_day3_4.png",
            "images/clock_sprites/clock_day3_5.png",
            "images/clock_sprites/clock_day3_6.png",
            "images/clock_sprites/clock_day3_7.png",
            "images/clock_sprites/clock_day3_8.png",
            "images/clock_sprites/clock_day4_1.png",
            "images/clock_sprites/clock_day4_2.png",
            "images/clock_sprites/clock_day4_3.png",
            "images/clock_sprites/clock_day4_4.png",
            "images/clock_sprites/clock_day4_5.png",
            "images/clock_sprites/clock_day4_6.png",
            "images/clock_sprites/clock_boss.png"
    };

    private int currentSprite = -1;

    /**
     * Creates the clock UI component and adds the event listener for EVENT_PART_OF_DAY_PASSED from DayNightCycleService
     */
    public void create(){
        super.create();
        addClock();

        // Event listener
        ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_INTERMITTENT_PART_OF_DAY_CLOCK,
                this::changeSprite);

        ServiceLocator.getDayNightCycleService().getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
                this::changeSprite);
    }

    /**
     * Adds the clock to the top right hand corner of the game screen and adds it to the stage
     */
    private void addClock(){
        rightTable = new Table();
        rightTable.top().right();
        rightTable.setFillParent(true);
        rightTable.padTop(60f).padRight(10f);

        //adding it on screen
        rightTable.add(clockImage).left().bottom().size(200f, 200f);

        stage.addActor(rightTable);
    }

    /**
     * Clears the current clock face
     */
    public void dispose() {
        rightTable.clear();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        //draw is handled by stage
    }

    /**
     * Called by the event listener on trigger. Updates the current clock face for the next one in the sequence
     *
     * @param partOfDay DayNightCycleStatus
     */
    private void changeSprite(DayNightCycleStatus partOfDay) {
        if (loaded) {
            if (ServiceLocator.getDayNightCycleService().getTimer().getTime() - timeLoaded < 300) {
                //avoid asynchronous update triggers right after being loaded in
                return;
            } else {
                loaded = false;
            }
        }
        if (this.currentSprite != clockSprites.length - 1) {
            this.currentSprite += 1;
        }

        this.clockImage = new Image(ServiceLocator.getResourceService().getAsset(clockSprites[currentSprite], Texture.class));

        rightTable.clear();
        rightTable.add(clockImage).left().bottom().size(200f, 200f);
    }

    public void loadFromSave() {
        DayNightCycleService cycleService = ServiceLocator.getDayNightCycleService();
        int dayNum = cycleService.getCurrentDayNumber();
        //8 segments in the clock, starts at dawn (between night & dawn)
        int numUpdates = (dayNum - 1) * 8;
        DayNightCycleStatus status = cycleService.getCurrentCycleStatus();
        switch (status) {
            case DUSK:
                numUpdates += 5;
                break;
            case NIGHT:
                numUpdates += 6 + (cycleService.partOfDayHalveIteration - 1);
                break;
            case DAY:
                numUpdates += cycleService.partOfDayHalveIteration;
                break;
            default:
                break;
        }
        if (numUpdates >= clockSprites.length) {
            numUpdates = clockSprites.length;
        }
        this.currentSprite = numUpdates - 1;
        changeSprite(status);
        loaded = true;
        timeLoaded = cycleService.getTimer().getTime();
    }
}
