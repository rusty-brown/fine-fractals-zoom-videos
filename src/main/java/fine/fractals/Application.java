package fine.fractals;

import fine.fractals.engine.CalculationThread;
import fine.fractals.engine.FractalEngine;
import fine.fractals.fractal.Fractal;
import fine.fractals.math.AreaDomain;
import fine.fractals.math.AreaImage;
import fine.fractals.math.common.Element;
import fine.fractals.ui.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Application {

    public static final double ZOOM = 0.98;
    public static final int TEST_OPTIMIZATION_FIX_SIZE = 4; // is from -it to it.

    public static final String APP_NAME = "_" + Formatter.now();
    public static final String USER_HOME = System.getProperty("user.home");
    private static final Logger log = LogManager.getLogger(Application.class);

    public static int RESOLUTION_DOMAIN_WIDTH = 1000;
    public static int RESOLUTION_DOMAIN_HEIGHT = 1000;
    public static int RESOLUTION_IMAGE_SAVE_FOR = 720;
    /**
     * To Render Images for a ZOOM Video.
     * 1.
     * set REPEAT = true
     * Don't use any fractals with optimization.
     * Try FineMandelbrotZoom()
     * <p>
     * 2.
     * set DESIGN_STATIC = Boolean.FALSE; To remember all the element paths calculated in the previous iterations
     */
    public static boolean REPEAT = true;

    /**
     * Use:
     * - TRUE to make images
     * - FALSE to make videos
     */
    public static boolean DESIGN_STATIC = Boolean.FALSE;

    public static int TIME_OUT = 100; // ms
    /* Increase this only in CalculationThread */
    public static int iteration = 0;
    public static boolean addText = false;

    public static Application ME;

    final BufferedImage designImage = new BufferedImage(Main.RESOLUTION_IMAGE_WIDTH, Main.RESOLUTION_IMAGE_HIGHT, BufferedImage.TYPE_INT_RGB);
    private final ApplicationWindow applicationWindow;
    private final OneTarget target;
    public AreaDomain areaDomain;
    public AreaImage areaImage;
    private boolean drawRectangle = false;
    private FractalEngine fractalEngine;
    private FractalWindow fractalWindow;

    public Application() {
        ME = this;

        log.info("Start");

        /* moveToInitialCoordinates immediately */
        this.areaDomain = new AreaDomain(Fractal.INIT_AREA_DOMAIN_SIZE, Fractal.INIT_DOMAIN_TARGET_re, Fractal.INIT_DOMAIN_TARGET_im, RESOLUTION_DOMAIN_WIDTH, RESOLUTION_DOMAIN_HEIGHT);
        this.areaImage = new AreaImage(Fractal.INIT_AREA_IMAGE_SIZE, Fractal.INIT_IMAGE_TARGET_reT, Fractal.INIT_IMAGE_TARGET_imX, Main.RESOLUTION_IMAGE_WIDTH, Main.RESOLUTION_IMAGE_HIGHT);

        this.target = new OneTarget();
        UIMouseListener uiMouseListener = new UIMouseListener();
        UIKeyDispatcher uiKeyDispatcher = new UIKeyDispatcher();
        UIMouseWheelListener uiMouseWheelListener = new UIMouseWheelListener();

        this.fractalEngine = new FractalEngine(designImage);

        this.applicationWindow = new ApplicationWindow(target, uiMouseListener, uiMouseWheelListener, uiKeyDispatcher, RESOLUTION_DOMAIN_WIDTH, RESOLUTION_DOMAIN_HEIGHT, "Application - " + Fractal.NAME + " - " + APP_NAME);
        this.fractalWindow = new FractalWindow(target, uiMouseListener, uiMouseWheelListener, uiKeyDispatcher, designImage, Main.RESOLUTION_IMAGE_WIDTH, Main.RESOLUTION_IMAGE_HIGHT, Fractal.NAME + " - " + APP_NAME);

        this.fractalWindow.setApplicationWindow(applicationWindow);
        this.applicationWindow.setDesignWindow(fractalWindow);


        /* Minimize windows */
        if (Main.RESOLUTION_IMAGE_HIGHT > 1000) {
            log.info("== hide fractal window");
            this.fractalWindow.frame.setState(Frame.ICONIFIED);
        }

        /* set window positions*/
        int scrWidth = 1920;
        int scrHeight = 1080;
        if (Main.RESOLUTION_IMAGE_HIGHT < scrHeight && Main.RESOLUTION_IMAGE_WIDTH < scrWidth) {
            int panel = 50;
            int borders = 2;
            int top = scrHeight - panel - Main.RESOLUTION_IMAGE_HIGHT;
            if (top < 0) {
                top = 0;
            }
            fractalWindow.frame.setLocation(scrWidth - (RESOLUTION_DOMAIN_WIDTH + borders), top);
            applicationWindow.frame.setLocation(scrWidth - (2 * (RESOLUTION_DOMAIN_WIDTH + borders)), top);
        }
    }


    public void execute() {
        CalculationThread.calculate(0);
        CalculationThread.joinMe();
    }

    /*********************************************************************************************/

    public boolean getDrawRectangle() {
        return this.drawRectangle;
    }

    public void toggleRectangle() {
        this.drawRectangle = !this.drawRectangle;
    }

    public void repaint() {
        this.fractalWindow.frame.repaint();
        this.applicationWindow.frame.repaint();
    }

    public void repaintMandelbrot() {
        applicationWindow.frame.repaint();
    }

    public void dispose() {
        this.fractalWindow.frame.dispose();
        this.applicationWindow.frame.dispose();
    }

    public OneTarget getTarget() {
        return this.target;
    }


    public void zoomIn() {
        log.info("ZOOM IN");
        this.areaDomain.zoomIn();
        this.areaImage.zoomIn();
        this.fractalEngine.updateDomain();
    }

    public void zoomOut() {
        log.info("ZOOM OUT");
        this.areaImage.zoomOut();
        this.areaDomain.zoomOut();
        this.fractalEngine.updateDomain();
    }

    public FractalEngine getEngine() {
        return this.fractalEngine;
    }

    public Element getMandelbrotElementAt(int mousePositionT, int mousePositionX) {
        return this.fractalEngine.getMandelbrotElementAt(mousePositionT, mousePositionX);
    }

}
