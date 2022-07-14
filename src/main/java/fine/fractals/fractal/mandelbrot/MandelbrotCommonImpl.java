package fine.fractals.fractal.mandelbrot;

import fine.fractals.data.mandelbrot.MandelbrotElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.fractal.mandelbrot.PixelsMandelbrotImpl.PixelsMandelbrot;

public abstract class MandelbrotCommonImpl {

    private static final Logger log = LogManager.getLogger(MandelbrotCommonImpl.class);

    public static MandelbrotCommonImpl Mandelbrot;

    /*
     * Calculate Domain Values
     */
    public abstract void calculate();

    /* Used for OneTarget */
    public MandelbrotElement getElementAt(int x, int y) {
        try {
            return PixelsMandelbrot.elementsStaticMandelbrot[x][y];
        } catch (Exception e) {
            log.fatal("getElementAt()", e);
            return null;
        }
    }

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
