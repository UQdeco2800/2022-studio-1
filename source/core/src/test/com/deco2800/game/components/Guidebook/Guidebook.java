//package com.deco2800.game.components.Guidebook;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.deco2800.game.entities.Entity;
//import com.deco2800.game.extensions.GameExtension;
//import com.deco2800.game.ui.UIComponent;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.plugins.MockMaker;
//
//import static jdk.dynalink.linker.support.Guards.isNotNull;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(GameExtension.class)
//@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class)
//public class Guidebook {
//    @Mock private Guidebook guidebook;
//    @Mock private GuidebookDisplay guidebookDisplay;
//    @Mock private UIComponent ui;
//
//
//    @Before
//    public void before () {
//        MockitoAnnotations.openMocks(this);
//        guidebookDisplay = new GuidebookDisplay();
//
//    }
//
////    @Test
////    public void notNullUI() {
////        when(guidebookDisplay.getGuidebook()).thenReturn(isNotNull());
////        boolean b = (guidebookDisplay.getGuidebook() != null);
////        assertEquals(b,false);
////    }
//
////    @Test
////    public void randomTest1() {
////        boolean b = (guidebookDisplay.displayBook() != null);
////    }
////
////    @Test
////    public void randomTest2() {
////        boolean b = (guidebookDisplay.getZIndex() == anyFloat());
////    }
//
//
//
//    @Test
//    public void newLineTest() {
//        String str = "This is a sample string for testing purposes. Hope it works, fingers crossed!!";
//        String correctAnswer ="This is a sample string for testing purposes. " + System.lineSeparator() + "Hope it works, fingers crossed!!";
//        String brokenString =  guidebookDisplay.format(str, 50);
//        assertEquals(correctAnswer, brokenString);
//    }
//
//
//}
