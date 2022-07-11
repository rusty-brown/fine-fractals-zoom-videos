package fine.fractals.machine;

import fine.fractals.images.FractalImages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.FinebrotFractal;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.PathsFinebrot;
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
            log.info("------------------------------- " + (++iteration) + " ---");
            calculationInProgress = true;

            /*
             * Make Mandelbrot domain for this calculation
             */
            if (first) {
                first = false;
                Mandelbrot.initializeDomainElements();
            } else {
                Mandelbrot.recalculatePixelsPositionsForThisZoom();
            }

            Mandelbrot.maskFullUpdate();
            Application.repaintMandelbrotWindow();

            /*
             * Execute calculation machinery
             */
            Mandelbrot.calculate();

            /*
             * Mirror calculation paths to Finebrot pixels
             */
            PathsFinebrot.domainToScreenGrid();

            Mandelbrot.maskFullUpdate();
            Application.repaintMandelbrotWindow();

            /*
             * Paint and refresh updated Finebrot
             */
            PerfectColorDistribution.perfectlyColorFinebrotValues();
            Application.repaintFinebrotWindow();

            if (SAVE_IMAGES) {
                FractalImages.saveImages();
            }
            calculationInProgress = false;
            if (REPEAT) {

                /*
                 * Update relevant variables to decide if ITERATION_MAX & min should be changed
                 */
                FinebrotFractal.update();

                Application.zoomIn();
            }

            if (iteration == 1) {
                AreaMandelbrot.moveToInitialCoordinates();
                AreaFinebrot.moveToInitialCoordinates();

                Mandelbrot.maskFullUpdate();
                Application.repaintMandelbrotWindow();
            }
        } while (REPEAT);
    }
}
