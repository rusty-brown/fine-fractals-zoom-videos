package fine.fractals.math;

import fine.fractals.Application;
import fine.fractals.Main;
import fine.fractals.fractal.Fractal;
import fine.fractals.math.common.Element;
import fine.fractals.math.common.HH;
import fine.fractals.ui.Formatter;
import fine.fractals.ui.OneTarget;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AreaDomain {

    public static AreaDomain ME;
    private static final Logger log = LogManager.getLogger(AreaDomain.class);
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
    private int resolutionT;
    private int resolutionX;
    private int resolutionHalfT;
    private int resolutionHalfX;

    public AreaDomain(double size, double centerReT, double centerImX, int resolutionT, int resolutionX) {
        this.resolutionT = resolutionT;
        this.resolutionX = resolutionX;
        // this.resolutionHalf = new N(this.resolutionT / 2, this.resolutionX / 2, this.resolution.y / 2, this.resolution.z / 2);
        this.resolutionHalfT = this.resolutionT / 2;
        this.resolutionHalfX = this.resolutionX / 2;

        double scrRatioX = (double) resolutionX / (double) resolutionT;

        this.sizeReT = size;
        this.sizeImX = size * scrRatioX;


        this.centerReT = centerReT;
        this.centerImX = centerImX;
        this.plank = size / resolutionT;

        log.info("plank: " + plank);

        this.numbersReT = new double[resolutionT];
        this.numbersImX = new double[resolutionX];

        initiate();

        ME = this;
    }

    public boolean contains(double reT, double imX) {
        return reT > this.borderLowReT
                && reT < this.borderHighReT
                && imX > this.borderLowImX
                && imX < this.borderHighImX;
    }

    public boolean contains(HH hh) {
        return hh.calculation.re > this.borderLowReT
                && hh.calculation.re < this.borderHighReT
                && hh.calculation.im > this.borderLowImX
                && hh.calculation.im < this.borderHighImX;
    }


    public double screenToDomainReT(int t) {
        return numbersReT[t];
    }

    public double screenToDomainImX(int x) {
        return numbersImX[x];
    }

    /* It will be carried by Calculation */
    public void screenToDomainCarry(HH hh, int t, int x) {
        try {
            hh.calculation.re = numbersReT[t];
            hh.calculation.im = numbersImX[x];
        } catch (Exception e) {
            log.fatal("screenToDomainCarry()", e);
        }
    }


    public boolean domainToScreenCarry(HH hh, double reT, double imX) {
        hh.calculation.pxRe = (int) Math.round((resolutionT * (reT - this.centerReT) / this.sizeReT) + resolutionHalfT);
        if (hh.calculation.pxRe >= resolutionT || hh.calculation.pxRe < 0) {
            hh.calculation.pxRe = HH.NOT;
            return false;
        }

        hh.calculation.pxIm = (int) Math.round(((resolutionX * (imX - this.centerImX)) / this.sizeImX) + resolutionHalfX);
        if (hh.calculation.pxIm >= resolutionX || hh.calculation.pxIm < 0) {
            hh.calculation.pxIm = HH.NOT;
            return false;
        }

        // y = (int) Math.round(((super.resolution.y * (imY - this.center.imY)) / this.size.imY) + super.resolutionHalf.y);
        // if (y >= super.resolution.y || y < 0) {
        // 	y = HH.NOT;
        // 	return false;
        // }
        //
        // z = (int) Math.round(((super.resolution.z * (imZ - this.center.imZ)) / this.size.imZ) + super.resolutionHalf.z);
        // if (z >= super.resolution.z || z < 0) {
        // 	z = HH.NOT;
        // 	return false;
        // }

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

        // this.borderLow.imY = center.imY - (size.imY / 2);
        // this.borderHigh.imY = center.imY + (size.imY / 2);
        // this.borderLow.imZ = center.imZ - (size.imZ / 2);
        // this.borderHigh.imZ = center.imZ + (size.imZ / 2);

        calculatePoints();
    }

    public void zoomIn() {
        sizeReT = sizeReT * Application.ZOOM;
        sizeImX = sizeImX * Application.ZOOM;
        this.plank = sizeReT / resolutionT;
        initiate();
    }

    public void zoomOut() {
        sizeReT = sizeReT * (1 / Application.ZOOM);
        sizeImX = sizeImX * (1 / Application.ZOOM);
        this.plank = sizeReT / resolutionT;
        initiate();
    }

    public String[] cToString(HH hh, int t, int x) {
        screenToDomainCarry(hh, t, x);
        return new String[]{
                Formatter.roundString(hh.calculation.re),
                Formatter.roundString(hh.calculation.im)};
    }

    public String sizeReTString() {
        return Formatter.roundString(this.sizeReT);
    }

    public String sizeImXString() {
        return Formatter.roundString(this.sizeImX);
    }


    public String sizeTString4() {
        return Formatter.round4(this.sizeReT);
    }

    public String sizeImXString4() {
        return Formatter.round4(this.sizeImX);
    }

    public void moveToCoordinates(OneTarget target) {
        this.centerReT = screenToDomainReT(target.getScreenFromCornerT());
        this.centerImX = screenToDomainImX(target.getScreenFromCornerX());
        //this.center.imY = screenToDomainImY(target.getScreenFromCorner());;
        //this.center.imZ = screenToDomainImZ(target.getScreenFromCorner());;
        log.info("Move to: " + this.centerReT + "," + this.centerImX);
    }

    /* Generate domain elements */
    private void calculatePoints() {
        for (int tt = 0; tt < resolutionT; tt++) {
            numbersReT[tt] = borderLowReT + (this.plank * tt);
        }
        for (int xx = 0; xx < resolutionX; xx++) {
            numbersImX[xx] = borderLowImX + (this.plank * xx);
        }

        //for (int yy = 0; yy < resolution.y; yy++) {
        //	numbersImY[yy] = borderLow.imY + (this.plank * yy);
        //}
        //for (int zz = 0; zz < resolution.z; zz++) {
        //	numbersImZ[zz] = borderLow.imZ + (this.plank * zz);
        //}
    }

    public double plank() {
        return this.plank;
    }

    /**
     * move to zoom target
     */
    public void moveToInitialCoordinates() {
        this.centerReT = Fractal.INIT_DOMAIN_TARGET_re;
        this.centerImX = Fractal.INIT_DOMAIN_TARGET_im;
    }


    /**
     * Call only if RESOLUTION_MULTIPLIER > (1 or odd)
     * If RM = 4 then wrapping will be 25... from -2 to +2 including zero
     * <p>
     * As zoom progress elements move further away from each other.
     * So there won're be any conflicts with already calculated elements.
     */
    public void wrapS(Element elementZero, Element[] wrapping) {
        if (Main.RESOLUTION_MULTIPLIER > 1 && Main.RESOLUTION_MULTIPLIER % 2 == 1) {
            int index = 0;
            double pn = this.plank / Main.RESOLUTION_MULTIPLIER;
            int half = (Main.RESOLUTION_MULTIPLIER - 1) / 2;
            for (int t = -half; t <= half; t++) {
                for (int x = -half; x <= half; x++) {
                    if (t == 0 && x == 0) {
                        // This was already calculated without wrap
                        wrapping[index++] = elementZero;
                    } else {
                        /* This only fills the pixel with multiple points */
                        double a = elementZero.originReT + (t * pn);
                        double b = elementZero.originImX + (x * pn);
                        wrapping[index++] = new Element(a, b);
                    }
                }
            }
        } else {
            throw new RuntimeException("AreaDomain: RESOLUTION_MULTIPLIER can be only 1 or odd");
        }
    }

    public void wrap2(Element elementZero, Element[] wrapping) {
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

    public double left() {
        return this.borderLowReT;
    }

    public double right() {
        return this.borderHighReT;
    }
}
