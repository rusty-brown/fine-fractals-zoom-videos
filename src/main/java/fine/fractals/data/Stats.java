package fine.fractals.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Stats {

    private static final Logger log = LogManager.getLogger(Stats.class);

    /*
     * Mandelbrot stats
     */

    public static int newElementsTooLong;
    public static int newElementsTooShort;
    public static int newElementsLong;
    private static int newElementsLong_measure;
    private static int newElementsLong_tolerance;

    /*
     * Finebrot stats
     */

    /*
     * All paths including previous calculation,
     * Amount of added paths now is not the same as red elementLong
     */
    public static int pathsTotalAmount;
    public static int pathsNewPointsAmount;
    private static int pathsTotalAmount_measure;
    private static int pathsTotalAmount_tolerance;
    public static int pixelsValueTotal;
    private static int pixelsValueTotal_measure;
    private static int pixelsValueTotal_tolerance;

    private static int averagePathLength_measure;

    /*
     * Actions
     */

    public static boolean notEnoughPixelsTotalValue = false;
    public static boolean lessPixelsTotalValue = false;
    public static boolean tooManyPixelsTotalValue = false;
    public static boolean tooManyPathsTotal = false;
    public static boolean notEnoughLongElements = false;

    private static final int TAKE_MEASURES_AT_FRAME = 42;

    private static void frame(int it) {
        if (it == TAKE_MEASURES_AT_FRAME) {

            newElementsLong_measure = newElementsLong;
            pixelsValueTotal_measure = pixelsValueTotal;
            pathsTotalAmount_measure = pathsTotalAmount;
            averagePathLength_measure = (int) ((double) pathsTotalAmount / (double) pixelsValueTotal);

            newElementsLong_tolerance = (int) (newElementsLong_measure * 0.5);
            pixelsValueTotal_tolerance = (int) (pixelsValueTotal_measure * 0.5);
            pathsTotalAmount_tolerance = (int) (pathsTotalAmount_measure * 0.5);

            log.info("* elementsLong_measure = " + newElementsLong_measure);
            log.info("* pixelsValueTotal_measure = " + pixelsValueTotal_measure);
            log.info("* pathsTotalAmount_measure = " + pathsTotalAmount_measure);
            log.info("* averagePathLength_measure = " + averagePathLength_measure);
        }
    }

    public static void update(int it) {
        frame(it);

        if (it > TAKE_MEASURES_AT_FRAME) {
            notEnoughPixelsTotalValue = false;
            if (pixelsValueTotal < pixelsValueTotal_measure) {
                notEnoughPixelsTotalValue = pixelsValueTotal_measure - pixelsValueTotal > pixelsValueTotal_tolerance;
            }

            tooManyPixelsTotalValue = false;
            if (pixelsValueTotal > pixelsValueTotal_measure) {
                tooManyPixelsTotalValue = pixelsValueTotal - pixelsValueTotal_measure > pixelsValueTotal_tolerance;
            }

            tooManyPathsTotal = false;
            if (pathsTotalAmount > pathsTotalAmount_measure) {
                tooManyPathsTotal = pathsTotalAmount - pathsTotalAmount_measure > pathsTotalAmount_tolerance;
            }

            notEnoughLongElements = false;
            if (newElementsLong < newElementsLong_measure) {
                notEnoughLongElements = newElementsLong_measure - newElementsLong > newElementsLong_tolerance;
            }

            lessPixelsTotalValue = pixelsValueTotal < pixelsValueTotal_measure;

            log.debug("> notEnoughPixelsTotalValue: " + notEnoughPixelsTotalValue);
            log.debug("> lessPixelsTotalValue:      " + lessPixelsTotalValue);
            log.debug("> tooManyPixelsTotalValue:   " + tooManyPixelsTotalValue);
            log.debug("> tooManyPathsTotal:         " + tooManyPathsTotal);
            log.debug("> notEnoughLongElements:     " + notEnoughLongElements);

            final int averagePathLength = (int) ((double) pathsTotalAmount / (double) pixelsValueTotal);
            final int newElementsAll = newElementsLong + newElementsTooShort + newElementsTooLong;
            final double domainElementsToNewCalculationPathPoints = ((double) pathsNewPointsAmount) / ((double) newElementsAll);

            log.info(String.format("newElementsAll:    %s \t(%s)", averagePathLength, averagePathLength_measure));
            log.info(String.format("averagePathLength: %s \t(%s)", averagePathLength, averagePathLength_measure));

            log.info("domainElementsToNewCalculationPathPoints:   " + domainElementsToNewCalculationPathPoints);
        }
    }

    public static void clean() {
        newElementsTooLong = 0;
        newElementsTooShort = 0;
        newElementsLong = 0;
        pathsTotalAmount = 0;
        pixelsValueTotal = 0;
        pathsNewPointsAmount = 0;
    }

    public static void print() {
        log.trace("newElementsTooLong    " + newElementsTooLong);
        log.trace("newElementsTooShort   " + newElementsTooShort);
        log.trace("newElementsLong       " + newElementsLong);
        log.trace("pathsTotalAmount      " + pathsTotalAmount);
        log.trace("pixelsValueTotal      " + pixelsValueTotal);
        log.trace("pathsNewPointsAmount  " + pathsNewPointsAmount);
    }
}
