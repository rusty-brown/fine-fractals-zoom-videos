package fine.fractals.fractal.mandelbrot;

import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.data.mem.Mem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.color.utils.ColorUtils.colorForState;
import static fine.fractals.data.mandelbrot.MandelbrotElementFactory.activeNew;
import static fine.fractals.data.mandelbrot.MandelbrotElementFactory.hibernatedDeepBlack;
import static fine.fractals.data.mandelbrot.ResolutionMultiplier.none;
import static fine.fractals.data.mandelbrot.ResolutionMultiplier.square_alter;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.FinebrotFractal;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_MULTIPLIER;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_WIDTH;
import static fine.fractals.fractal.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.images.FractalImages.MandelbrotMaskImage;
import static fine.fractals.machine.ApplicationImpl.Application;
import static fine.fractals.machine.ApplicationImpl.neighbours;
import static org.junit.Assert.assertEquals;

public class PixelsMandelbrotImpl {

    private static final Logger log = LogManager.getLogger(PixelsMandelbrotImpl.class);

    /**
     * Singleton instance
     */
    public static final PixelsMandelbrotImpl PixelsMandelbrot = new PixelsMandelbrotImpl();

    /**
     * Don't do any wrapping the first time
     * Because Mandelbrot elements are not optimized
     */
    private static boolean firstDomainExecution = true;
    final MandelbrotElement[][] elementsStaticMandelbrot = new MandelbrotElement[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
    private final ArrayList<MandelbrotElement> elementsToRemember = new ArrayList<>();
    private final int chunkAmount = 20;
    public int wrapped = 0;
    int notWrapped = 0;
    private boolean odd = true;

    public PixelsMandelbrotImpl() {
        assertEquals("RESOLUTION_WIDTH " + RESOLUTION_WIDTH + " must be divisible by chunkAmount = " + chunkAmount, 0, RESOLUTION_WIDTH % chunkAmount);
        assertEquals("RESOLUTION_HEIGHT " + RESOLUTION_HEIGHT + " must be divisible by chunkAmount = " + chunkAmount, 0, RESOLUTION_HEIGHT % chunkAmount);
    }

    public static boolean checkDomain(int x, int y) {
        return x >= 0 && x < RESOLUTION_WIDTH
                && y >= 0 && y < RESOLUTION_HEIGHT;
    }

    public final void initializeDomainElements() {
        log.debug("constructor");
        for (int x = 0; x < RESOLUTION_WIDTH; x++) {
            for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
                elementsStaticMandelbrot[x][y] = activeNew(AreaMandelbrot.screenToDomainRe(x), AreaMandelbrot.screenToDomainIm(y));
            }
        }
    }

    public ArrayList<ArrayList<MandelbrotElement>> fullDomainAsWrappedParts() {
        log.debug("fetchDomainWrappedParts()");

        int chunkSizeX = RESOLUTION_WIDTH / chunkAmount;
        int chunkSizeY = RESOLUTION_HEIGHT / chunkAmount;

        final ArrayList<ArrayList<MandelbrotElement>> domainFull = new ArrayList<>();

        wrapped = 0;
        notWrapped = 0;

        /* All the pixel (domain) will be split to multiple chunks */
        for (int x = 0; x < chunkAmount; x++) {
            for (int y = 0; y < chunkAmount; y++) {

                final ArrayList<MandelbrotElement> chunkOfElements = makeChunk(
                        x * chunkSizeX, (x + 1) * chunkSizeX,
                        y * chunkSizeY, (y + 1) * chunkSizeY
                );

                if (!chunkOfElements.isEmpty()) {
                    domainFull.add(chunkOfElements);
                }
            }
        }

        firstDomainExecution = false;

        /* Switch wrapping the next time */
        odd = !odd;

        log.debug("wrapped " + wrapped);
        log.debug("notWrapped " + notWrapped);
        log.debug("domain chunks " + domainFull.size());

        return domainFull;
    }

    /**
     * Makes small square domain part
     */
    private ArrayList<MandelbrotElement> makeChunk(int xFrom, int xTo, int yFrom, int yTo) {
        final ArrayList<MandelbrotElement> chunk = new ArrayList<>();
        MandelbrotElement elementZero;
        for (int x = xFrom; x < xTo; x++) {
            for (int y = yFrom; y < yTo; y++) {
                elementZero = elementsStaticMandelbrot[x][y];
                if (elementZero.isActiveNew()) {
                    chunk.add(elementZero);
                    if (!firstDomainExecution) {
                        if (RESOLUTION_MULTIPLIER != none) {
                            /*
                             * All new elements are Active new.
                             * Wrap only those elements which have some hibernated active neighbours.
                             * Most new elements are far away from interesting Mandelbrot set horizon
                             */
                            if (isOnMandelbrotHorizon(x, y)) {
                                wrapped++;
                                wrap(chunk, elementZero);
                            } else {
                                notWrapped++;
                            }
                        }
                    }
                }
            }
        }
        return chunk;
    }

