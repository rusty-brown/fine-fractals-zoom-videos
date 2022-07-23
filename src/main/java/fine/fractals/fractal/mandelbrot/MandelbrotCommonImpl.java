package fine.fractals.fractal.mandelbrot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.fractal.mandelbrot.PixelsMandelbrotImpl.PixelsMandelbrot;

public abstract class MandelbrotCommonImpl {

    @SuppressWarnings(value = "unused")
    private static final Logger log = LogManager.getLogger(MandelbrotCommonImpl.class);

    /*
     * Calculate Domain Values
     */
    public abstract void calculate();

    public void recalculatePixelsPositionsForThisZoom() {
        PixelsMandelbrot.recalculatePixelsPositionsForThisZoom();
    }

    public void initializeDomainElements() {
        PixelsMandelbrot.initializeDomainElements();
    }

    public void maskFullUpdate() {
        PixelsMandelbrot.maskFullUpdate();
    }
}
