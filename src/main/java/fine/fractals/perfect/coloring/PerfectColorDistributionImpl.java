package fine.fractals.perfect.coloring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.PaletteImpl.Palette;
import static fine.fractals.context.TargetImpl.Target;
import static fine.fractals.context.finebrot.FinebrotImpl.Finebrot;
import static fine.fractals.images.FractalImage.FinebrotImage;
import static java.lang.Double.compare;
import static java.lang.Integer.compare;
import static java.lang.Math.abs;

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
    static final List<FinebrotPixel> pixels = new ArrayList<>();

    public static final PerfectColorDistributionImpl PerfectColorDistribution;

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

        int zeroValueElements = 0;

        /* identify zero and low-value elements as zero or noise */
        final int threshold = 4;

        /* read screen values */
        for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
            for (int x = 0; x < RESOLUTION_WIDTH; x++) {
                int v = Finebrot.valueAt(x, y);
                if (v <= threshold) {
                    zeroValueElements++;
                }
                pixels.add(new FinebrotPixel(v, x, y));
            }
        }

        /* order data from smallest to highest screen value */
        final double px = Target.re();
        pixels.sort((a, b) -> {
            int compare = compare(a.pixelValue(), b.pixelValue());
            if (compare == 0) {
                return compare(abs(a.px() - px), abs(b.px() - px));
            }
            return compare;
        });

        final int allPixelsTotal = RESOLUTION_WIDTH * RESOLUTION_HEIGHT;
        final int allPixelsNonZero = allPixelsTotal - zeroValueElements;
        final int paletteColorCount = Palette.colorResolution();
        final int singleColorUse = ((int) ((double) allPixelsNonZero / (double) paletteColorCount));
        final int left = allPixelsNonZero - (paletteColorCount * singleColorUse);

        log.info("----------------------------------");
        log.info("All pixels to paint:        " + allPixelsTotal);
        log.info("--------------------------> " + (zeroValueElements + left + (singleColorUse * paletteColorCount)));
        log.info("Zero value pixels to paint: " + zeroValueElements);
        log.info("Non zero pixels to paint:   " + allPixelsNonZero);
        log.info("Spectrum, available colors: " + paletteColorCount);
        log.info("Pixels per each color:      " + singleColorUse);
        log.info("left:                       " + left);
        log.info("----------------------------------");

        /* pixel index */
        int pi;
        FinebrotPixel sp;

        /* paint mismatched pixel amount with the least value colour */
        for (pi = 0; pi < left + zeroValueElements; pi++) {
            sp = pixels.get(pi);
            FinebrotImage.setRGB(sp.px(), sp.py(), Palette.getSpectrumValue(0).getRGB());
        }

        /* color all remaining pixels, these are order by value */
        for (int paletteColourIndex = 0; paletteColourIndex < paletteColorCount; paletteColourIndex++) {
            for (int ci = 0; ci < singleColorUse; ci++) {
                /* color all these pixels with same color */
                sp = pixels.get(pi++);
                if (sp.pixelValue() <= threshold) {
                    /* color zero-value elements and low-value-noise with the darkest color */
                    FinebrotImage.setRGB(sp.px(), sp.py(), Palette.getSpectrumValue(0).getRGB());
                } else {
                    /* perfect-color all significant pixels */
                    FinebrotImage.setRGB(sp.px(), sp.py(), Palette.getSpectrumValue(paletteColourIndex).getRGB());
                }
            }
        }
        log.info("painted:                   " + pi);

        /*
         * Behold, the coloring is perfect!
         */

        log.info("clear pixels");
        pixels.clear();
    }
}
