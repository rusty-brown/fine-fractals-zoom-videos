package fine.fractals.machine.concurent;

import fine.fractals.data.annotation.ThreadSafe;
import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.fractal.finebrot.common.FinebrotCpu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.Mandelbrot;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.PathsFinebrot;
import static fine.fractals.machine.ApplicationImpl.Application;
import static java.lang.System.currentTimeMillis;

public class CalculationPathThread implements Runnable {

    @SuppressWarnings(value = "unused")
    private static final Logger log = LogManager.getLogger(CalculationPathThread.class);

    private static long lastMandelbrotRefresh = 0;

    private final ArrayList<MandelbrotElement> mandelbrotElementsPart;
    private final FinebrotCpu finebrotFractal;

    public CalculationPathThread(FinebrotCpu finebrotFractal, ArrayList<MandelbrotElement> mandelbrotElementsPart) {
        this.mandelbrotElementsPart = mandelbrotElementsPart;
        this.finebrotFractal = finebrotFractal;
    }

    @ThreadSafe
    public void run() {
        for (MandelbrotElement el : mandelbrotElementsPart) {
            /*
             * Investigate calculation path for each mandelbrot pixel
             */
            final ArrayList<double[]> path = finebrotFractal.calculatePath(el);
            if (path != null) {

                /*
                 * Removed lastIteration, lastVisitedRe, lastVisitedIm
                 * There isn't continuation of unfinished iteration from previous calculation (ITERATION_MAX increased)
                 * The element and its path is going to migrate out of screen soon.
                 */

                PathsFinebrot.addEscapePathLong(path);
            }
        }
        if (lastMandelbrotRefresh + 97 < currentTimeMillis()) {
            /*
             * Handle refresh with calculation progress for all the threads
             */
            lastMandelbrotRefresh = currentTimeMillis();

            Mandelbrot.maskFullUpdate();
            Application.repaintMandelbrotWindow();
        }
    }
}
