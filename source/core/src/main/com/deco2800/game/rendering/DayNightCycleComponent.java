package com.deco2800.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for shading the game screen when invoked in RendererService
 */
public class DayNightCycleComponent {

    Logger logger = LoggerFactory.getLogger(DayNightCycleComponent.class);
    private final ShaderProgram dayNightCycleShader;
    public static final TextureRegion BLACK_IMAGE = new TextureRegion();

    public static final float NIGHT_INTENSITY = 0.95f;
    public static final float DUSK_INTENSITY = 0.06f;
    public static final float DAWN_INTENSITY = 0.095f;
    public static final float DAY_INTENSITY = 0.18f;

    public static final  Vector3 bright = new Vector3(6.3f, 6.3f, 6.7f);

    public static final Vector3 dark = new Vector3(0.3f, 0.3f, 0.7f);

    private Vector3 ambientColour;

    private float intensity;

    /**
     * Creates a new DayNightCycleComponent. Making sure to compile shader files
     */
    public DayNightCycleComponent() {

        Pixmap map = new Pixmap(128, 128, Pixmap.Format.RGBA8888);;
        map.setColor(Color.BLACK);
        map.fillRectangle(0, 0, map.getWidth(), map.getHeight());
        BLACK_IMAGE.setTexture(new Texture(map));

        this.ambientColour = bright;
        this.intensity = DAY_INTENSITY;

        ShaderProgram.pedantic = false;
        var vertexShader = Gdx.files.internal("shaders/base.vert");
        var lightShader=  Gdx.files.internal("shaders/light.frag");
        this.dayNightCycleShader = new ShaderProgram(vertexShader, lightShader);

        /* Subscribe to part of day changes */
        DayNightCycleService dayNightCycleService = ServiceLocator.getDayNightCycleService();
        if (dayNightCycleService != null) {
            dayNightCycleService.getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
                    this::onPartOfDayChange);
        }
    }

    /**
     *  Called to apply the shader
     *  Should be frequently called as ambientColor and intensity might changed.
     *
     * @param batch the sprite batch to apply the filter to
     */
    public void render(SpriteBatch batch) {
        dayNightCycleShader.bind();
        {
            dayNightCycleShader.setUniformi("u_lightmap", 1);
            dayNightCycleShader.setUniformf("ambientColor", ambientColour.x, ambientColour.y, ambientColour.z, intensity);
            dayNightCycleShader.setUniformf("resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.setShader(dayNightCycleShader);
            Texture tex = BLACK_IMAGE.getTexture();
            tex.bind(0);
        }
    }

    /**
     * Invoked when the part of day has changed
     *
     * @param partOfDay the new part of day
     */
    public void onPartOfDayChange(DayNightCycleStatus partOfDay) {
        this.intensity = switch (partOfDay) {
            case DAWN -> DAWN_INTENSITY;
            case DAY -> DAY_INTENSITY;
            case DUSK -> DUSK_INTENSITY;
            case NIGHT -> NIGHT_INTENSITY;
            default -> 0.5f;
        };

        this.ambientColour = switch (partOfDay) {
            case DAWN, DAY, DUSK -> bright;
            case NIGHT -> dark;
            default -> bright;
        };
    }

    public ShaderProgram getDayNightCycleShader() {
        return dayNightCycleShader;
    }

    public Vector3 getAmbientColour() {
        return ambientColour;
    }

    public float getIntensity() {
        return intensity;
    }
}
