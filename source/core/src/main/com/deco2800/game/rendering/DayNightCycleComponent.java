package com.deco2800.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.DayNightCycleService;
import com.deco2800.game.services.DayNightCycleStatus;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.configs.DayNightCycleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for shading the game screen when invoked in RendererService
 */
public class DayNightCycleComponent {

    Logger logger = LoggerFactory.getLogger(DayNightCycleComponent.class);
    private final ShaderProgram dayNightCycleShader;
    public static final TextureRegion BLACK_IMAGE = new TextureRegion();

    public static final float NIGHT_INTENSITY = 0.055f;
    public static final float DUSK_INTENSITY = 0.075f;
    public static final float DAWN_INTENSITY = 0.095f;
    public static final float DAY_INTENSITY = 0.15f;

    private static final float FADE_EVERY = 500f; // 0.5 sec

    private long lastIntensityFade;

    public static final  Vector3 bright = new Vector3(6.3f, 6.3f, 6.7f);

    public static final Vector3 dark = new Vector3(0.3f, 0.3f, 0.7f);

    private Vector3 ambientColour;

    private float intensity;

    private DayNightCycleConfig config;

    private DayNightCycleStatus currentPartOfDay;

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

        this.lastIntensityFade = System.currentTimeMillis();

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
        this.currentPartOfDay = dayNightCycleService.getCurrentCycleStatus();

        this.config =  FileLoader.readClass(DayNightCycleConfig.class, "configs/DayNight.json");
    }

    /**
     *  Called to apply the shader
     *  Should be frequently called as ambientColor and intensity might changed.
     *
     * @param batch the sprite batch to apply the filter to
     */
    public void render(SpriteBatch batch) {
        this.fade();
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
     * Give steps values according to number of seconds in each part of day.
     * Used for gradually fading to the next part of day
     * @param partOfDay the part of day
     * @return the amount to step up by
     */
    public float getCycleIntensityStep(DayNightCycleStatus partOfDay) {
        /* converts length to seconds */
        return switch (partOfDay) {
            case DAWN -> getIntensityDifference(partOfDay)/(config.dawnLength/FADE_EVERY);
            case DAY -> getIntensityDifference(partOfDay)/(config.dayLength/FADE_EVERY);
            case DUSK -> getIntensityDifference(partOfDay)/(config.duskLength/FADE_EVERY);
            case NIGHT -> getIntensityDifference(partOfDay)/(config.nightLength/FADE_EVERY);
            default -> 0.5f;
        };
    }

    /**
     * Return the intensity between the part of day and next
     *
     * @param partOfDay
     * @return
     */
    public float getIntensityDifference(DayNightCycleStatus partOfDay) {
        return switch (partOfDay) {
            case DAWN -> DAY_INTENSITY - DAWN_INTENSITY;
            case DAY -> DAY_INTENSITY - DUSK_INTENSITY;
            case DUSK -> DUSK_INTENSITY - NIGHT_INTENSITY;
            case NIGHT -> DAWN_INTENSITY - NIGHT_INTENSITY;
            default -> 0.0f; // never gonna happen
        };
    }



    /**
     * Invoked when the part of day has changed
     *
     * @param partOfDay the new part of day
     */
    public void onPartOfDayChange(DayNightCycleStatus partOfDay) {
        this.currentPartOfDay = partOfDay;
    }

    /**
     * Gradually fades to the next part of the day
     */
    private void fade() {
        if (currentPartOfDay != null) {
            if (shouldFade()) {
                this.intensity = switch (currentPartOfDay) {
                    case DAWN, NIGHT -> intensity + getCycleIntensityStep(currentPartOfDay);
                    case DAY, DUSK -> intensity - getCycleIntensityStep(currentPartOfDay);
                    default -> intensity;
                };

                this.lastIntensityFade = System.currentTimeMillis();
            }
        }
    }

    /**
     * Signals whether we need to fade to next part of day.
     * It's based on the number of milliseconds set per fade step
     *
     * @return true if fade is needed false otherwise
     */
    private boolean shouldFade() {
        return System.currentTimeMillis() - lastIntensityFade >= FADE_EVERY;
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
