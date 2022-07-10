package fine.fractals.test.fractal.mandelbrot;

import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.fractal.mandelbrot.PixelsMandelbrotImpl;
import org.junit.Test;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.RESOLUTION_WIDTH;
import static org.junit.Assert.assertEquals;

public class TestPixelsMandelbrotImpl {

    @Test
    public void testMandelbrotDomainChunking() {
        RESOLUTION_WIDTH = 680;
        RESOLUTION_HEIGHT = 640;
        final PixelsMandelbrotImpl PixelsMandelbrot = new PixelsMandelbrotImpl();
        PixelsMandelbrot.initializeDomainElements();
        ArrayList<ArrayList<MandelbrotElement>> arrayLists = PixelsMandelbrot.fetchDomainWrappedParts();

        int total = 0;
        for (ArrayList<MandelbrotElement> list : arrayLists) {
            total += list.size();
        }

        assertEquals(total, RESOLUTION_WIDTH * RESOLUTION_HEIGHT);
    }
}
