package fine.fractals.context;

import fine.fractals.data.Element;
import fine.fractals.data.Mem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.context.mandelbrot.MandelbrotImpl.Mandelbrot;

public class TargetImpl {

    private static final Logger log = LogManager.getLogger(TargetImpl.class);

    private String cTextReT = "";
    private String cTextImX = "";
    /* Coordinates counted from top left corner */
    private int scrT;
    private int scrX;
    private int scrCornerT;
    private int scrCornerX;

    private Element mandelbrotElement = null;

    public static TargetImpl Target;

    static {
        log.info("init");
        Target = new TargetImpl();
    }

    private TargetImpl() {
    }

    public void update(int mousePositionT, int mousePositionX) {
        this.scrCornerT = mousePositionT;
        this.scrCornerX = mousePositionX;
        this.scrT = this.scrCornerT - (RESOLUTION_WIDTH / 2);
        this.scrX = this.scrCornerX - (RESOLUTION_HEIGHT / 2);

        Mem mem = new Mem();

        if (!FractalEngineImpl.calculationInProgress) {
            String[] tmp = AreaMandelbrot.cToString(mem, this.scrCornerT, this.scrCornerX);
            this.cTextReT = tmp[0];
            this.cTextImX = tmp[1];
            mandelbrotElement = Mandelbrot.getElementAt(mousePositionT, mousePositionX);
        } else {
            mandelbrotElement = null;
            try {
                String[] tmp = AreaMandelbrot.cToString(mem, this.scrCornerT, this.scrCornerX);
                this.cTextReT = tmp[0];
                this.cTextImX = tmp[1];
            } catch (Exception e) {
                log.info("OneTarget.update(" + mousePositionT + ", " + mousePositionX + "): " + e.getMessage());
                this.cTextReT = "-";
                this.cTextImX = "-";
            }
        }
        /* I don't want to Move area domain and area image coordinates in case I want just Zoom in with Space */
    }

    public int getScreenFromCenterT() {
        return this.scrT;
    }

    public int getScreenFromCenterX() {
        return this.scrX;
    }

    public int getScreenFromCornerT() {
        return this.scrCornerT;
    }

    public int getScreenFromCornerX() {
        return this.scrCornerX;
    }

    public String getTextReT() {
        return this.cTextReT;
    }

    public String getTextImX() {
        return this.cTextImX;
    }

    public String getMandelbrotValue() {
        if (this.mandelbrotElement != null) {
            try {
                int value = this.mandelbrotElement.getValue();
                return String.valueOf(value);
            } catch (NullPointerException ex) {
                /* expect int corrected = null */
                // Time.m("Exception: MouseMove -> OneTarget -> getMandelbrotValue - There probably was a calculation exception.");
                log.info(".");
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
}


