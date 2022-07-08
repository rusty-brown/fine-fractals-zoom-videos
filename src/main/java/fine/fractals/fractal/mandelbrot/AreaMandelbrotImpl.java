package fine.fractals.fractal.mandelbrot;

import fine.fractals.data.mem.Mem;
import fine.fractals.formatter.Formatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.context.ApplicationImpl.RESOLUTION_HEIGHT;
import static fine.fractals.context.ApplicationImpl.RESOLUTION_WIDTH;
import static fine.fractals.context.ApplicationImpl.ZOOM;
import static fine.fractals.machine.TargetImpl.Target;
import static fine.fractals.fractal.finebrot.common.FinebrotFractalImpl.*;

public class AreaMandelbrotImpl {

    private static final Logger log = LogManager.getLogger(AreaMandelbrotImpl.class);
    private final double[] numbersRe;
    private final double[] numbersIm;
    /* position of the centre of domain area */
    public double centerRe;
    public double centerIm;
    /* size of domain area */
    public double sizeRe;
    public double sizeIm;
    private double borderLowRe;
    private double borderLowIm;
    private double borderHighRe;
    private double borderHighIm;
    private double plank;
    private final int resolutionHalfRe;
    private final int resolutionHalfIm;

    public static final AreaMandelbrotImpl AreaMandelbrot;

    static {
        log.info("init");
        AreaMandelbrot = new AreaMandelbrotImpl();
        log.info("initiate");
        AreaMandelbrot.initiate();
    }

    private AreaMandelbrotImpl() {
        double size = INIT_MANDELBROT_AREA_SIZE;
        this.resolutionHalfRe = RESOLUTION_WIDTH / 2;
        this.resolutionHalfIm = RESOLUTION_HEIGHT / 2;

        final double scrRatioX = (double) RESOLUTION_HEIGHT / (double) RESOLUTION_WIDTH;
        this.sizeRe = size;
        this.sizeIm = size * scrRatioX;

        this.centerRe = INIT_MANDELBROT_TARGET_re;
        this.centerIm = INIT_MANDELBROT_TARGET_im;
        this.plank = size / RESOLUTION_WIDTH;
        log.info("plank: " + plank);

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

    /* It will be carried by Calculation */
    public void screenToDomainCarry(Mem m, int x, int y) {
        try {
            m.re = numbersRe[x];
            m.im = numbersIm[y];
        } catch (Exception e) {
            log.fatal("screenToDomainCarry()", e);
        }
    }


    public void domainToScreenCarry(Mem m, double re, double im) {
        m.px = (int) Math.round((RESOLUTION_WIDTH * (re - this.centerRe) / this.sizeRe) + resolutionHalfRe);
        if (m.px >= RESOLUTION_WIDTH || m.px < 0) {
            m.px = Mem.NOT;
            return;
        }
        m.py = (int) Math.round(((RESOLUTION_HEIGHT * (im - this.centerIm)) / this.sizeIm) + resolutionHalfIm);
        if (m.py >= RESOLUTION_HEIGHT || m.py < 0) {
            m.py = Mem.NOT;
        }
    }

    /**
     * call after Zoom in or out
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
        log.info("Move to: " + this.centerRe + "," + this.centerIm);
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
