package client.view.scenes;

import static org.junit.Assert.assertEquals;

import javafx.scene.paint.Color;
import org.junit.Test;

public class UtilsTest
{
    @Test
    public void toCSSTest()
    {
        Color red = Color.RED;
        assertEquals("rgba(255,0,0,255)", Utils.toCSS(red));
        Color white = Color.WHITE;
        assertEquals("rgba(255,255,255,255)", Utils.toCSS(white));
        Color black = Color.BLACK;
        assertEquals("rgba(0,0,0,255)", Utils.toCSS(black));

        Color custom = Color.web("rgb(1,2,3)");
        assertEquals("rgba(1,2,3,255)", Utils.toCSS(custom));
    }
}
