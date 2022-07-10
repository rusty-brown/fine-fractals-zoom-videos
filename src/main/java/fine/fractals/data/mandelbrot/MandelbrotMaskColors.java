package fine.fractals.data.mandelbrot;

import java.awt.*;

import static java.awt.Color.BLACK;
import static java.awt.Color.GRAY;
import static java.awt.Color.GREEN;
import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static java.awt.Color.YELLOW;

public class MandelbrotMaskColors {

    public static final Color ACTIVE_NEW = GREEN;
    public static final Color FINISHED_TOO_LONG = BLACK;
    public static final Color HIBERNATED_DEEP_BLACK = GRAY;
    public static final Color FINISHED_TOO_SHORT = WHITE;
    public static final Color FINISHED_SUCCESS = RED;
    public static final Color FINISHED_SUCCESS_PAST = LIGHT_GRAY;

    /**
     * For refresh during Mandelbrot pixels recalculation.
     */
    public static final Color NULL = YELLOW;

}
