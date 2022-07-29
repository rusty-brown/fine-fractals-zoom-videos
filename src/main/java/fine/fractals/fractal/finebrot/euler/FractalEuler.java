package fine.fractals.fractal.finebrot.euler;

import fine.fractals.data.annotation.ThreadSafe;
import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.data.mem.MemEuler;
import fine.fractals.fractal.finebrot.common.FinebrotCpu;
import fine.fractals.fractal.mandelbrot.MandelbrotImpl;
import fine.fractals.perfect.coloring.EulerPerfectColorDistributionImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.euler.PixelsEulerFinebrotImpl.Spectra.blue;
import static fine.fractals.fractal.finebrot.euler.PixelsEulerFinebrotImpl.Spectra.green;
import static fine.fractals.fractal.finebrot.euler.PixelsEulerFinebrotImpl.Spectra.red;
import static fine.fractals.math.MathematicianImpl.Mathematician;

/**
 * Fractal Euler type uses three color spectra for better mathematical analysis and better coloring results.
 * Possible use as:
 * - prime path lengths     -> red spectrum
 * - Fibonacci path lengths -> green spectrum
 * - other path lengths     -> blue spectrum
 */
public abstract class FractalEuler extends FinebrotCpu {

    private static final Logger log = LogManager.getLogger(FractalEuler.class);

    public static PixelsEulerFinebrotImpl PixelsEulerFinebrot;

    public FractalEuler() {
        log.debug("constructor");
        PixelsEulerFinebrot = new PixelsEulerFinebrotImpl();
        PerfectColorDistribution = new EulerPerfectColorDistributionImpl();
        PathsFinebrot = new PathsEulerFinebrotImpl();
        Mandelbrot = new MandelbrotImpl(this);
    }

    public static void colorsFor(MemEuler m, int elementIndex, int pathLength) {
        if (Mathematician.isPrime(elementIndex)) {
            m.spectra = red;
            return;
        }
        if (Mathematician.isPrime(pathLength)) {
            m.spectra = green;
            return;
        }
        m.spectra = blue;
    }

    public abstract void math(MemEuler m, double re, double im);

    @Override
    @ThreadSafe
    public ArrayList<double[]> calculatePath(MandelbrotElement el) {
        int iterator = 0;
        int length = 0;
        final MemEuler m = new MemEuler(el.originRe, el.originIm);
        while (m.quadrance() < CALCULATION_BOUNDARY && iterator < ITERATION_MAX) {

            /*
             * Investigate if this is a good calculation path
             * Don't create path data yet. Too many origin's don't produce good data
             * Most long expensive calculations end up inside Mandelbrot set
             */
            math(m, el.originRe, el.originIm);
            if (AreaFinebrot.contains(m)) {
                length++;
            }
            iterator++;
        }
        el.setFinishedState(iterator, length);

        /* Verify divergent path length */
        if (length > ITERATION_min && iterator < ITERATION_MAX) {

            /*
             * This origin produced good data, record calculation path
             */
            m.re = el.originRe;
            m.im = el.originIm;
            final ArrayList<double[]> path = new ArrayList<>(length);
            for (int i = 0; i < iterator; i++) {
                math(m, el.originRe, el.originIm);
                if (AreaFinebrot.contains(m)) {
                    path.add(new double[]{m.re, m.im});
                }
            }
            return path;
        } else {
            return null;
        }
    }
}
