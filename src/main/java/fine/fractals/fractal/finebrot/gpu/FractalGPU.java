package fine.fractals.fractal.finebrot.gpu;

import fine.fractals.fractal.finebrot.PixelsFinebrotImpl;
import fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl;
import fine.fractals.fractal.finebrot.common.PathsFinebrotImpl;
import fine.fractals.fractal.mandelbrot.gpu.MandelbrotGpuImpl;
import fine.fractals.perfect.coloring.PerfectColorDistributionImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class FractalGPU extends FinebrotAbstractImpl {

    private static final Logger log = LogManager.getLogger(FractalGPU.class);

    public FractalGPU() {
        log.debug("constructor");
        PixelsFinebrot = new PixelsFinebrotImpl();
        PerfectColorDistribution = new PerfectColorDistributionImpl();
        PathsFinebrot = new PathsFinebrotImpl();

        Mandelbrot = new MandelbrotGpuImpl();
    }
}
