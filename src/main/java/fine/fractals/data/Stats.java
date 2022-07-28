package fine.fractals.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.PixelsFinebrot;

public class Stats {

    private static final Logger log = LogManager.getLogger(Stats.class);

    private static final int TAKE_MEASURES_AT_FRAME = 20;
    public static int newElementsTooLong;
    public static int newElementsTooShort;
    public static int newElementsLong;

    /*
     * All paths including previous calculation,
     * Amount of added paths now is not the same as red elementLong
     */
    public static int pathsTotalAmount;

    public static int pathsNewPointsAmount;
    public static int pixelsValueTotal;
    public static int pixelsValueBest;

    public static boolean notEnoughPixelsTotalValue = false;
    public static boolean lessPixelsTotalValue = false;
    public static boolean tooManyPixelsTotalValue = false;

    public static boolean notEnoughPixelsBestValue = false;
    public static boolean lessPixelsBestValue = false;
    public static boolean tooManyPixelsBestValue = false;

    public static boolean tooManyPathsTotal = false;
    public static boolean notEnoughLongElements = false;

    private static int newElementsLong_measure;
    private static int newElementsLong_tolerance;
    private static int pathsTotalAmount_measure;
    private static int pathsTotalAmount_tolerance;
    private static int pixelsValueTotal_measure;
    private static int pixelsValueTotal_tolerance;
    private static int pixelsValueBest_measure;
    private static int pixelsValueBest_tolerance;
    private static int averagePathLength_measure;

    private static void frame(int it) {
        if (it == TAKE_MEASURES_AT_FRAME) {

            log.debug("newElementsLong  " + newElementsLong);
            log.debug("pixelsValueTotal " + pixelsValueTotal);
            log.debug("pathsTotalAmount " + pathsTotalAmount);

            newElementsLong_measure = newElementsLong;
            pixelsValueTotal_measure = pixelsValueTotal;
            pathsTotalAmount_measure = pathsTotalAmount;
            averagePathLength_measure = (int) ((double) pixelsValueTotal / (double) pathsTotalAmount);
            pixelsValueBest_measure = PixelsFinebrot.bestFourChunksValue();

            newElementsLong_tolerance = (int) (newElementsLong_measure * 0.5);
            pixelsValueTotal_tolerance = (int) (pixelsValueTotal_measure * 0.5);
            pathsTotalAmount_tolerance = (int) (pathsTotalAmount_measure * 0.5);
            pixelsValueBest_tolerance = (int) (pixelsValueBest_measure * 0.5);

            log.info("* elementsLong_measure      = " + newElementsLong_measure);
            log.info("* pixelsValueTotal_measure  = " + pixelsValueTotal_measure);
            log.info("* pixelsValueBest_measure   = " + pixelsValueBest_measure);
            log.info("* pathsTotalAmount_measure  = " + pathsTotalAmount_measure);
            log.info("* averagePathLength_measure = " + averagePathLength_measure);
        }
    }

    public static void update(int it) {
        frame(it);

        if (it > TAKE_MEASURES_AT_FRAME) {

            /* Total value */
            notEnoughPixelsTotalValue = false;
            if (pixelsValueTotal < pixelsValueTotal_measure) {
                notEnoughPixelsTotalValue = pixelsValueTotal_measure - pixelsValueTotal > pixelsValueTotal_tolerance;
            }
            tooManyPixelsTotalValue = false;
            if (pixelsValueTotal > pixelsValueTotal_measure) {
                tooManyPixelsTotalValue = pixelsValueTotal - pixelsValueTotal_measure > pixelsValueTotal_tolerance;
            }
            lessPixelsTotalValue = pixelsValueTotal < pixelsValueTotal_measure;

            /* Best chunks value */
            notEnoughPixelsBestValue = false;
            pixelsValueBest = PixelsFinebrot.bestFourChunksValue();
            if (pixelsValueBest < pixelsValueBest_measure) {
                notEnoughPixelsBestValue = pixelsValueBest_measure - pixelsValueBest > pixelsValueBest_tolerance;
            }
            tooManyPixelsBestValue = false;
            if (pixelsValueBest > pixelsValueBest_measure) {
                tooManyPixelsBestValue = pixelsValueBest - pixelsValueBest_measure > pixelsValueBest_tolerance;
            }
            lessPixelsBestValue = pixelsValueBest < pixelsValueBest_measure;

            /* Paths */
            tooManyPathsTotal = false;
            if (pathsTotalAmount > pathsTotalAmount_measure) {
                tooManyPathsTotal = pathsTotalAmount - pathsTotalAmount_measure > pathsTotalAmount_tolerance;
            }

            /* Mandelbrot long elements */
            notEnoughLongElements = false;
            if (newElementsLong < newElementsLong_measure) {
                notEnoughLongElements = newElementsLong_measure - newElementsLong > newElementsLong_tolerance;
            }

            log.debug("> notEnoughPixelsTotalValue: " + notEnoughPixelsTotalValue);
            log.debug("> lessPixelsTotalValue:      " + lessPixelsTotalValue);
            log.debug("> lessPixelsBestValue:       " + lessPixelsBestValue + " (" + pixelsValueBest + " < " + pixelsValueBest_measure + ")");
            log.debug("> tooManyPixelsTotalValue:   " + tooManyPixelsTotalValue);
            log.debug("> tooManyPathsTotal:         " + tooManyPathsTotal);
            log.debug("> notEnoughLongElements:     " + notEnoughLongElements);

            final int averagePathLength = (int) ((double) pixelsValueTotal / (double) pathsTotalAmount);
            final int newElementsAll = newElementsLong + newElementsTooShort + newElementsTooLong;
            final double domainElementsToNewCalculationPathPoints = ((double) pathsNewPointsAmount) / ((double) newElementsAll);

            log.debug(String.format("averagePathLength: %s \t(%s)", averagePathLength, averagePathLength_measure));
            log.debug("domainElementsToNewCalculationPathPoints:   " + domainElementsToNewCalculationPathPoints);
        }
    }

    public static void clean() {
        newElementsTooLong = 0;
        newElementsTooShort = 0;
        newElementsLong = 0;
        pathsTotalAmount = 0;
        pixelsValueTotal = 0;
        pixelsValueBest = 0;
        pathsNewPointsAmount = 0;
    }

    public static void print() {
        log.trace("newElementsTooLong    " + newElementsTooLong);
        log.trace("newElementsTooShort   " + newElementsTooShort);
        log.trace("newElementsLong       " + newElementsLong);
        log.trace("pathsTotalAmount      " + pathsTotalAmount);
        log.trace("pixelsValueTotal      " + pixelsValueTotal);
        log.debug("pixelsValueBest       " + pixelsValueBest);
        log.trace("pathsNewPointsAmount  " + pathsNewPointsAmount);
    }
}
