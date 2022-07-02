package fine.fractals.context;

import fine.fractals.data.MandelbrotElement;
import fine.fractals.data.Mem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.context.mandelbrot.MandelbrotImpl.Mandelbrot;

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

    public static final TargetImpl Target;

    static {
        log.info("init");
        Target = new TargetImpl();
    }

    private TargetImpl() {
    }

    public void update(int mousePositionX, int mousePositionY) {
        this.scrCornerRe = mousePositionX;
        this.scrCornerIm = mousePositionY;
        this.scrRe = this.scrCornerRe - (RESOLUTION_WIDTH / 2);
        this.scrIm = this.scrCornerIm - (RESOLUTION_HEIGHT / 2);

        Mem mem = new Mem();

        if (!FractalEngineImpl.calculationInProgress) {
            String[] tmp = AreaMandelbrot.cToString(mem, this.scrCornerRe, this.scrCornerIm);
            this.cTextRe = tmp[0];
            this.cTextIm = tmp[1];
            mandelbrotElement = Mandelbrot.getElementAt(mousePositionX, mousePositionY);
        } else {
            mandelbrotElement = null;
            try {
                String[] tmp = AreaMandelbrot.cToString(mem, this.scrCornerRe, this.scrCornerIm);
                this.cTextRe = tmp[0];
                this.cTextIm = tmp[1];
            } catch (Exception e) {
                log.info("OneTarget.update(" + mousePositionX + ", " + mousePositionY + "): " + e.getMessage());
                this.cTextRe = "-";
                this.cTextIm = "-";
            }
        }
        /* I don't want to Move area domain and area image coordinates in case I want just Zoom in with Space */
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
