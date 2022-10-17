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

    public static final float NIGHT_INTENSITY = 0.035f;
    public static final float DUSK_INTENSITY = 0.075f;
    public static final float DAWN_INTENSITY = 0.095f;
    public static final float DAY_INTENSITY = 0.15f;

    private static final float FADE_EVERY = 500f; // 0.5 sec

    private static final float FADE_DAWN_EVERY = 500f;

    private static final float FADE_NIGHT_EVERY = 1500f; // 1.5sec

    private long lastIntensityFade;

    public static final Vector3 bright = new Vector3(6.3f, 6.3f, 6.7f);

    public static final Vector3 dark = new Vector3(0.3f, 0.3f, 0.7f);

    private Vector3 ambientColour;

    private float intensity;

    private final DayNightCycleConfig config;

    private DayNightCycleStatus currentPartOfDay;

    /**
     * Creates a new DayNightCycleComponent. Making sure to compile shader files
     */
    public DayNightCycleComponent() {

        Pixmap map = new Pixmap(128, 128, Pixmap.Format.RGBA8888);
        ;
        map.setColor(Color.BLACK);
        map.fillRectangle(0, 0, map.getWidth(), map.getHeight());
        BLACK_IMAGE.setTexture(new Texture(map));

        this.ambientColour = bright;
        this.intensity = DAWN_INTENSITY;

        this.lastIntensityFade = System.currentTimeMillis();

        ShaderProgram.pedantic = false;
        var vertexShader = Gdx.files.internal("shaders/base.vert");
        var lightShader = Gdx.files.internal("shaders/light.frag");
        this.dayNightCycleShader = new ShaderProgram(vertexShader, lightShader);

        /* Subscribe to part of day changes */
        DayNightCycleService dayNightCycleService = ServiceLocator.getDayNightCycleService();
        if (dayNightCycleService != null) {
            dayNightCycleService.getEvents().addListener(DayNightCycleService.EVENT_PART_OF_DAY_PASSED,
                    this::onPartOfDayChange);
            this.currentPartOfDay = dayNightCycleService.getCurrentCycleStatus();
        }


        this.config = FileLoader.readClass(DayNightCycleConfig.class, "configs/DayNight.json");
    }

    /**
     * Called to apply the shader
     * Should be frequently called as ambientColor and intensity might changed.
     *
     * @param batch the sprite batch to apply the filter to
     */
    public void render(SpriteBatch batch) {

        /* Start dayNightCycle on first render, indicates all assets have loaded */
        if (!ServiceLocator.getDayNightCycleService().hasStarted()) {
            ServiceLocator.getDayNightCycleService().start();
        }

        if (!ServiceLocator.getDayNightCycleService().hasEnded()) {
            this.fade();
        }

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
     *
     * @return the amount to step up by
     */
    public float getCurrentCycleIntensityStep() {
        /* converts length to seconds */
        return switch (currentPartOfDay) {
            case DAWN -> getIntensityDifference(currentPartOfDay) / (config.dawnLength / FADE_DAWN_EVERY);
            case DAY -> getIntensityDifference(currentPartOfDay) / (config.dayLength / FADE_EVERY);
            case DUSK -> getIntensityDifference(currentPartOfDay) / (config.duskLength / FADE_EVERY);
            case NIGHT -> getIntensityDifference(currentPartOfDay) / (config.nightLength / FADE_NIGHT_EVERY);
            default -> 0.5f;
        };
    }

    /**
     * Gets the current part of day intensity
     *
     * @return the intensity for current part of day
     */
    public float getCurrentPartOfDayIntensity() {
        return switch (currentPartOfDay) {
            case DAWN -> DAWN_INTENSITY;
            case DAY -> DAY_INTENSITY;
            case DUSK -> DUSK_INTENSITY;
            case NIGHT -> NIGHT_INTENSITY;
            default -> 0.0f; // not going to happen
        };
    }

    /**
     * Computes the intensity between the part of day and next
     *
     * @param partOfDay the part of day to get next part of day difference from
     *
     * @return the intensity difference
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
        this.intensity = getCurrentPartOfDayIntensity();
    }


    public void setPartOfDay(DayNightCycleStatus partOfDay) {
        this.currentPartOfDay = partOfDay;
    }

    /**
     * Gradually fades to the next part of the day.
     * Night overrides its own light fading and replaces light shader with dark one
     */
    public void fade() {
        if (currentPartOfDay != null) {
            if (shouldFade()) {
                this.intensity = switch (currentPartOfDay) {
                    case DAWN, NIGHT -> intensity + getCurrentCycleIntensityStep();
                    case DAY, DUSK -> intensity - getCurrentCycleIntensityStep();
                    default -> intensity;
                };

                if (currentPartOfDay != DayNightCycleStatus.NIGHT) {
                    this.ambientColour = bright;
                } else {
                    // override for NIGHT
                    this.intensity = 0.75f;
                    this.ambientColour = dark;
                }

                this.lastIntensityFade = System.currentTimeMillis();
            }
        }


    }

    /**
     * Gets the intensity to go to from the current part of day.
     * Say if current day is DAWN then the target intensity is DAY
     * and so on.
     *
     * @return The target intensity to reach
     */
    private float getCurrentPartOfDayTargetIntensity() {
        return switch (currentPartOfDay) {
            case DAWN -> DAY_INTENSITY;
            case DAY -> DUSK_INTENSITY;
            case DUSK -> NIGHT_INTENSITY;
            case NIGHT -> DAWN_INTENSITY;
            default -> 0.0f; // never gonna happen
        };
    }

    /**
     * Signals whether we need to fade to next part of day.
     * It's based on the number of milliseconds set per fade step
     * some parts of day fade faster and others slower.
     * Add bounds so it doesn't get too bright or too dark
     *
     * @return true if fade is needed false otherwise
     */
    public boolean shouldFade() {
        if (currentPartOfDay == DayNightCycleStatus.DAWN && intensity <= getCurrentPartOfDayTargetIntensity()) {
            return (System.currentTimeMillis() - lastIntensityFade) >= FADE_DAWN_EVERY;
        } else if (currentPartOfDay == DayNightCycleStatus.NIGHT && intensity <= getCurrentPartOfDayTargetIntensity()) {
            return (System.currentTimeMillis() - lastIntensityFade) >= FADE_NIGHT_EVERY;
        } else if (currentPartOfDay == DayNightCycleStatus.DUSK && intensity >= getCurrentPartOfDayTargetIntensity()) {
            return (System.currentTimeMillis() - lastIntensityFade) >= FADE_EVERY;
        } else if (currentPartOfDay == DayNightCycleStatus.DAY && intensity >= getCurrentPartOfDayTargetIntensity()) {
            return (System.currentTimeMillis() - lastIntensityFade) >= FADE_EVERY;
        }

        return false;
    }

    public Vector3 getAmbientColour() {
        return ambientColour;
    }

}
