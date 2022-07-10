package fine.fractals.data.mandelbrot;

import java.awt.*;

public class MandelbrotMaskColors {

    public static final Color ACTIVE_NEW = new Color(40, 180, 150);
    public static final Color FINISHED_TOO_LONG = new Color(0, 0, 0);
    public static final Color HIBERNATED_DEEP_BLACK = new Color(40, 40, 40);
    public static final Color FINISHED_TOO_SHORT = new Color(220, 220, 240);
    public static final Color FINISHED_SUCCESS = new Color(255, 0, 0);
    public static final Color FINISHED_SUCCESS_PAST = new Color(92, 0, 0);

    /**
     * For refresh during Mandelbrot pixels recalculation.
     */
    public static final Color NULL = new Color(90, 90, 99);

}
