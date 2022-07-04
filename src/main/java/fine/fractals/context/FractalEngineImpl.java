package fine.fractals.context;

import fine.fractals.machine.FractalMachine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.Main.SAVE_IMAGES;
import static fine.fractals.context.ApplicationImpl.Application;
import static fine.fractals.context.ApplicationImpl.REPEAT;
import static fine.fractals.context.finebrot.FinebrotImpl.Finebrot;
import static fine.fractals.context.mandelbrot.MandelbrotImpl.Mandelbrot;
import static fine.fractals.perfect.coloring.PerfectColorDistributionImpl.PerfectColorDistribution;

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

        Finebrot.clear();

        if (updateDomain) {
            Mandelbrot.domainForThisZoom();
            updateDomain = false;
        }

        Mandelbrot.createMask();
        Application.repaintMandelbrotWindow();

        /*
         * Mandelbrot calculation creates Finebrot data and image
         */
        Mandelbrot.calculate();

        if (REPEAT) {
            /* Test if Optimization didn't break anything */
            Mandelbrot.fixOptimizationBreak();

            Mandelbrot.createMask();
            Application.repaintMandelbrotWindow();
        }

        PerfectColorDistribution.perfectlyColorScreenValues();

        if (SAVE_IMAGES) {
            FractalMachine.saveImages();
        }
        calculationInProgress = false;
        Application.repaintWindows();
    }

    public void updateDomain() {
        this.updateDomain = true;
    }
}
