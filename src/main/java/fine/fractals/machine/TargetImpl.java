package fine.fractals.machine;

import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.data.mem.Mem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_WIDTH;
import static fine.fractals.fractal.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.fractal.mandelbrot.MandelbrotImpl.Mandelbrot;

public class TargetImpl {

    private static final Logger log = LogManager.getLogger(TargetImpl.class);

    private String cTextRe = "";
    private String cTextIm = "";

    /* Coordinates counted from top left corner */
    private int scrRe;
    private int scrIm;
    private int scrCornerRe;
    private int scrCornerIm;

    private MandelbrotElement mandelbrotElement = null;

    public static final TargetImpl Target = new TargetImpl();

    private TargetImpl() {
        log.debug("constructor");
    }

    public void update(int mousePositionX, int mousePositionY) {
        this.scrCornerRe = mousePositionX;
        this.scrCornerIm = mousePositionY;
        this.scrRe = this.scrCornerRe - (RESOLUTION_WIDTH / 2);
        this.scrIm = this.scrCornerIm - (RESOLUTION_HEIGHT / 2);

        Mem m = new Mem();
        if (!FractalEngineImpl.calculationInProgress) {
            String[] tmp = AreaMandelbrot.cToString(m, this.scrCornerRe, this.scrCornerIm);
            this.cTextRe = tmp[0];
            this.cTextIm = tmp[1];
            mandelbrotElement = Mandelbrot.getElementAt(mousePositionX, mousePositionY);
        } else {
            mandelbrotElement = null;
            try {
                String[] tmp = AreaMandelbrot.cToString(m, this.scrCornerRe, this.scrCornerIm);
                this.cTextRe = tmp[0];
                this.cTextIm = tmp[1];
            } catch (Exception e) {
                log.info("OneTarget.update(" + mousePositionX + ", " + mousePositionY + "): " + e.getMessage());
                this.cTextRe = "-";
                this.cTextIm = "-";
            }
        }
        /* Don't move AreaMandelbrot or AreaFinebrot - I may want to zoom in with space bar */
    }

    public int getScreenFromCenterX() {
        return this.scrRe;
    }

    public int getScreenFromCenterY() {
        return this.scrIm;
    }

    public int getScreenFromCornerX() {
        return this.scrCornerRe;
    }

    public int getScreenFromCornerY() {
        return this.scrCornerIm;
    }

    public String getTextRe() {
        return this.cTextRe;
    }

    public String getTextIm() {
        return this.cTextIm;
    }

    public String getMandelbrotValue() {
        if (this.mandelbrotElement != null) {
            try {
                int value = this.mandelbrotElement.getValue();
                return String.valueOf(value);
            } catch (NullPointerException ex) {
                /* expect int corrected = null */
                log.fatal("getMandelbrotValue() null");
            }
        }
        return "-";
    }

    public String getMandelbrotState() {
        if (this.mandelbrotElement != null) {
            return this.mandelbrotElement.getState().toString();
        }
        return "-";
    }

    public double re() {
        return scrRe;
    }
}
