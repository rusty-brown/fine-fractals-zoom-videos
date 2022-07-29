package fine.fractals.fractal.finebrot.common;

import fine.fractals.data.mem.Mem;

import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.RESOLUTION_WIDTH;

public abstract class AreaAbstract {

    /* identify center of area */
    protected final int resolutionHalfRe;
    protected final int resolutionHalfIm;
    /* position of the centre of domain area */
    public double centerRe;
    public double centerIm;
    /* size of domain area */
    public double sizeRe;
    public double sizeIm;

    protected AreaAbstract() {
        this.resolutionHalfRe = RESOLUTION_WIDTH / 2;
        this.resolutionHalfIm = RESOLUTION_HEIGHT / 2;
    }

    public void pointToPixel(Mem m, double re, double im) {
        m.good = true;
        m.px = (int) Math.round((RESOLUTION_WIDTH * (re - this.centerRe) / this.sizeRe) + resolutionHalfRe);
        if (m.px >= RESOLUTION_WIDTH || m.px < 0) {
            m.good = false;
            return;
        }
        m.py = (int) Math.round(((RESOLUTION_HEIGHT * (im - this.centerIm)) / this.sizeIm) + resolutionHalfIm);
        if (m.py >= RESOLUTION_HEIGHT || m.py < 0) {
            m.good = false;
        }
    }
}
