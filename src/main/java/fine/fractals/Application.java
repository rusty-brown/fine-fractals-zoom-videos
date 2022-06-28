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

import java.awt.image.BufferedImage;

import static fine.fractals.Main.RESOLUTION_IMAGE_HEIGHT;
import static fine.fractals.Main.RESOLUTION_IMAGE_WIDTH;

public class Application {

    public static final double ZOOM = 0.98;
    public static final int TEST_OPTIMIZATION_FIX_SIZE = 4; // is from -it to it.

    public static final String APP_NAME = "_" + Formatter.now();
    public static final String USER_HOME = System.getProperty("user.home");
    private static final Logger log = LogManager.getLogger(Application.class);

    public static int RESOLUTION_DOMAIN_WIDTH = 1000;
    public static int RESOLUTION_DOMAIN_HEIGHT = 1000;
    public static int RESOLUTION_IMAGE_SAVE_FOR = 720;

    public static boolean REPEAT = true;

    /* Increase this only in CalculationThread */
    public static int iteration = 0;

    public static Application ME;

    final BufferedImage designImage = new BufferedImage(RESOLUTION_IMAGE_WIDTH, RESOLUTION_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final ApplicationWindow applicationWindow;
    private final OneTarget target;
    public AreaDomain areaDomain;
    public AreaImage areaImage;

    private final FractalEngine fractalEngine;
    private final FractalWindow fractalWindow;

    public Application() {
        ME = this;

        log.info("Start");

        /* moveToInitialCoordinates immediately */
        this.areaDomain = new AreaDomain(Fractal.INIT_AREA_DOMAIN_SIZE, Fractal.INIT_DOMAIN_TARGET_re, Fractal.INIT_DOMAIN_TARGET_im, RESOLUTION_DOMAIN_WIDTH, RESOLUTION_DOMAIN_HEIGHT);
        this.areaImage = new AreaImage(Fractal.INIT_AREA_IMAGE_SIZE, Fractal.INIT_IMAGE_TARGET_reT, Fractal.INIT_IMAGE_TARGET_imX, RESOLUTION_IMAGE_WIDTH, RESOLUTION_IMAGE_HEIGHT);

        this.target = new OneTarget();
        UIMouseListener uiMouseListener = new UIMouseListener();
        UIKeyDispatcher uiKeyDispatcher = new UIKeyDispatcher();
        UIMouseWheelListener uiMouseWheelListener = new UIMouseWheelListener();

        this.fractalEngine = new FractalEngine(designImage);

        this.applicationWindow = new ApplicationWindow(target, uiMouseListener, uiMouseWheelListener, uiKeyDispatcher, RESOLUTION_DOMAIN_WIDTH, RESOLUTION_DOMAIN_HEIGHT, "Application - " + Fractal.NAME + " - " + APP_NAME);
        this.fractalWindow = new FractalWindow(target, uiMouseListener, uiMouseWheelListener, uiKeyDispatcher, designImage, RESOLUTION_IMAGE_WIDTH, RESOLUTION_IMAGE_HEIGHT, Fractal.NAME + " - " + APP_NAME);

        this.fractalWindow.setApplicationWindow(applicationWindow);
        this.applicationWindow.setDesignWindow(fractalWindow);


        /* set window positions*/
        int scrWidth = 1920;
        int scrHeight = 1080;
        if (RESOLUTION_IMAGE_HEIGHT < scrHeight && RESOLUTION_IMAGE_WIDTH < scrWidth) {
            int panel = 50;
            int borders = 2;
            int top = scrHeight - panel - RESOLUTION_IMAGE_HEIGHT;
            if (top < 0) {
                top = 0;
            }
            fractalWindow.frame.setLocation(scrWidth - (RESOLUTION_DOMAIN_WIDTH + borders), top);
            applicationWindow.frame.setLocation(scrWidth - (2 * (RESOLUTION_DOMAIN_WIDTH + borders)), top);
        }
    }


    public void execute() {
        CalculationThread.calculate();
        CalculationThread.joinMe();
    }

    public void repaint() {
        log.info("repaint");
        this.fractalWindow.frame.repaint();
        this.applicationWindow.frame.repaint();
    }

    public void repaintMandelbrot() {
        applicationWindow.frame.repaint();
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

    public FractalEngine getEngine() {
        return this.fractalEngine;
    }

    public Element getMandelbrotElementAt(int mousePositionT, int mousePositionX) {
        return this.fractalEngine.getMandelbrotElementAt(mousePositionT, mousePositionX);
    }

}
