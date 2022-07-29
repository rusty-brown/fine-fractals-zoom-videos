package fine.fractals.fractal.mandelbrot;

import fine.fractals.data.mem.Mem;
import fine.fractals.formatter.Formatter;
import fine.fractals.fractal.finebrot.common.AreaAbstract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.INIT_MANDELBROT_AREA_SIZE;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.INIT_MANDELBROT_TARGET_im;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.INIT_MANDELBROT_TARGET_re;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_WIDTH;
import static fine.fractals.machine.ApplicationImpl.ZOOM;
import static fine.fractals.machine.TargetImpl.Target;

public class AreaMandelbrotImpl extends AreaAbstract {

    /**
     * Singleton instance
     */
    public static final AreaMandelbrotImpl AreaMandelbrot;
    private static final Logger log = LogManager.getLogger(AreaMandelbrotImpl.class);

    static {
        log.debug("init");
        AreaMandelbrot = new AreaMandelbrotImpl();
        log.debug("initiate");
        AreaMandelbrot.initiate();
    }

    private final double[] numbersRe;
    private final double[] numbersIm;
    private double borderLowRe;
    private double borderLowIm;
    private double borderHighRe;
    private double borderHighIm;
    private double plank;

    private AreaMandelbrotImpl() {
        log.debug("constructor");
        final double size = INIT_MANDELBROT_AREA_SIZE;
        final double scrRatioX = (double) RESOLUTION_HEIGHT / (double) RESOLUTION_WIDTH;
        this.sizeRe = size;
        this.sizeIm = size * scrRatioX;

        this.centerRe = INIT_MANDELBROT_TARGET_re;
        this.centerIm = INIT_MANDELBROT_TARGET_im;
        this.plank = size / RESOLUTION_WIDTH;
        log.debug("plank: " + plank);

        this.numbersRe = new double[RESOLUTION_WIDTH];
        this.numbersIm = new double[RESOLUTION_HEIGHT];
    }

    public boolean contains(double re, double im) {
        return re > this.borderLowRe
                && re < this.borderHighRe
                && im > this.borderLowIm
                && im < this.borderHighIm;
    }

    public double screenToDomainRe(int x) {
        return numbersRe[x];
    }

    public double screenToDomainIm(int y) {
        return numbersIm[y];
    }

    public void screenToDomainCarry(Mem m, int x, int y) {
        try {
            m.re = numbersRe[x];
            m.im = numbersIm[y];
        } catch (Exception e) {
            log.fatal("screenToDomainCarry()", e);
        }
    }

    /**
     * call after Zoom in
     */
    private void initiate() {
        this.borderLowRe = centerRe - (sizeRe / 2);
        this.borderHighRe = centerRe + (sizeRe / 2);
        this.borderLowIm = centerIm - (sizeIm / 2);
        this.borderHighIm = centerIm + (sizeIm / 2);

        calculatePoints();
    }

    public void zoomIn() {
        sizeRe = sizeRe * ZOOM;
        sizeIm = sizeIm * ZOOM;
        this.plank = sizeRe / RESOLUTION_WIDTH;
        initiate();
    }

    public String[] cToString(Mem m, int x, int y) {
        screenToDomainCarry(m, x, y);
        return new String[]{
                Formatter.roundString(m.re),
                Formatter.roundString(m.im)};
    }

    public String sizeReString() {
        return Formatter.roundString(this.sizeRe);
    }

    public String sizeImString() {
        return Formatter.roundString(this.sizeIm);
    }


    public void moveToCoordinates() {
        this.centerRe = screenToDomainRe(Target.getScreenFromCornerX());
        this.centerIm = screenToDomainIm(Target.getScreenFromCornerY());
        log.debug("Move to: " + this.centerRe + "," + this.centerIm);
    }

    /* Generate domain elements */
    private void calculatePoints() {
        for (int x = 0; x < RESOLUTION_WIDTH; x++) {
            numbersRe[x] = borderLowRe + (this.plank * x);
        }
        for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
            numbersIm[y] = borderLowIm + (this.plank * y);
        }
    }

    /**
     * move to zoom target
     */
    public void moveToInitialCoordinates() {
        this.centerRe = INIT_MANDELBROT_TARGET_re;
        this.centerIm = INIT_MANDELBROT_TARGET_im;
    }

    public double plank() {
        return plank;
    }
}
