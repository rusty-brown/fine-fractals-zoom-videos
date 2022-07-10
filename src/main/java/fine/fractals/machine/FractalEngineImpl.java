package fine.fractals.machine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.PerfectColorDistribution;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.SAVE_IMAGES;
import static fine.fractals.fractal.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.fractal.mandelbrot.MandelbrotImpl.Mandelbrot;
import static fine.fractals.machine.ApplicationImpl.Application;
import static fine.fractals.machine.ApplicationImpl.REPEAT;
import static fine.fractals.machine.ApplicationImpl.iteration;

public class FractalEngineImpl extends Thread {

    private static final Logger log = LogManager.getLogger(FractalEngineImpl.class);

    /**
     * Singleton instance
     */
    public static final FractalEngineImpl FractalEngine = new FractalEngineImpl();
    public static boolean calculationInProgress;

    private boolean first = true;

    private FractalEngineImpl() {
        log.debug("constructor");
    }

    @Override
    public void run() {
        do {
            log.info("Iteration: " + iteration++);

            calculationInProgress = true;

            if (first) {
                first = false;
                Mandelbrot.initializeDomainElements();
            } else {
                Mandelbrot.recalculatePixelsPositionsForThisZoom();
                Mandelbrot.createMaskAndRepaint();
            }

            /*
             * Mandelbrot calculation creates Finebrot data
             */
            Mandelbrot.calculate();

            PerfectColorDistribution.perfectlyColorFinebrotValues();
            Application.repaintFinebrotWindow();

            if (SAVE_IMAGES) {
                FractalMachine.saveImages();
            }
            calculationInProgress = false;
            if (REPEAT) {
                Application.zoomIn();
            }

            if (iteration == 1) {
                AreaMandelbrot.moveToInitialCoordinates();
                AreaFinebrot.moveToInitialCoordinates();
            }
        } while (REPEAT);
    }
}
