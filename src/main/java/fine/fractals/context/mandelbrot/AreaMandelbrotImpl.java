package fine.fractals.context.mandelbrot;

import fine.fractals.Main;
import fine.fractals.data.Element;
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
    public double centerReT;
    public double centerImX;
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
    private int resolutionHalfT;
    private int resolutionHalfX;

    public static AreaMandelbrotImpl AreaMandelbrot;

    static {
        log.info("init");
        AreaMandelbrot = new AreaMandelbrotImpl();
        log.info("initiate");
        AreaMandelbrot.initiate();
    }

    private AreaMandelbrotImpl() {
        double size = INIT_AREA_DOMAIN_SIZE;
        double centerImX = INIT_DOMAIN_TARGET_im;
        // this.resolutionHalf = new N(this.RESOLUTION_WIDTH / 2, this.RESOLUTION_HEIGHT / 2, this.resolution.y / 2, this.resolution.z / 2);
        this.resolutionHalfT = RESOLUTION_WIDTH / 2;
        this.resolutionHalfX = RESOLUTION_HEIGHT / 2;

        double scrRatioX = (double) RESOLUTION_HEIGHT / (double) RESOLUTION_WIDTH;

        this.sizeReT = size;
        this.sizeImX = size * scrRatioX;


        this.centerReT = INIT_DOMAIN_TARGET_re;
        this.centerImX = centerImX;
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


    public boolean domainToScreenCarry(Mem mem, double reT, double imX) {
        mem.pxRe = (int) Math.round((RESOLUTION_WIDTH * (reT - this.centerReT) / this.sizeReT) + resolutionHalfT);
        if (mem.pxRe >= RESOLUTION_WIDTH || mem.pxRe < 0) {
            mem.pxRe = Mem.NOT;
            return false;
        }

        mem.pxIm = (int) Math.round(((RESOLUTION_HEIGHT * (imX - this.centerImX)) / this.sizeImX) + resolutionHalfX);
        if (mem.pxIm >= RESOLUTION_HEIGHT || mem.pxIm < 0) {
            mem.pxIm = Mem.NOT;
            return false;
        }
        return true;
    }

    /**
     * call after Zoom in or out
     */
    private void initiate() {
        this.borderLowReT = centerReT - (sizeReT / 2);
        this.borderHighReT = centerReT + (sizeReT / 2);
        this.borderLowImX = centerImX - (sizeImX / 2);
        this.borderHighImX = centerImX + (sizeImX / 2);

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

    public String sizeImXString() {
        return Formatter.roundString(this.sizeImX);
    }


    public void moveToCoordinates() {
        this.centerReT = screenToDomainReT(Target.getScreenFromCornerT());
        this.centerImX = screenToDomainImX(Target.getScreenFromCornerX());
        log.info("Move to: " + this.centerReT + "," + this.centerImX);
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

    public double plank() {
        return this.plank;
    }

    /**
     * move to zoom target
     */
    public void moveToInitialCoordinates() {
        this.centerReT = INIT_DOMAIN_TARGET_re;
        this.centerImX = INIT_DOMAIN_TARGET_im;
    }


    public void wrap(Element elementZero, Element[] wrapping) {
        if (Main.RESOLUTION_MULTIPLIER == 2) {
            int index = 0;
            double pn = this.plank / 3;
            int half = (Main.RESOLUTION_MULTIPLIER - 1) / 2;

            /* This only fills the pixel with multiple points */
            double a1 = elementZero.originReT + (half * pn);
            double b1 = elementZero.originImX + (half * pn);
            wrapping[index++] = new Element(a1, b1);

            double a2 = elementZero.originReT + (-half * pn);
            double b2 = elementZero.originImX + (-half * pn);
            wrapping[index] = new Element(a2, b2);
        } else {
            throw new RuntimeException("AreaDomain: RESOLUTION_MULTIPLIER can be only 1 or odd");
        }
    }
}
