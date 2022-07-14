package fine.fractals.fractal.finebrot.common;

import fine.fractals.data.Stats;
import fine.fractals.data.annotation.ThreadSafe;
import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.data.mandelbrot.ResolutionMultiplier;
import fine.fractals.perfect.coloring.common.PerfectColorDistributionAbstract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.machine.ApplicationImpl.iteration;

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
     *  800  600
     * 1280  720
     * 1080 1920 full HD high
     * 1920 1080 full HD
     * 2560 1440 quad HD
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

    public FinebrotAbstractImpl() {
        log.debug("constructor");
        log.info(this.getClass().getSimpleName());
    }

    @ThreadSafe
    public abstract boolean calculatePath(MandelbrotElement el, final ArrayList<double[]> path);

    public void update() {
        log.debug("update()");

        ITERATION_min += 1;
        ITERATION_MAX += 250;

        Stats.update(iteration);

        if (Stats.notEnoughPixelsTotalValue) {
            log.info("increase ITERATION_MAX, not enough Points (2)");
            ITERATION_MAX += 1_000;
        }
        if (Stats.lessPixelsTotalValue) {
            ITERATION_MAX += 1_000;
            log.info("increase ITERATION_MAX, bit less Points (+)");
        }

        Stats.clean();

        log.info("ITERATION_MAX = " + ITERATION_MAX);
        log.info("ITERATION_min = " + ITERATION_min);
    }
}
