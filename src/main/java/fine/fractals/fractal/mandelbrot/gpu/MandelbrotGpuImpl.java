package fine.fractals.fractal.mandelbrot.gpu;

import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.fractal.mandelbrot.MandelbrotCommonImpl;
import fine.fractals.gpgpu.GPU;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;

import static fine.fractals.fractal.mandelbrot.PixelsMandelbrotImpl.PixelsMandelbrot;

public class MandelbrotGpuImpl extends MandelbrotCommonImpl {

    private static final Logger log = LogManager.getLogger(MandelbrotGpuImpl.class);

    public MandelbrotGpuImpl() {
        log.debug("constructor");
    }

    /*
     * Calculate Domain Values
     */
    @Override
    public void calculate() {
        log.debug("calculate()");

        final ArrayList<ArrayList<MandelbrotElement>> domainFullChunkedAndWrapped = PixelsMandelbrot.fullDomainAsWrappedParts();

        Collections.shuffle(domainFullChunkedAndWrapped);

        final ArrayList<MandelbrotElement> first = domainFullChunkedAndWrapped.get(0);
        for (int i = 1; i < domainFullChunkedAndWrapped.size(); i++) {
            first.addAll(domainFullChunkedAndWrapped.get(i));
        }

        GPU.rebuild().calculate(first);
    }
}
