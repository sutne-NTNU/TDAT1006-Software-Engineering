package client.view.scenes;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class Utils
{
    /**
     * Converts a Color object to a CSS rgba color string.
     */
    public static String toCSS(Color color)
    {
        int r = (int)Math.round(255 * color.getRed());
        int g = (int)Math.round(255 * color.getGreen());
        int b = (int)Math.round(255 * color.getBlue());
        int a = (int)Math.round(255 * color.getOpacity());
        return String.format("rgba(%s,%s,%s,%s)", r, g, b, a);
    }

    /**
     * @return true if the given TextField has content.
     */
    public static boolean hasContent(TextField field)
    {
        return field.getText().trim().length() > 0;
    }
}
