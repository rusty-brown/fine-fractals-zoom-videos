package fine.fractals.fractal.finebrot.common;

import fine.fractals.fractal.finebrot.PixelsFinebrotImpl;
import fine.fractals.fractal.mandelbrot.MandelbrotImpl;
import fine.fractals.perfect.coloring.PerfectColorDistributionImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class FinebrotCommonImpl extends FinebrotAbstractImpl {

    private static final Logger log = LogManager.getLogger(FinebrotCommonImpl.class);

    public static PixelsFinebrotImpl PixelsFinebrot;

    public FinebrotCommonImpl() {
        log.debug("constructor");
        PixelsFinebrot = new PixelsFinebrotImpl();
        PerfectColorDistribution = new PerfectColorDistributionImpl();
        PathsFinebrot = new PathsFinebrotImpl();
        Mandelbrot = new MandelbrotImpl();
    }
}
