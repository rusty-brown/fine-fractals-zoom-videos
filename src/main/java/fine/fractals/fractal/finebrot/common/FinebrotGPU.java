package fine.fractals.fractal.finebrot.common;

import fine.fractals.fractal.finebrot.PixelsFinebrotImpl;
import fine.fractals.fractal.mandelbrot.MandelbrotGpuImpl;
import fine.fractals.perfect.coloring.PerfectColorDistributionImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class FinebrotGPU extends FinebrotAbstractImpl {

    private static final Logger log = LogManager.getLogger(FinebrotGPU.class);

    public FinebrotGPU() {
        log.debug("constructor");
        PixelsFinebrot = new PixelsFinebrotImpl();
        PerfectColorDistribution = new PerfectColorDistributionImpl();
        PathsFinebrot = new PathsFinebrotImpl();

        Mandelbrot = new MandelbrotGpuImpl();
    }
}
