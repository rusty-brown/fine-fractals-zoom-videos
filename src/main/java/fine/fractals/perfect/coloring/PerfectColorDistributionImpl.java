package fine.fractals.perfect.coloring;

import fine.fractals.data.finebrot.FinebrotPixel;
import fine.fractals.perfect.coloring.common.PerfectColorDistributionAbstract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static fine.fractals.color.common.PaletteImpl.Palette;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_WIDTH;
import static fine.fractals.fractal.finebrot.finite.FractalFinite.PixelsFinebrot;
import static fine.fractals.images.FractalImages.FinebrotImage;
import static fine.fractals.machine.ApplicationImpl.coloringThreshold;

public class PerfectColorDistributionImpl extends PerfectColorDistributionAbstract {

    private final static Logger log = LogManager.getLogger(PerfectColorDistributionImpl.class);

    /**
     * Finebrot pixels, order by value
     */
    static final List<FinebrotPixel> pixels = new ArrayList<>();

    public PerfectColorDistributionImpl() {
        log.debug("constructor");
    }

    public void perfectlyColorFinebrotValues() {
        log.debug("perfectlyColorScreenValues()");

        int zeroValueElements = 0;

        /* read screen values */
        for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
            for (int x = 0; x < RESOLUTION_WIDTH; x++) {
                int v = PixelsFinebrot.valueAt(x, y);
                if (v <= coloringThreshold) {
                    zeroValueElements++;
                }
                pixels.add(new FinebrotPixel(v, x, y));
            }
        }

        /*
         *  order pixels from the smallest to the highest value
         */
        pixels.sort(comparator);

        final int allPixelsTotal = RESOLUTION_WIDTH * RESOLUTION_HEIGHT;
        final int allPixelsNonZero = allPixelsTotal - zeroValueElements;
        final int paletteColorCount = Palette.colorResolution();
        final int singleColorUse = ((int) ((double) allPixelsNonZero / (double) paletteColorCount));
        final int left = allPixelsNonZero - (paletteColorCount * singleColorUse);

        log.debug("------------------------------------");
        log.debug("All pixels to paint:        " + allPixelsTotal);
        log.debug("--------------------------->" + (zeroValueElements + left + (singleColorUse * paletteColorCount)));
        log.debug("Zero value pixels to paint: " + zeroValueElements);
        log.debug("Non zero pixels to paint:   " + allPixelsNonZero);
        log.debug("Spectrum, available colors: " + paletteColorCount);
        log.debug("Pixels per each color:      " + singleColorUse);
        log.debug("left:                       " + left);
        log.debug("------------------------------------");

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
                if (sp.pixelValue() <= coloringThreshold) {
                    /* color zero-value elements and low-value-noise with the darkest color */
                    FinebrotImage.setRGB(sp.px(), sp.py(), Palette.getSpectrumValue(0).getRGB());
                } else {
                    /* perfect-color all significant pixels */
                    FinebrotImage.setRGB(sp.px(), sp.py(), Palette.getSpectrumValue(paletteColourIndex).getRGB());
                }
            }
        }
        log.debug("painted:                   " + pi);

        /*
         * Behold, the coloring is perfect
         */

        log.debug("clear pixels");
        pixels.clear();
        PixelsFinebrot.clear();
    }
}
