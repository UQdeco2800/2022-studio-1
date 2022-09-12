package com.deco2800.game.components.tasks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

public class DayNightClockComponent extends UIComponent {
    private Image clockImage;
    private Table rightTable;

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
            "images/clock_sprites/clock_day4_7.png",
            "images/clock_sprites/clock_day4_8.png"
    };

    private int currentSprite = 0;

    public void create(){
        super.create();
        addClock();

        // Event listener
    }

    private void addClock(){
        rightTable = new Table();
        rightTable.top().right();
        rightTable.setFillParent(true);
        rightTable.padTop(60f).padRight(10f);

        //Bring in the clock
        clockImage = new Image(ServiceLocator.getResourceService().getAsset(clockSprites[currentSprite], Texture.class));
        //adding it on screen
       // rightTable.add(clockImage).pad(5);
        rightTable.add(clockImage).left().bottom().size(200f, 200f);

        stage.addActor(rightTable);
    }
    public void dispose() {
        rightTable.clear();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        //draw is handled by stage
    }

    private void changeSprite() {
        this.currentSprite += 1;
        this.clockImage = new Image(ServiceLocator.getResourceService().getAsset(clockSprites[currentSprite], Texture.class));
    }
}
