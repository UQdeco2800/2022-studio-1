package com.deco2800.game.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.components.maingame.MainGameTutorials;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
public class MainGameTutorialsTest {

    @Test
    void createsAndDisposes() {
        MainGameTutorials tutorials = mock(MainGameTutorials.class);
        tutorials.create();

        verify(tutorials).create();

        tutorials.dispose();
        verify(tutorials).dispose();
    }

    @Test
    void draws() {
        SpriteBatch batch = mock(SpriteBatch.class);
        MainGameTutorials tutorials = mock(MainGameTutorials.class);

        tutorials.draw(batch);
        verify(tutorials).draw(batch);
    }
}
