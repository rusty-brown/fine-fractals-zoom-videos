package fine.fractals.fractal.finebrot.common;

import fine.fractals.data.Stats;
import fine.fractals.data.mandelbrot.ResolutionMultiplier;
import fine.fractals.fractal.finebrot.PixelsFinebrotImpl;
import fine.fractals.fractal.mandelbrot.MandelbrotCommonImpl;
import fine.fractals.perfect.coloring.common.PerfectColorDistributionAbstract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.machine.ApplicationImpl.iteration;

/**
 * Top parent class for Fine Fractals
 * Each fractal type must implement methods
 * - math()
 * - calculatePath()
 * Fractal Types extend {@link FinebrotCommonImpl} or must define implementation for
 * - pixels, paths, and coloring implementation
 */
public abstract class FinebrotAbstractImpl {

    private static final Logger log = LogManager.getLogger(FinebrotAbstractImpl.class);
    /**
     * 4 is quadrance from (0, 0)
     * If intermediate calculation result [re,im] spirals beyond this boundary. Calculation stops as divergent.
     */
    public static final int CALCULATION_BOUNDARY = 4;
    /**
     * Delete shorter paths then this
     */
    public static final int TOLERATE_PATH_LENGTH_min = 4;
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

    /*
     * Application singletons
     */
    public static PixelsFinebrotImpl PixelsFinebrot;
    public static MandelbrotCommonImpl Mandelbrot;
    public static FinebrotAbstractImpl FinebrotFractal;
    public static PathsFinebrotCommonImpl PathsFinebrot;
    public static PerfectColorDistributionAbstract PerfectColorDistribution;

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

    public void update() {
        log.debug("update()");

        ITERATION_MAX += 150;

        Stats.update(iteration);

        if (Stats.notEnoughPixelsBestValue) {
            log.info("increase ITERATION_MAX, not enough Points");
            ITERATION_MAX += 20_000;
        }
        if (Stats.lessPixelsBestValue) {
            ITERATION_MAX += 2_000;
            log.info("increase ITERATION_MAX, bit less Points");
        }
        if (Stats.tooManyPathsTotal) {
            log.info("increase a bit ITERATION_min, too many paths total");
            ITERATION_min += 1;
        }

        Stats.print();
        Stats.clean();

        log.info("ITERATION_MAX = " + ITERATION_MAX);
        log.info("ITERATION_min = " + ITERATION_min);
    }
}
