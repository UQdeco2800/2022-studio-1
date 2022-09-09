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

    public void create(){
        super.create();
        addClock();
    }
    private void addClock(){
        rightTable = new Table();
        rightTable.top().right();
        rightTable.setFillParent(true);
        rightTable.padTop(40f).padRight(-40f);


        //Bring in the clock
        clockImage = new Image(ServiceLocator.getResourceService().getAsset("images/clock.png",Texture.class));
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
}
