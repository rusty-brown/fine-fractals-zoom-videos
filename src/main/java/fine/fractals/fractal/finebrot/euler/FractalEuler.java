package fine.fractals.fractal.finebrot.euler;

import fine.fractals.data.MandelbrotElement;
import fine.fractals.data.MemEuler;
import fine.fractals.fractal.finebrot.common.FinebrotFractalImpl;
import fine.fractals.perfect.coloring.EulerPerfectColorDistributionImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.math.MathematicianImpl.Mathematician;

public abstract class FractalEuler extends FinebrotFractalImpl {

    private static final Logger log = LogManager.getLogger(FractalEuler.class);

    public static PixelsEulerFinebrotImpl PixelsEulerFinebrot;

    public FractalEuler() {
        log.info("FractalEuler()");
        PixelsEulerFinebrot = new PixelsEulerFinebrotImpl();

        PerfectColorDistribution = new EulerPerfectColorDistributionImpl();
        PathsFinebrot = new PathsEulerFinebrotImpl();
    }

    public abstract void math(MemEuler m, double re, double im);

    @Override
    public boolean calculatePath(MandelbrotElement el, ArrayList<double[]> path) {
        int iterator = 0;

        final MemEuler m = new MemEuler();
        m.re = el.originRe;
        m.im = el.originIm;
        while (m.quadrance() < CALCULATION_BOUNDARY && iterator < ITERATION_MAX) {

            math(m, el.originRe, el.originIm);

            if (AreaFinebrot.contains(m)) {
                path.add(new double[]{m.re, m.im});
            }
            iterator++;
        }

        el.setValues(iterator);
        return iterator < ITERATION_MAX;
    }

    public static void colorsFor(MemEuler m, int elementIndex, int pathLength) {
        if (Mathematician.isPrime(elementIndex)) {
            m.spectra = 0;
            return;
        }
        if (Mathematician.isPrime(pathLength)) {
            m.spectra = 1;
            return;
        }
        m.spectra = 2;
    }
}
