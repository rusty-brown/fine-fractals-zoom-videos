package fine.fractals.perfect.coloring;

import fine.fractals.data.finebrot.FinebrotPixel;
import fine.fractals.perfect.coloring.common.PerfectColorDistributionAbstract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static fine.fractals.color.common.PaletteEulerImpl.PaletteEuler3;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_WIDTH;
import static fine.fractals.fractal.finebrot.euler.FractalEuler.PixelsEulerFinebrot;
import static fine.fractals.fractal.finebrot.euler.PixelsEulerFinebrotImpl.Spectra.blue;
import static fine.fractals.fractal.finebrot.euler.PixelsEulerFinebrotImpl.Spectra.green;
import static fine.fractals.fractal.finebrot.euler.PixelsEulerFinebrotImpl.Spectra.red;
import static fine.fractals.images.FractalImages.FinebrotImage;

public class EulerPerfectColorDistributionImpl extends PerfectColorDistributionAbstract {

    private final static Logger log = LogManager.getLogger(EulerPerfectColorDistributionImpl.class);

    /**
     * Finebrot pixels, order by value
     */
    static final List<FinebrotPixel> pixelsRed = new ArrayList<>();
    static final List<FinebrotPixel> pixelsGreen = new ArrayList<>();
    static final List<FinebrotPixel> pixelsBlue = new ArrayList<>();

    public EulerPerfectColorDistributionImpl() {
        log.debug("constructor");
    }

    public void perfectlyColorFinebrotValues() {
        log.debug("perfectlyColorScreenValues()");

        int zeroValueElementsRed = 0;
        int zeroValueElementsGreen = 0;
        int zeroValueElementsBlue = 0;

        /* identify zero and low-value elements as zero or noise */
        final int threshold = 1;

        /* read screen values */
        for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
            for (int x = 0; x < RESOLUTION_WIDTH; x++) {
                int r = PixelsEulerFinebrot.valueAt(x, y, red);
                int g = PixelsEulerFinebrot.valueAt(x, y, green);
                int b = PixelsEulerFinebrot.valueAt(x, y, blue);
                if (r <= threshold) {
                    zeroValueElementsRed++;
                }
                if (g <= threshold) {
                    zeroValueElementsGreen++;
                }
                if (b <= threshold) {
                    zeroValueElementsBlue++;
                }
                pixelsRed.add(new FinebrotPixel(r, x, y));
                pixelsGreen.add(new FinebrotPixel(g, x, y));
                pixelsBlue.add(new FinebrotPixel(b, x, y));
            }
        }

        /*
         *  order pixels from the smallest to the highest value
         */
        pixelsRed.sort(comparator);
        pixelsGreen.sort(comparator);
        pixelsBlue.sort(comparator);

        final int allPixelsTotal = RESOLUTION_WIDTH * RESOLUTION_HEIGHT;
        final int allPixelsNonZeroRed = allPixelsTotal - zeroValueElementsRed;
        final int allPixelsNonZeroGreen = allPixelsTotal - zeroValueElementsGreen;
        final int allPixelsNonZeroBlue = allPixelsTotal - zeroValueElementsBlue;
        final int paletteColorCount = PaletteEuler3.colorResolution(); // same
        final int singleColorUseRed = ((int) ((double) allPixelsNonZeroRed / (double) paletteColorCount));
        final int singleColorUseGreen = ((int) ((double) allPixelsNonZeroGreen / (double) paletteColorCount));
        final int singleColorUseBlue = ((int) ((double) allPixelsNonZeroBlue / (double) paletteColorCount));
        final int leftRed = allPixelsNonZeroRed - (paletteColorCount * singleColorUseRed);
        final int leftGreen = allPixelsNonZeroGreen - (paletteColorCount * singleColorUseGreen);
        final int leftBlue = allPixelsNonZeroBlue - (paletteColorCount * singleColorUseBlue);

        log.debug("------------------------------------");
        log.debug("All pixels to paint:        " + allPixelsTotal);
        log.debug("--------------------------->" + (zeroValueElementsRed + leftRed + (singleColorUseRed * paletteColorCount)));
        log.debug("--------------------------->" + (zeroValueElementsGreen + leftGreen + (singleColorUseGreen * paletteColorCount)));
        log.debug("--------------------------->" + (zeroValueElementsBlue + leftBlue + (singleColorUseBlue * paletteColorCount)));
        log.debug("Zero value pixels to paint: " + zeroValueElementsRed);
        log.debug("Zero value pixels to paint: " + zeroValueElementsGreen);
        log.debug("Zero value pixels to paint: " + zeroValueElementsBlue);
        log.debug("Non zero pixels to paint:   " + allPixelsNonZeroRed);
        log.debug("Non zero pixels to paint:   " + allPixelsNonZeroGreen);
        log.debug("Non zero pixels to paint:   " + allPixelsNonZeroBlue);
        log.debug("Spectrum, available colors: " + paletteColorCount);
        log.debug("Pixels per each color:      " + singleColorUseRed);
        log.debug("Pixels per each color:      " + singleColorUseGreen);
        log.debug("Pixels per each color:      " + singleColorUseBlue);
        log.debug("left:                       " + leftRed);
        log.debug("left:                       " + leftGreen);
        log.debug("left:                       " + leftBlue);
        log.debug("------------------------------------");

