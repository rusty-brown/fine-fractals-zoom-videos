package fine.fractals.fractal.finebrot.common;

import fine.fractals.data.annotation.ThreadSafe;
import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.data.mandelbrot.ResolutionMultiplier;
import fine.fractals.machine.ApplicationImpl;
import fine.fractals.perfect.coloring.common.PerfectColorDistributionAbstract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Top level Fine Fractal Implementation
 * Subclasses must define specific abstract method definition or implementation
 * - math() with relevant MemX object
 * - calculatePath()
 * Fractal Types extend {@link FinebrotCommonImpl} or must define implementation for
 * - pixels, paths, and coloring implementation
 */
public abstract class FinebrotAbstractImpl {

    /**
     * 4 worked well for all fractals so far
     * 4 is distance from (0, 0)
     */
    public static final int CALCULATION_BOUNDARY = 4;
    private static final Logger log = LogManager.getLogger(FinebrotAbstractImpl.class);
    /**
     * Image resolution height & width
     * 800  600
     * 1080 1920 full HD high
     * 1920 1080 full HD
     * 2560 1440 quad HD
     * Resolution should be divisible by PixelsMandelbrotImpl.chunkAmount
     */
    public static int RESOLUTION_WIDTH;
    public static int RESOLUTION_HEIGHT;
    public static boolean SAVE_IMAGES;
    public static ResolutionMultiplier RESOLUTION_MULTIPLIER;
    /**
     * Instantiated by fractal type
     */
    public static PerfectColorDistributionAbstract PerfectColorDistribution;
    /**
     * Instantiated by specific fractal
     * A specific fractal which is going to be calculated
     */
    public static FinebrotAbstractImpl FinebrotFractal;
    /**
     * Instantiated by fractal type
     */
    public static PathsFinebrotCommonImpl PathsFinebrot;
    public static String NAME;

    public static int ITERATION_MAX;
    public static int ITERATION_min;

    public static double INIT_FINEBROT_AREA_SIZE;
    public static double INIT_FINEBROT_TARGET_re;
    public static double INIT_FINEBROT_TARGET_im;

    public static double INIT_MANDELBROT_AREA_SIZE;
    public static double INIT_MANDELBROT_TARGET_re;
    public static double INIT_MANDELBROT_TARGET_im;

    private int pathsAmount;
    private int pathsAmount_measure;
    private int pathsAmount_tolerance;
    private int pointsTotal;
    private int pointsTotal_measure;
    private int pointsTotal_tolerance;
    private int elementLong;
    private int elementLong_measure;
    private int elementLong_tolerance;

    private int pathsLength_measure;
    private int pathsLength_tolerance;

    public FinebrotAbstractImpl() {
        log.debug("constructor");
        log.info(this.getClass().getSimpleName());
    }

    @ThreadSafe
    public abstract boolean calculatePath(MandelbrotElement el, final ArrayList<double[]> path);

    public void update() {
        log.debug("update()");

        ITERATION_min += 3;
        ITERATION_MAX += 10;

        final int it = ApplicationImpl.iteration;
        final int frameTakeMeasuresAtFrame = 42;
        double tol = 0.25;

        if (it == frameTakeMeasuresAtFrame) {

            elementLong_measure = elementLong;
            pointsTotal_measure = pointsTotal;
            pathsAmount_measure = pathsAmount;
            pathsLength_measure = (int) ((double) pointsTotal / (double) pathsAmount);

            elementLong_tolerance = (int) (elementLong_measure * tol);
            pointsTotal_tolerance = (int) (pointsTotal_measure * tol);
            pathsAmount_tolerance = (int) (pathsAmount_measure * tol);
            pathsLength_tolerance = (int) (pathsLength_measure * tol);

            log.info("* elementLong_measure = " + elementLong_measure);
            log.info("* pointsTotal_measure = " + pointsTotal_measure);
            log.info("* pathsAmount_measure = " + pathsAmount_measure);
            log.info("* pathsLength_measure = " + pathsLength_measure);

        }
        if (it > frameTakeMeasuresAtFrame) {

            /* elementLong, pointsTotal, pathsAmount were updated by paths() and elements() */

            int pathsLength = (int) ((double) pointsTotal / (double) pathsAmount);

            log.info("man. elements: " + elementLong + " <-> m:" + elementLong_measure + " ... " + (elementLong - elementLong_measure) + " < " + elementLong_tolerance);
            log.info("fin. points:   " + pointsTotal + " <> m:" + pointsTotal_measure + " ... " + (pointsTotal - pointsTotal_measure) + " < " + pointsTotal_tolerance);
            log.info("path lengths:  " + pathsLength + " <> m:" + pathsLength_measure + " ... " + (pathsLength - pathsLength_measure) + " < " + pathsLength_tolerance);
            log.info("path amount:  (" + pathsAmount + " <> m:" + pathsAmount_measure + " ... " + (pathsAmount - pathsAmount_measure) + " < " + pathsAmount_tolerance + ")");

            boolean tooManyLongElements = false;
            if (elementLong > elementLong_measure) {
                tooManyLongElements = elementLong - elementLong_measure > elementLong_tolerance;
            }

            boolean notEnoughPoints = false;
            if (pointsTotal < pointsTotal_measure) {
                notEnoughPoints = pointsTotal_measure - pointsTotal > pointsTotal_tolerance;
            }

            boolean tooManyPoints = false;
            if (pointsTotal > pointsTotal_measure) {
                tooManyPoints = pointsTotal - pointsTotal_measure > pointsTotal_tolerance;
            }


            boolean pathsToShort = false;
            if (pathsLength < pathsLength_measure) {
                pathsToShort = pathsLength_measure - pathsLength > pathsLength_tolerance;
            }

            log.info("");
            log.info(tooManyLongElements + "-" + notEnoughPoints + "-" + pathsToShort);

            if (tooManyPoints) {
                log.info("increase ITERATION_min, too many points");
                ITERATION_min *= 1.02;
            }
            if (notEnoughPoints) {
                log.info("increase ITERATION_MAX, not enough points");
                log.info("decrease ITERATION_min, not enough points");
                ITERATION_MAX *= 1.02;
                ITERATION_min *= 0.98;
            }
        }

        log.info("ITERATION_MAX = " + ITERATION_MAX);
        log.info("ITERATION_min = " + ITERATION_min);
    }

    public void paths(int pathsAmount, int pointsTotal) {
        this.pathsAmount = pathsAmount;
        this.pointsTotal = pointsTotal;
    }

    public void elements(int elementsFinishedLong) {
        this.elementLong = elementsFinishedLong;
    }
}
