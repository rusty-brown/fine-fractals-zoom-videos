package fine.fractals.perfect.coloring;

import fine.fractals.data.finebrot.FinebrotPixel;
import fine.fractals.perfect.coloring.common.PerfectColorDistributionAbstract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static fine.fractals.context.ApplicationImpl.RESOLUTION_HEIGHT;
import static fine.fractals.context.ApplicationImpl.RESOLUTION_WIDTH;
import static fine.fractals.context.TargetImpl.Target;
import static fine.fractals.fractal.finebrot.euler.FractalEuler.PixelsEulerFinebrot;
import static fine.fractals.images.FractalImage.FinebrotImage;
import static fine.fractals.palette.PaletteEulerImpl.PaletteEuler3;
import static java.lang.Double.compare;
import static java.lang.Integer.compare;
import static java.lang.Math.abs;

public class EulerPerfectColorDistributionImpl extends PerfectColorDistributionAbstract {

    private final static Logger log = LogManager.getLogger(EulerPerfectColorDistributionImpl.class);

    /**
     * Finebrot pixels, order by value
     */
    static final List<FinebrotPixel> pixelsR = new ArrayList<>();
    static final List<FinebrotPixel> pixelsG = new ArrayList<>();
    static final List<FinebrotPixel> pixelsB = new ArrayList<>();

    public EulerPerfectColorDistributionImpl() {
    }

    public void perfectlyColorFinebrotValues() {
        log.info("perfectlyColorScreenValues()");

        int zeroValueElementsR = 0;
        int zeroValueElementsG = 0;
        int zeroValueElementsB = 0;

        /* identify zero and low-value elements as zero or noise */
        final int threshold = 4;

        /* read screen values */
        for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
            for (int x = 0; x < RESOLUTION_WIDTH; x++) {
                int r = PixelsEulerFinebrot.valueAt(x, y, 0);
                int g = PixelsEulerFinebrot.valueAt(x, y, 1);
                int b = PixelsEulerFinebrot.valueAt(x, y, 2);
                if (r <= threshold) {
                    zeroValueElementsR++;
                }
                if (g <= threshold) {
                    zeroValueElementsG++;
                }
                if (b <= threshold) {
                    zeroValueElementsB++;
                }
                pixelsR.add(new FinebrotPixel(r, x, y));
                pixelsG.add(new FinebrotPixel(r, x, y));
                pixelsB.add(new FinebrotPixel(b, x, y));
            }
        }

        /*
         *  order pixels from the smallest to the highest value
         */

        pixelsR.sort(comparator);
        pixelsG.sort(comparator);
        pixelsB.sort(comparator);

        final int allPixelsTotal = RESOLUTION_WIDTH * RESOLUTION_HEIGHT;
        final int allPixelsNonZeroR = allPixelsTotal - zeroValueElementsR;
        final int allPixelsNonZeroG = allPixelsTotal - zeroValueElementsG;
        final int allPixelsNonZeroB = allPixelsTotal - zeroValueElementsB;
        final int paletteColorCount = PaletteEuler3.colorResolution(); // same
        final int singleColorUseR = ((int) ((double) allPixelsNonZeroR / (double) paletteColorCount));
        final int singleColorUseG = ((int) ((double) allPixelsNonZeroG / (double) paletteColorCount));
        final int singleColorUseB = ((int) ((double) allPixelsNonZeroB / (double) paletteColorCount));
        final int leftR = allPixelsNonZeroR - (paletteColorCount * singleColorUseR);
        final int leftG = allPixelsNonZeroG - (paletteColorCount * singleColorUseG);
        final int leftB = allPixelsNonZeroB - (paletteColorCount * singleColorUseB);