        /* pixel index */
        int piRed;
        FinebrotPixel sp;
        /* paint mismatched pixel amount with the least value colour */
        for (piRed = 0; piRed < leftRed + zeroValueElementsRed; piRed++) {
            sp = pixelsRed.get(piRed);
            PixelsEulerFinebrot.set(sp.px(), sp.py(), red, 0);
        }
        /* color all remaining pixels, these are order by value */
        for (int paletteColourIndex = 0; paletteColourIndex < paletteColorCount; paletteColourIndex++) {
            for (int ci = 0; ci < singleColorUseRed; ci++) {
                /* color all these pixels with same color */
                sp = pixelsRed.get(piRed++);
                if (sp.pixelValue() <= threshold) {
                    PixelsEulerFinebrot.set(sp.px(), sp.py(), red, 0);
                } else {
                    /* perfect-color all significant pixels */
                    PixelsEulerFinebrot.set(sp.px(), sp.py(), red, PaletteEuler3.getSpectrumValueRed(paletteColourIndex).getRed());
                }
            }
        }

        int piGreen;
        for (piGreen = 0; piGreen < leftGreen + zeroValueElementsGreen; piGreen++) {
            sp = pixelsGreen.get(piGreen);
            PixelsEulerFinebrot.set(sp.px(), sp.py(), green, 0);
        }
        /* color all remaining pixels, these are order by value */
        for (int paletteColourIndex = 0; paletteColourIndex < paletteColorCount; paletteColourIndex++) {
            for (int ci = 0; ci < singleColorUseGreen; ci++) {
                /* color all these pixels with same color */
                sp = pixelsGreen.get(piGreen++);
                if (sp.pixelValue() <= threshold) {
                    /* color zero-value elements and low-value-noise with the darkest color */
                    PixelsEulerFinebrot.set(sp.px(), sp.py(), green, 0);
                } else {
                    /* perfect-color all significant pixels */
                    PixelsEulerFinebrot.set(sp.px(), sp.py(), green, PaletteEuler3.getSpectrumValueGreen(paletteColourIndex).getGreen());
                }
            }
        }

        int piBlue;
        for (piBlue = 0; piBlue < leftBlue + zeroValueElementsBlue; piBlue++) {
            sp = pixelsBlue.get(piBlue);
            PixelsEulerFinebrot.set(sp.px(), sp.py(), blue, 0);
        }
        /* color all remaining pixels, these are order by value */
        for (int paletteColourIndex = 0; paletteColourIndex < paletteColorCount; paletteColourIndex++) {
            for (int ci = 0; ci < singleColorUseBlue; ci++) {
                /* color all these pixels with same color */
                sp = pixelsBlue.get(piBlue++);
                if (sp.pixelValue() <= threshold) {
                    /* color zero-value elements and low-value-noise with the darkest color */
                    PixelsEulerFinebrot.set(sp.px(), sp.py(), blue, 0);
                } else {
                    /* perfect-color all significant pixels */
                    PixelsEulerFinebrot.set(sp.px(), sp.py(), blue, PaletteEuler3.getSpectrumValueBlue(paletteColourIndex).getBlue());
                }
            }
        }

        log.debug("painted:                   " + piRed);
        log.debug("painted:                   " + piGreen);
        log.debug("painted:                   " + piBlue);

        /*
         * read 3 screen colors
         * write image colors
         */
        for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
            for (int x = 0; x < RESOLUTION_WIDTH; x++) {
                int r = PixelsEulerFinebrot.valueAt(x, y, red);
                int g = PixelsEulerFinebrot.valueAt(x, y, green);
                int b = PixelsEulerFinebrot.valueAt(x, y, blue);
                FinebrotImage.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }

        /*
         * Behold, the coloring is perfect
         */

        log.debug("clear pixels");
        pixelsRed.clear();
        pixelsGreen.clear();
        pixelsBlue.clear();
        PixelsEulerFinebrot.clear();
    }
}
