package fine.fractals.perfect.coloring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.PaletteImpl.Palette;
import static fine.fractals.context.finebrot.FinebrotImpl.Finebrot;
import static fine.fractals.images.FractalImage.FinebrotImage;

/**
 * Method used for perfect coloring is
 * - Gather all screen pixels and order them by (density map) value
 * - Expand full coloring spectrum to amount of pixels to be painted
 * -> Color all pixels ordered by value
 */
public class PerfectColorDistributionImpl {

    private final static Logger log = LogManager.getLogger(PerfectColorDistributionImpl.class);

    /**
     * Ordered values of screen pixels
     * Ready to be colored
     */
    static final List<ScreenPixel> pixels = new ArrayList<>();

    public static PerfectColorDistributionImpl PerfectColorDistribution;

    private PerfectColorDistributionImpl() {
    }

    static {
        log.info("init");
        PerfectColorDistribution = new PerfectColorDistributionImpl();
    }

    /**
     * Color palette contains fewer colors than all pixels on the screen
     * Expand colour palette so that all pixels may be colored perfectly
     */
    public void perfectlyColorScreenValues() {

        /* read screen values */
        for (int x = 0; x < RESOLUTION_HEIGHT; x++) {
            for (int t = 0; t < RESOLUTION_WIDTH; t++) {
                pixels.add(new ScreenPixel(Finebrot.valueAt(t, x), t, x));
            }
        }

        /* order data from smallest to highest screen value */
        pixels.sort((a, b) -> Integer.compare(a.pixelValue(), b.pixelValue()));

        final int allPixels = RESOLUTION_WIDTH * RESOLUTION_HEIGHT;
        final int paletteColorCount = Palette.colorResolution();
        final int singleColorUse = ((int) ((double) allPixels / (double) paletteColorCount));
        final int left = allPixels - (paletteColorCount * singleColorUse);

        log.info("All pixels to paint:        " + allPixels);
        log.info("Spectrum, available colors: " + paletteColorCount);
        log.info("Pixels per each color:      " + singleColorUse);
        log.info("left: " + left);

        /* pixel index */
        int pi;
        ScreenPixel sp;

        /* paint mismatched pixel amount with the least value colour */
        for (pi = 0; pi < left; pi++) {
            sp = pixels.get(pi);
            FinebrotImage.setRGB(sp.px(), sp.py(), Palette.getSpectrumValue(0).getRGB());
        }

        /* color all remaining pixels, these are order by value */
        for (int paletteColourIndex = 0; paletteColourIndex < paletteColorCount; paletteColourIndex++) {
            for (int ci = 0; ci < singleColorUse; ci++) {
                /* color all these pixels with same color */
                sp = pixels.get(pi++);
                /* write screen values to image */
                FinebrotImage.setRGB(sp.px(), sp.py(), Palette.getSpectrumValue(paletteColourIndex).getRGB());
            }
        }
        /* behold the coloring is perfect */
    }

    public void clear() {
        pixels.clear();
    }
}
