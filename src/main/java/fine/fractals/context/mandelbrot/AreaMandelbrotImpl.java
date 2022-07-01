package fine.fractals.context.mandelbrot;

import fine.fractals.Main;
import fine.fractals.data.MandelbrotElement;
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
    private final double[] numbersReT;
    private final double[] numbersImX;
    /* position of the centre of domain area */
    public double centerRe;
    public double centerIm;
    /* size of domain area */
    public double sizeReT;
    public double sizeImX;
    private double borderLowReT;
    private double borderLowImX;
    private double borderHighReT;
    private double borderHighImX;
    /* Plank's length */
    /* It depends on Height which is resolution domain Y */
    private double plank;
    private final int resolutionHalfT;
    private final int resolutionHalfX;

    public static AreaMandelbrotImpl AreaMandelbrot;

    static {
        log.info("init");
        AreaMandelbrot = new AreaMandelbrotImpl();
        log.info("initiate");
        AreaMandelbrot.initiate();
    }

    private AreaMandelbrotImpl() {
        double size = INIT_AREA_DOMAIN_SIZE;
        this.resolutionHalfT = RESOLUTION_WIDTH / 2;
        this.resolutionHalfX = RESOLUTION_HEIGHT / 2;

        final double scrRatioX = (double) RESOLUTION_HEIGHT / (double) RESOLUTION_WIDTH;
        this.sizeReT = size;
        this.sizeImX = size * scrRatioX;

        this.centerRe = INIT_DOMAIN_TARGET_re;
        this.centerIm = INIT_DOMAIN_TARGET_im;
        this.plank = size / RESOLUTION_WIDTH;
        log.info("plank: " + plank);

        this.numbersReT = new double[RESOLUTION_WIDTH];
        this.numbersImX = new double[RESOLUTION_HEIGHT];
    }

    public boolean contains(double reT, double imX) {
        return reT > this.borderLowReT
                && reT < this.borderHighReT
                && imX > this.borderLowImX
                && imX < this.borderHighImX;
    }

    public boolean contains(Mem mem) {
        return mem.re > this.borderLowReT
                && mem.re < this.borderHighReT
                && mem.im > this.borderLowImX
                && mem.im < this.borderHighImX;
    }


    public double screenToDomainReT(int t) {
        return numbersReT[t];
    }

    public double screenToDomainImX(int x) {
        return numbersImX[x];
    }

    /* It will be carried by Calculation */
    public void screenToDomainCarry(Mem mem, int t, int x) {
        try {
            mem.re = numbersReT[t];
            mem.im = numbersImX[x];
        } catch (Exception e) {
            log.fatal("screenToDomainCarry()", e);
        }
    }


    public void domainToScreenCarry(Mem mem, double reT, double imX) {
        mem.pxRe = (int) Math.round((RESOLUTION_WIDTH * (reT - this.centerRe) / this.sizeReT) + resolutionHalfT);
        if (mem.pxRe >= RESOLUTION_WIDTH || mem.pxRe < 0) {
            mem.pxRe = Mem.NOT;
            return;
        }
        mem.pxIm = (int) Math.round(((RESOLUTION_HEIGHT * (imX - this.centerIm)) / this.sizeImX) + resolutionHalfX);
        if (mem.pxIm >= RESOLUTION_HEIGHT || mem.pxIm < 0) {
            mem.pxIm = Mem.NOT;
        }
    }

    /**
     * call after Zoom in or out
     */
    private void initiate() {
        this.borderLowReT = centerRe - (sizeReT / 2);
        this.borderHighReT = centerRe + (sizeReT / 2);
        this.borderLowImX = centerIm - (sizeImX / 2);
        this.borderHighImX = centerIm + (sizeImX / 2);

        calculatePoints();
    }

    public void zoomIn() {
        sizeReT = sizeReT * ZOOM;
        sizeImX = sizeImX * ZOOM;
        this.plank = sizeReT / RESOLUTION_WIDTH;
        initiate();
    }

    public String[] cToString(Mem mem, int t, int x) {
        screenToDomainCarry(mem, t, x);
        return new String[]{
                Formatter.roundString(mem.re),
                Formatter.roundString(mem.im)};
    }

    public String sizeReTString() {
        return Formatter.roundString(this.sizeReT);
    }

    public String sizeImString() {
        return Formatter.roundString(this.sizeImX);
    }


    public void moveToCoordinates() {
        this.centerRe = screenToDomainReT(Target.getScreenFromCornerT());
        this.centerIm = screenToDomainImX(Target.getScreenFromCornerX());
        log.info("Move to: " + this.centerRe + "," + this.centerIm);
    }

    /* Generate domain elements */
    private void calculatePoints() {
        for (int tt = 0; tt < RESOLUTION_WIDTH; tt++) {
            numbersReT[tt] = borderLowReT + (this.plank * tt);
        }
        for (int xx = 0; xx < RESOLUTION_HEIGHT; xx++) {
            numbersImX[xx] = borderLowImX + (this.plank * xx);
        }
    }

    /**
     * move to zoom target
     */
    public void moveToInitialCoordinates() {
        this.centerRe = INIT_DOMAIN_TARGET_re;
        this.centerIm = INIT_DOMAIN_TARGET_im;
    }


    public void wrap(MandelbrotElement elementZero, MandelbrotElement[] wrapping) {
        if (Main.RESOLUTION_MULTIPLIER == 2) {
            int index = 0;
            double pn = this.plank / 3;
            int half = (Main.RESOLUTION_MULTIPLIER - 1) / 2;

            /* This only fills the pixel with multiple points */
            double a1 = elementZero.originReT + (half * pn);
            double b1 = elementZero.originImX + (half * pn);
            wrapping[index++] = new MandelbrotElement(a1, b1);

            double a2 = elementZero.originReT + (-half * pn);
            double b2 = elementZero.originImX + (-half * pn);
            wrapping[index] = new MandelbrotElement(a2, b2);
        } else {
            throw new RuntimeException("AreaDomain: RESOLUTION_MULTIPLIER can be only 1 or odd");
        }
    }
}
