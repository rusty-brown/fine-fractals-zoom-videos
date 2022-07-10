package fine.fractals.machine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.PerfectColorDistribution;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.SAVE_IMAGES;
import static fine.fractals.fractal.mandelbrot.MandelbrotImpl.Mandelbrot;

public class FractalEngineImpl {

    private static final Logger log = LogManager.getLogger(FractalEngineImpl.class);

    /**
     * Singleton instance
     */
    public static final FractalEngineImpl FractalEngine;
    public static boolean calculationInProgress;

    static {
        log.info("init");
        FractalEngine = new FractalEngineImpl();
    }

    private boolean updateDomain = false;
    private boolean first = true;

    private FractalEngineImpl() {
        log.debug("constructor");
    }

    public void calculate() {
        log.info("calculate()");
        calculationInProgress = true;

        if (first) {
            first = false;
            Mandelbrot.domainScreenCreateInitialization();
        }

        if (updateDomain) {
            Mandelbrot.domainForThisZoom();
            updateDomain = false;
        }
        Mandelbrot.createMaskAndRepaint();

        /*
         * Mandelbrot calculation creates Finebrot data
         */
        Mandelbrot.calculate();

        PerfectColorDistribution.perfectlyColorFinebrotValues();

        if (SAVE_IMAGES) {
            FractalMachine.saveImages();
        }
        calculationInProgress = false;
    }

    public void updateDomain() {
        this.updateDomain = true;
    }
}