        log.debug("------------------------------------");
        log.debug("All pixels to paint:        " + allPixelsTotal);
        log.debug("--------------------------->" + (zeroValueElementsR + leftR + (singleColorUseR * paletteColorCount)));
        log.debug("--------------------------->" + (zeroValueElementsG + leftG + (singleColorUseG * paletteColorCount)));
        log.debug("--------------------------->" + (zeroValueElementsB + leftB + (singleColorUseB * paletteColorCount)));
        log.debug("Zero value pixels to paint: " + zeroValueElementsR);
        log.debug("Zero value pixels to paint: " + zeroValueElementsG);
        log.debug("Zero value pixels to paint: " + zeroValueElementsB);
        log.debug("Non zero pixels to paint:   " + allPixelsNonZeroR);
        log.debug("Non zero pixels to paint:   " + allPixelsNonZeroG);
        log.debug("Non zero pixels to paint:   " + allPixelsNonZeroB);
        log.debug("Spectrum, available colors: " + paletteColorCount);
        log.debug("Pixels per each color:      " + singleColorUseR);
        log.debug("Pixels per each color:      " + singleColorUseG);
        log.debug("Pixels per each color:      " + singleColorUseB);
        log.debug("left:                       " + leftR);
        log.debug("left:                       " + leftG);
        log.debug("left:                       " + leftB);
        log.debug("------------------------------------");

        /* pixel index */
        int piR;
        FinebrotPixel sp;


        /* paint mismatched pixel amount with the least value colour */
        for (piR = 0; piR < leftR + zeroValueElementsR; piR++) {
            sp = pixelsR.get(piR);
            FinebrotImage.setRGB(sp.px(), sp.py(), PaletteEuler3.getSpectrumValueR(0).getRGB());
        }

        /* color all remaining pixels, these are order by value */
        for (int paletteColourIndex = 0; paletteColourIndex < paletteColorCount; paletteColourIndex++) {
            for (int ci = 0; ci < singleColorUseR; ci++) {
                /* color all these pixels with same color */
                sp = pixelsR.get(piR++);
                if (sp.pixelValue() <= threshold) {
                    /* color zero-value elements and low-value-noise with the darkest color */
                    FinebrotImage.setRGB(sp.px(), sp.py(), PaletteEuler3.getSpectrumValueR(0).getRGB());
                } else {
                    /* perfect-color all significant pixels */
                    FinebrotImage.setRGB(sp.px(), sp.py(), PaletteEuler3.getSpectrumValueR(paletteColourIndex).getRGB());
                }
            }
        }

        int piG;

        for (piG = 0; piG < leftG + zeroValueElementsG; piG++) {
            sp = pixelsG.get(piG);
            FinebrotImage.setRGB(sp.px(), sp.py(), PaletteEuler3.getSpectrumValueG(0).getRGB());
        }

        /* color all remaining pixels, these are order by value */
        for (int paletteColourIndex = 0; paletteColourIndex < paletteColorCount; paletteColourIndex++) {
            for (int ci = 0; ci < singleColorUseG; ci++) {
                /* color all these pixels with same color */
                sp = pixelsG.get(piG++);
                if (sp.pixelValue() <= threshold) {
                    /* color zero-value elements and low-value-noise with the darkest color */
                    FinebrotImage.setRGB(sp.px(), sp.py(), PaletteEuler3.getSpectrumValueG(0).getRGB());
                } else {
                    /* perfect-color all significant pixels */
                    FinebrotImage.setRGB(sp.px(), sp.py(), PaletteEuler3.getSpectrumValueG(paletteColourIndex).getRGB());
                }
            }
        }

        int piB;

        for (piB = 0; piB < leftB + zeroValueElementsB; piB++) {
            sp = pixelsB.get(piB);
            FinebrotImage.setRGB(sp.px(), sp.py(), PaletteEuler3.getSpectrumValueB(0).getRGB());
        }

        /* color all remaining pixels, these are order by value */
        for (int paletteColourIndex = 0; paletteColourIndex < paletteColorCount; paletteColourIndex++) {
            for (int ci = 0; ci < singleColorUseB; ci++) {
                /* color all these pixels with same color */
                sp = pixelsB.get(piB++);
                if (sp.pixelValue() <= threshold) {
                    /* color zero-value elements and low-value-noise with the darkest color */
                    FinebrotImage.setRGB(sp.px(), sp.py(), PaletteEuler3.getSpectrumValueB(0).getRGB());
                } else {
                    /* perfect-color all significant pixels */
                    FinebrotImage.setRGB(sp.px(), sp.py(), PaletteEuler3.getSpectrumValueB(paletteColourIndex).getRGB());
                }
            }
        }

        log.debug("painted:                   " + piR);
        log.debug("painted:                   " + piG);
        log.debug("painted:                   " + piB);

        /*
         * Behold, the coloring is perfect!
         */

        log.debug("clear pixels");
        pixelsR.clear();
        pixelsG.clear();
        pixelsB.clear();
    }
}
