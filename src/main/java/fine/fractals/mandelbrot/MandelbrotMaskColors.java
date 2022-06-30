package fine.fractals.mandelbrot;

import java.awt.*;

public class MandelbrotMaskColors {

    public static Color NULL = Color.CYAN;
    public static Color MOVED = Color.YELLOW;
    public static Color ACTIVE_NEW = Color.GREEN;
    public static Color ACTIVE_FIXED = Color.BLUE;
    public static Color HIBERNATED_BLACK = Color.BLACK;
    public static Color HIBERNATED_BLACK_NEIGHBOR = Color.GRAY;
    public static Color FINISHED_OUT = Color.WHITE;
    public static Color FINISHED_IN = Color.RED;
    public static Color ERROR = Color.ORANGE;
    /**
     * For optimization fix
     */
    public static Color RECALCULATE = new Color(155, 1, 198);

}
