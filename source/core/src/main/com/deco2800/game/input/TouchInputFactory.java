package com.deco2800.game.input;

import com.deco2800.game.components.player.TouchPlayerInputComponent;
import com.deco2800.game.ui.terminal.TouchTerminalInputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TouchInputFactory extends InputFactory {
  private static final Logger logger = LoggerFactory.getLogger(TouchInputFactory.class);

  /**
   * Creates an input handler for the player
   *
   * @return Player input handler
   */
  @Override
  public InputComponent createForPlayer() {
    logger.debug("Creating player input handler");
    return new TouchPlayerInputComponent();
  }

  /**
   * Creates an input handler for the terminal
   *
   * @return Terminal input handler
   */
  @Override
  public InputComponent createForTerminal() {
    logger.debug("Creating terminal input handler");
    return new TouchTerminalInputComponent();
  }

  @Override
  public InputComponent createForCamera() {
    logger.debug("Creating camera input handler");
    return null; // FOR MOUSE PANNING AND ZOOMING
  }

  @Override
  public InputComponent createForStoryLine() {
    logger.debug("Creating Storyline input handler");
    return null; //could be used for touch screen transitions
  }
}
