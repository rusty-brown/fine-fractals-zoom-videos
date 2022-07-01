package fine.fractals.context.mandelbrot;

import fine.fractals.data.Mem;
import fine.fractals.formatter.Formatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.ApplicationImpl.ZOOM;
import static fine.fractals.context.TargetImpl.Target;
import static fine.fractals.fractal.Fractal.*;

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
    /* Plank's length */
    /* It depends on Height which is resolution domain Y */
    private double plank;
    private final int resolutionHalfRe;
    private final int resolutionHalfIm;

    public static AreaMandelbrotImpl AreaMandelbrot;

    static {
        log.info("init");
        AreaMandelbrot = new AreaMandelbrotImpl();
        log.info("initiate");
        AreaMandelbrot.initiate();
    }

    private AreaMandelbrotImpl() {
        double size = INIT_AREA_DOMAIN_SIZE;
        this.resolutionHalfRe = RESOLUTION_WIDTH / 2;
        this.resolutionHalfIm = RESOLUTION_HEIGHT / 2;

        final double scrRatioX = (double) RESOLUTION_HEIGHT / (double) RESOLUTION_WIDTH;
        this.sizeRe = size;
        this.sizeIm = size * scrRatioX;

        this.centerRe = INIT_DOMAIN_TARGET_re;
        this.centerIm = INIT_DOMAIN_TARGET_im;
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

    public boolean contains(Mem mem) {
        return mem.re > this.borderLowRe
                && mem.re < this.borderHighRe
                && mem.im > this.borderLowIm
                && mem.im < this.borderHighIm;
    }


    public double screenToDomainRe(int t) {
        return numbersRe[t];
    }

    public double screenToDomainIm(int x) {
        return numbersIm[x];
    }

    /* It will be carried by Calculation */
    public void screenToDomainCarry(Mem mem, int x, int y) {
        try {
            mem.re = numbersRe[x];
            mem.im = numbersIm[y];
        } catch (Exception e) {
            log.fatal("screenToDomainCarry()", e);
        }
    }


    public void domainToScreenCarry(Mem mem, double re, double im) {
        mem.pxRe = (int) Math.round((RESOLUTION_WIDTH * (re - this.centerRe) / this.sizeRe) + resolutionHalfRe);
        if (mem.pxRe >= RESOLUTION_WIDTH || mem.pxRe < 0) {
            mem.pxRe = Mem.NOT;
            return;
        }
        mem.pxIm = (int) Math.round(((RESOLUTION_HEIGHT * (im - this.centerIm)) / this.sizeIm) + resolutionHalfIm);
        if (mem.pxIm >= RESOLUTION_HEIGHT || mem.pxIm < 0) {
            mem.pxIm = Mem.NOT;
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

    public String[] cToString(Mem mem, int x, int y) {
        screenToDomainCarry(mem, x, y);
        return new String[]{
                Formatter.roundString(mem.re),
                Formatter.roundString(mem.im)};
    }

    public String sizeReTString() {
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
        for (int tt = 0; tt < RESOLUTION_WIDTH; tt++) {
            numbersRe[tt] = borderLowRe + (this.plank * tt);
        }
        for (int xx = 0; xx < RESOLUTION_HEIGHT; xx++) {
            numbersIm[xx] = borderLowIm + (this.plank * xx);
        }
    }

    /**
     * move to zoom target
     */
    public void moveToInitialCoordinates() {
        this.centerRe = INIT_DOMAIN_TARGET_re;
        this.centerIm = INIT_DOMAIN_TARGET_im;
    }

    public double plank() {
        return plank;
    }
}
