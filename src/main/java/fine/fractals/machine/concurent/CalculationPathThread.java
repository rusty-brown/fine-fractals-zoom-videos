package fine.fractals.machine.concurent;

import fine.fractals.data.annotation.ThreadSafe;
import fine.fractals.data.mandelbrot.MandelbrotElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.*;
import static fine.fractals.fractal.mandelbrot.MandelbrotImpl.Mandelbrot;
import static java.lang.System.currentTimeMillis;

public class CalculationPathThread implements Runnable {

    @SuppressWarnings(value = "unused")
    private static final Logger log = LogManager.getLogger(CalculationPathThread.class);

    private static long lastMandelbrotRefresh = 0;

    private final ArrayList<MandelbrotElement> mandelbrotElementsPart;

    public CalculationPathThread(ArrayList<MandelbrotElement> mandelbrotElementsPart) {
        this.mandelbrotElementsPart = mandelbrotElementsPart;
    }

    @ThreadSafe
    public void run() {
        for (MandelbrotElement el : mandelbrotElementsPart) {
            final ArrayList<double[]> path = new ArrayList<>();

            /*
             * Investigate calculation path for each mandelbrot pixel
             */
            final boolean pathTest = FinebrotFractal.calculatePath(el, path);
            if (pathTest) {
                el.setHibernatedFinished();
                /*
                 * Removed lastIteration, lastVisitedRe, lastVisitedIm
                 * There isn't continuation of unfinished iteration from previous calculation (ITERATION_MAX increased)
                 * The element and its path is going to migrate out of screen soon.
                 */
                if (path.size() > ITERATION_MIN) {
                    el.setHibernatedFinishedLong();
                    PathsFinebrot.addEscapePathLong(path);
                }
            }
        }
        if (lastMandelbrotRefresh + 42 < currentTimeMillis()) {
            lastMandelbrotRefresh = currentTimeMillis();
            Mandelbrot.createMaskAndRepaint();
        }
    }
}
