package fine.fractals.context;

import fine.fractals.machine.FractalMachine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.context.ApplicationImpl.REPEAT;
import static fine.fractals.context.ApplicationImpl.SAVE_IMAGES;
import static fine.fractals.fractal.finebrot.common.FinebrotFractalImpl.PerfectColorDistribution;
import static fine.fractals.fractal.finebrot.finite.FractalFinite.PixelsFinebrot;
import static fine.fractals.fractal.mandelbrot.MandelbrotImpl.Mandelbrot;

public class FractalEngineImpl {

    private static final Logger log = LogManager.getLogger(FractalEngineImpl.class);

    public static boolean calculationInProgress;

    public static final FractalEngineImpl FractalEngine;

    private boolean updateDomain = false;

    static {
        log.info("init");
        FractalEngine = new FractalEngineImpl();
    }

    private FractalEngineImpl() {
    }

    public void calculate() {
        log.info("calculate()");

        calculationInProgress = true;

        PixelsFinebrot.clear();

        if (updateDomain) {
            Mandelbrot.domainForThisZoom();
            updateDomain = false;
        }

        Mandelbrot.createMask();

        /*
         * Mandelbrot calculation creates Finebrot data and image
         */
        Mandelbrot.calculate();

        if (REPEAT) {
            /* Test if Optimization didn't break anything */
            Mandelbrot.fixOptimizationBreak();

            Mandelbrot.createMask();
        }

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
