package com.deco2800.game.input;

import com.deco2800.game.components.camera.KeyboardCameraInputComponent;
import com.deco2800.game.components.player.KeyboardPlayerInputComponent;
import com.deco2800.game.components.storyline.StoryLineInputComponent;
import com.deco2800.game.ui.terminal.KeyboardTerminalInputComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyboardInputFactory creates input handlers that process keyboard and touch
 * support.
 */
public class KeyboardInputFactory extends InputFactory {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardInputFactory.class);

    /**
     * Creates an input handler for the player.
     * 
     * @return Player input handler
     */
    @Override
    public InputComponent createForPlayer() {
        logger.debug("Creating player input handler");
        return new KeyboardPlayerInputComponent();
    }

    /**
     * Creates an input handler for the terminal.
     *
     * @return Terminal input handler
     */
    public InputComponent createForTerminal() {
        logger.debug("Creating terminal input handler");
        return new KeyboardTerminalInputComponent();
    }

    /**
     * Creates an input handler for the camera.
     * 
     * @return Camera input handler
     */
    @Override
    public InputComponent createForCamera() {
        logger.debug("Creating camera input handler");
        return new KeyboardCameraInputComponent();
    }

    /**
     * Creates an input handler for the Storyline
     * @return Storyline input handler
     */
    public InputComponent createForStoryLine() {
        logger.debug("Creating Storyline input handler");
        return new StoryLineInputComponent();
    }
}
