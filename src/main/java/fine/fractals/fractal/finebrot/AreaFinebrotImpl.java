package fine.fractals.fractal.finebrot;

import fine.fractals.data.mem.Mem;
import fine.fractals.formatter.Formatter;
import fine.fractals.fractal.finebrot.common.AreaAbstract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.INIT_FINEBROT_AREA_SIZE;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.INIT_FINEBROT_TARGET_im;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.INIT_FINEBROT_TARGET_re;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_WIDTH;
import static fine.fractals.machine.ApplicationImpl.ZOOM;
import static fine.fractals.machine.TargetImpl.Target;

public class AreaFinebrotImpl extends AreaAbstract {

    private static final Logger log = LogManager.getLogger(AreaFinebrotImpl.class);

    /**
     * Singleton instance
     */
    public static final AreaFinebrotImpl AreaFinebrot;

    static {
        log.debug("init");
        AreaFinebrot = new AreaFinebrotImpl();
        log.debug("initiate");
        AreaFinebrot.initiate();
    }

    private final double[] numbersRe;
    private final double[] numbersIm;
    public double borderLowRe;
    public double borderLowIm;
    public double borderHighRe;
    public double borderHighIm;
    private double plank;

    private AreaFinebrotImpl() {
        log.debug("constructor");

        final double scrRatioX = (double) RESOLUTION_HEIGHT / (double) RESOLUTION_WIDTH;
        this.sizeRe = INIT_FINEBROT_AREA_SIZE;
        this.sizeIm = INIT_FINEBROT_AREA_SIZE * scrRatioX;

        this.centerRe = INIT_FINEBROT_TARGET_re;
        this.centerIm = INIT_FINEBROT_TARGET_im;
        this.plank = INIT_FINEBROT_AREA_SIZE / RESOLUTION_WIDTH;
        log.debug("plank: " + plank);

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
        this.centerRe = INIT_FINEBROT_TARGET_re;
        this.centerIm = INIT_FINEBROT_TARGET_im;
    }
}
