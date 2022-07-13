package fine.fractals.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Stats {

    private static final Logger log = LogManager.getLogger(Stats.class);

    /*
     * Mandelbrot stats
     */

    public static int elementsTooLong;
    public static int elementsTooShort;
    public static int elementsLong;
    private static int elementsLong_measure;
    private static int elementsLong_tolerance;

    /*
     * Finebrot stats
     */

    /* all paths including previous calculation, amount of added paths now is same as elementLong*/
    public static int pathsTotalAmount;
    private static int pathsTotalAmount_measure;
    private static int pathsTotalAmount_tolerance;
    public static int pixelsValueTotal;
    private static int pixelsValueTotal_measure;
    private static int pixelsValueTotal_tolerance;

    /*
     * Actions
     */

    public static boolean notEnoughPixelsTotalValue = false;
    public static boolean lessPixelsTotalValue = false;
    public static boolean tooManyPixelsTotalValue = false;
    public static boolean tooManyPathsTotal = false;
    public static boolean notEnoughLongElements = false;

    private static final int TAKE_MEASURES_AT_FRAME = 42;

    public static void frame(int it) {
        if (it == TAKE_MEASURES_AT_FRAME) {

            elementsLong_measure = elementsLong;
            pixelsValueTotal_measure = pixelsValueTotal;
            pathsTotalAmount_measure = pathsTotalAmount;

            elementsLong_tolerance = (int) (elementsLong_measure * 0.5);
            pixelsValueTotal_tolerance = (int) (pixelsValueTotal_measure * 0.5);
            pathsTotalAmount_tolerance = (int) (pathsTotalAmount_measure * 0.5);

            log.info("* elementsLong_measure = " + elementsLong_measure);
            log.info("* pixelsValueTotal_measure = " + pixelsValueTotal_measure);
            log.info("* pathsTotalAmount_measure = " + pathsTotalAmount_measure);
        }
    }

    public static void update(int it) {
        if (it > TAKE_MEASURES_AT_FRAME) {

            notEnoughPixelsTotalValue = false;
            if (pixelsValueTotal < pixelsValueTotal_measure) {
                notEnoughPixelsTotalValue = pixelsValueTotal_measure - pixelsValueTotal > pixelsValueTotal_tolerance;
            }

            lessPixelsTotalValue = pixelsValueTotal < pixelsValueTotal_measure;

            tooManyPixelsTotalValue = false;
            if (pixelsValueTotal > pixelsValueTotal_measure) {
                tooManyPixelsTotalValue = pixelsValueTotal - pixelsValueTotal_measure > pixelsValueTotal_tolerance;
            }

            tooManyPathsTotal = false;
            if (pathsTotalAmount > pathsTotalAmount_measure) {
                tooManyPathsTotal = pathsTotalAmount - pathsTotalAmount_measure > pathsTotalAmount_tolerance;
            }

            notEnoughLongElements = false;
            if (elementsLong < elementsLong_measure) {
                notEnoughLongElements = elementsLong_measure - elementsLong > elementsLong_tolerance;
            }

            log.debug("> notEnoughPixelsTotalValue: " + notEnoughPixelsTotalValue);
            log.debug("> lessPixelsTotalValue:      " + lessPixelsTotalValue);
            log.debug("> tooManyPixelsTotalValue:   " + tooManyPixelsTotalValue);
            log.debug("> tooManyPathsTotal:         " + tooManyPathsTotal);
            log.debug("> notEnoughLongElements:     " + notEnoughLongElements);
        }
    }

    public static void clean() {
        elementsTooLong = 0;
        elementsTooShort = 0;
        elementsLong = 0;
        pathsTotalAmount = 0;
        pixelsValueTotal = 0;
    }
}
