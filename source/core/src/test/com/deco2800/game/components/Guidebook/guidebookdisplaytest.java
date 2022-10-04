//package com.deco2800.game.components.Guidebook;
//
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.deco2800.game.AtlantisSinks;
//import com.deco2800.game.extensions.GameExtension;
//import com.deco2800.game.screens.GuidebookScreen;
//import com.deco2800.game.ui.UIComponent;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//
//public class guidebookdisplaytest {
//    private GuidebookDisplay guidebookDisplay;
//
//    @BeforeEach
//    public void beforeEach() {
//        guidebookDisplay = new GuidebookDisplay();
//
//    }
////
//////    @Test
//////    public void notNullUI() {
//////        when(guidebookDisplay.getGuidebook()).thenReturn(isNotNull());
//////        boolean b = (guidebookDisplay.getGuidebook() != null);
//////        assertEquals(b,false);
//////    }
////
//////    @Test
//////    public void randomTest1() {
//////        boolean b = (guidebookDisplay.displayBook() != null);
//////    }
//////
//////    @Test
//////    public void randomTest2() {
//////        boolean b = (guidebookDisplay.getZIndex() == anyFloat());
//////    }
////
////
//
//    @Test
//    public void newLineTest() {
//        String str = "This is a sample string for testing purposes. Hope it works, fingers crossed!!";
//        String correctAnswer ="This is a sample string for testing purposes. " + System.lineSeparator() + "Hope it works, fingers crossed!!";
//        String brokenString =  guidebookDisplay.format(str, 50);
//        assertEquals(correctAnswer, brokenString);
//    }
//
//    public boolean getBookTest() {
//       Table[] tableDisplay = guidebookDisplay.displayBook();
//       for (Object t : tableDisplay) {
//           if (t == null) {
//               return true;
//           }
//       }
//        return false;
//    }
//
//    @Test
//    public void displayNotNull() {
//        assertEquals(getBookTest(), false);
//    }
//}
