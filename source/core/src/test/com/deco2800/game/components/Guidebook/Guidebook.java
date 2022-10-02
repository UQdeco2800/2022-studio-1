//package com.deco2800.game.components.Guidebook;
//
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.deco2800.game.components.Component;
//import com.deco2800.game.extensions.GameExtension;
//import com.deco2800.game.rendering.DayNightCycleComponent;
//import com.deco2800.game.rendering.RenderService;
//import com.deco2800.game.screens.GuidebookScreen;
//import com.deco2800.game.services.ServiceLocator;
//import com.deco2800.game.ui.UIComponent;
//import com.deco2800.game.ui.terminal.commands.Command;
//import com.deco2800.game.utils.RenderUtil;
//import org.junit.Test;
//import com.deco2800.game.components.Guidebook.GuidebookDisplay;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.spy;
//
//@ExtendWith(GameExtension.class)
//@ExtendWith(MockitoExtension.class)
//public class Guidebook {
//    @Mock
//    private Guidebook guidebook;
//    private GuidebookDisplay guidebookDisplay;
//    private UIComponent ui;
//
//
//    @BeforeEach
//    void beforeEach() {
//        Component component = spy(Component.class);
//        RenderUtil renderUtil = Mockito.spy(RenderUtil.getInstance());
//        RenderService renderService = Mockito.spy(ServiceLocator.getRenderService());
//        Texture texture = mock(Texture.class);
//        SpriteBatch sprite = mock(SpriteBatch.class);
//        guidebook = new Guidebook();
//        guidebookDisplay = new GuidebookDisplay();
//        ui = mock(UIComponent.class);
//
//    }
//
////Guidebook guidebook = mock(Guidebook.class);
////UIComponent ui = mock(UIComponent.class);
////GuidebookDisplay guidebookDisplay = new GuidebookDisplay();
//
//
//
//
//    @Test
//    public void newLineTest() {
//
//        String str = "This is a sample string for testing purposes. Hope it works, fingers crossed!!";
//        String correctAnswer ="This is a sample string for testing purposes. " + System.lineSeparator() + "Hope it works, fingers crossed!!";
//        String brokenString =  guidebookDisplay.lineAmount(str);
//        assertEquals(correctAnswer, brokenString);
//
//    }
//
////    @Test
////    void testUI() {
////
////    }
////
////    @Test
////    void getContentText() {
////
////    }
//
//}
