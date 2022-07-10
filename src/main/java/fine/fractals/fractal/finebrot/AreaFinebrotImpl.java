package fine.fractals.fractal.finebrot;

import fine.fractals.data.mem.Mem;
import fine.fractals.formatter.Formatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.INIT_FINEBROT_AREA_SIZE;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.INIT_FINEBROT_TARGET_im;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.INIT_FINEBROT_TARGET_re;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_WIDTH;
import static fine.fractals.machine.ApplicationImpl.ZOOM;
import static fine.fractals.machine.TargetImpl.Target;

public class AreaFinebrotImpl {

    public static final AreaFinebrotImpl AreaFinebrot;
    private static final Logger log = LogManager.getLogger(AreaFinebrotImpl.class);

    static {
        log.info("init");
        AreaFinebrot = new AreaFinebrotImpl();
        log.info("initiate");
        AreaFinebrot.initiate();
    }

    private final double[] numbersRe;
    private final double[] numbersIm;
    private final int resolutionHalfRe;
    private final int resolutionHalfIm;
    /* position of the centre of image area */
    public double centerRe;
    public double centerIm;
    /* size of image area */
    public double sizeRe;
    public double sizeIm;
    private double borderLowRe;
    private double borderLowIm;
    private double borderHighRe;
    private double borderHighIm;
    private double plank;

    private AreaFinebrotImpl() {
        log.debug("constructor");
        this.resolutionHalfRe = RESOLUTION_WIDTH / 2;
        this.resolutionHalfIm = RESOLUTION_HEIGHT / 2;

        final double scrRatioX = (double) RESOLUTION_HEIGHT / (double) RESOLUTION_WIDTH;
        this.sizeRe = INIT_FINEBROT_AREA_SIZE;
        this.sizeIm = INIT_FINEBROT_AREA_SIZE * scrRatioX;

        this.centerRe = INIT_FINEBROT_TARGET_re;
        this.centerIm = INIT_FINEBROT_TARGET_im;
        this.plank = INIT_FINEBROT_AREA_SIZE / RESOLUTION_WIDTH;
        log.info("plank: " + plank);

        this.numbersRe = new double[RESOLUTION_WIDTH];
        this.numbersIm = new double[RESOLUTION_HEIGHT];
    }

    public boolean contains(Mem m) {
        return m.re > this.borderLowRe
                && m.re < this.borderHighRe
                && m.im > this.borderLowIm
                && m.im < this.borderHighIm;
    }

    public boolean isOutside(double re, double im) {
        return re < this.borderLowRe
                || re > this.borderHighRe
                || im < this.borderLowIm
                || im > this.borderHighIm;
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


    public double screenToDomainCreateRe(int x) {
        return numbersRe[x];
    }

    public double screenToDomainCreateIm(int y) {
        return numbersIm[y];
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

    public String sizeReString() {
        return Formatter.roundString(this.sizeRe);
    }

    public String sizeImString() {
        return Formatter.roundString(this.sizeIm);
    }

    public void moveToCoordinates() {
        this.centerRe = screenToDomainCreateRe(Target.getScreenFromCornerX());
        this.centerIm = screenToDomainCreateIm(Target.getScreenFromCornerY());
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
        this.centerRe = INIT_FINEBROT_TARGET_re;
        this.centerIm = INIT_FINEBROT_TARGET_im;
    }
}