    private void wrap(ArrayList<MandelbrotElement> domainFull, MandelbrotElement elementZero) {
        if (RESOLUTION_MULTIPLIER == square_alter) {
            final double d = AreaMandelbrot.plank() / 3;
            if (odd) {
                domainFull.add(activeNew(elementZero.originRe + d, elementZero.originIm + d));
                domainFull.add(activeNew(elementZero.originRe - d, elementZero.originIm - d));
            } else {
                domainFull.add(activeNew(elementZero.originRe - d, elementZero.originIm + d));
                domainFull.add(activeNew(elementZero.originRe + d, elementZero.originIm - d));
            }
        } else {
            final int multiplier;
            switch (RESOLUTION_MULTIPLIER) {
                case square_3 -> multiplier = 3;
                case square_5 -> multiplier = 5;
                case square_11 -> multiplier = 11;
                case square_51 -> multiplier = 51;
                case square_101 -> multiplier = 101;
                default -> throw new RuntimeException("unknown RESOLUTION_MULTIPLIER");
            }

            final double pn = AreaMandelbrot.plank() / multiplier;
            final int half = (multiplier - 1) / 2;
            /* This fills the pixel with multiple points */
            for (int x = -half; x <= half; x++) {
                for (int y = -half; y <= half; y++) {
                    if (x != 0 || y != 0) {
                        domainFull.add(activeNew(elementZero.originRe + (x * pn), elementZero.originIm + (y * pn)));
                    }
                    /* else do nothing, there already is element0 for the center of this pixel */
                }
            }
        }
    }

    public void maskFullUpdate() {
        log.debug("createMask()");
        for (int x = 0; x < RESOLUTION_WIDTH; x++) {
            for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
                MandelbrotMaskImage.setRGB(x, y, colorForState(elementsStaticMandelbrot[x][y]).getRGB());
            }
        }
    }

    /*
     * This is called already after zoom
     */
    public void recalculatePixelsPositionsForThisZoom() {
        int finishedLong = 0;

        /*
         * Scan Mandelbrot elements - old positions from previous calculation
         * They will be moved to new positions - remembered elements
         * For these moved elements the next calculation will be skipped. They are all hibernated from previous calculations.
         */
        MandelbrotElement element;
        for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
            for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
                element = elementsStaticMandelbrot[xx][yy];
                if (element.isFinishedSuccessNow()) {
                    finishedLong++;
                }
                /* There was already zoom in, the new area si smaller */
                if (AreaMandelbrot.contains(element.originRe, element.originIm)) {
                    /* Move elements to new coordinates */
                    elementsToRemember.add(element);
                }
            }
        }

        FinebrotFractal.elements(finishedLong);

        /*
         * Delete all elements assigned to Mandelbrot coordinates.
         * Some are remembered and will be moved to new coordinates.
         */
        for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
            for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
                elementsStaticMandelbrot[xx][yy] = null;
            }
        }

        /*
         * Add remembered elements to their new position for new calculation
         * If there is a conflict, two or more points moved to same pixel, then use the active one if there is any.
         * Don't drop conflicts around, simply calculate new elements in the next calculation iteration.
         */
        int newPositionX;
        int newPositionY;
        MandelbrotElement filledAlready;
        final Mem m = new Mem();

        for (MandelbrotElement el : elementsToRemember) {
            AreaMandelbrot.domainToScreenCarry(m, el.originRe, el.originIm);
            newPositionX = m.px;
            newPositionY = m.py;

            if (newPositionY != Mem.NOT && newPositionX != Mem.NOT) {
                filledAlready = elementsStaticMandelbrot[newPositionX][newPositionY];
                if (filledAlready != null) {
                    /* conflict */
                    if (filledAlready.hasWorseStateThen(el)) {
                        /*
                         * Replace by element with better state
                         * Better to delete the other one, then to drop it to other empty px.
                         * That would cause problem with optimization, better calculate new and shiny px.
                         */
                        elementsStaticMandelbrot[newPositionX][newPositionY] = el;
                    }
                } else {
                    /* Good, there is no conflict */
                    elementsStaticMandelbrot[newPositionX][newPositionY] = el;
                }
            }
        }

        /*
         * Repaint with only moved elements
         */
        maskFullUpdate();
        Application.repaintMandelbrotWindow();

        /*
         * Create new elements on positions where nothing was moved to
         */
        MandelbrotElement el;
        for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
            for (int x = 0; x < RESOLUTION_WIDTH; x++) {
                el = elementsStaticMandelbrot[x][y];
                if (el == null) {
                    AreaMandelbrot.screenToDomainCarry(m, x, y);
                    if (allNeighborsFinishedTooLong(x, y)) {
                        /* Calculation for some positions should be skipped as they are too far away form any long successful divergent position */
                        elementsStaticMandelbrot[x][y] = hibernatedDeepBlack(m.re, m.im);
                    } else {
                        elementsStaticMandelbrot[x][y] = activeNew(m.re, m.im);
                    }
                } else {
                    /* If relevant, mark it as element from previous calculation iteration */
                    el.past();
                }
            }
        }

        elementsToRemember.clear();
    }

    /**
     * Verify if any neighbor px,py finished well, long or at least too short.
     * This method identifies deep black convergent elements of Mandelbrot set inside.
     * Don't do any calculation for them.
     */
    public boolean allNeighborsFinishedTooLong(int x, int y) {
        MandelbrotElement el;
        for (int a = -neighbours; a < neighbours; a++) {
            for (int b = -neighbours; b < neighbours; b++) {
                int xx = x + a;
                int yy = y + b;
                if (checkDomain(xx, yy)) {
                    el = elementsStaticMandelbrot[xx][yy];
                    if (el != null && (el.isFinishedSuccessAny() || el.isFinishedTooShort())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /*
     * All new elements are Active New
     * For wrapping, search only elements, which have some past finished neighbors
     */
    public boolean isOnMandelbrotHorizon(int x, int y) {
        boolean red = false;
        boolean black = false;
        MandelbrotElement el;
        for (int a = -neighbours; a < neighbours; a++) {
            for (int b = -neighbours; b < neighbours; b++) {
                int xx = x + a;
                int yy = y + b;
                if (checkDomain(xx, yy)) {
                    el = elementsStaticMandelbrot[xx][yy];
                    if (el != null) {
                        if (el.isFinishedSuccessPast()) {
                            red = true;
                        }
                        if (el.isHibernated()) {
                            black = true;
                        }
                        if (red && black) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
