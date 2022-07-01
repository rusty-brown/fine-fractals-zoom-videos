package fine.fractals.context;

import fine.fractals.machine.FractalMachine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.ApplicationImpl.*;
import static fine.fractals.context.finebrot.FinebrotImpl.Finebrot;
import static fine.fractals.context.mandelbrot.MandelbrotImpl.Mandelbrot;
import static fine.fractals.perfect.coloring.PerfectColorDistributionImpl.PerfectColorDistribution;

public class FractalEngineImpl {

    private static final Logger log = LogManager.getLogger(FractalEngineImpl.class);
    public static boolean calculationInProgress;
    public static String calculationProgress;
    public static String calculationText;
    public static int[] calculationProgressPoint = null;

    public static FractalEngineImpl FractalEngine;

    private boolean updateDomain = false;

    static {
        log.info("init");
        FractalEngine = new FractalEngineImpl();
    }

    private FractalEngineImpl() {
    }

    public void calculate() {

        log.info("calculateFromThread");

        Finebrot.clear();

        calculationInProgress = true;

        // Calculate Design
        if (updateDomain) {
            Mandelbrot.domainForThisZoom();
            log.info("new domain done");
            updateDomain = false;
        }

        log.trace("repaint mandelbrot mask - after optimization break");
        Mandelbrot.createMask();
        Application.repaintMandelbrotWindow();

        log.info("CALCULATE");
        Mandelbrot.calculate();

        if (REPEAT) {
            /* Test if Optimization didn't break anything */
            Mandelbrot.fixOptimizationBreak();
        }

        log.info("ScreenValuesToImages");
        PerfectColorDistribution.clear();
        PerfectColorDistribution.perfectlyColorScreenValues();

        /* save file based on screen height; don't save it for testing */
        if (RESOLUTION_WIDTH >= RESOLUTION_IMAGE_SAVE_FOR) {
            log.info("Save images");
            FractalMachine.saveImage();
            log.info("Save images DONE");
        }
        log.info("DONE");
        calculationInProgress = false;
        calculationProgressPoint = null;
        Application.repaintWindows();
    }

    public void updateDomain() {
        this.updateDomain = true;
    }

}
