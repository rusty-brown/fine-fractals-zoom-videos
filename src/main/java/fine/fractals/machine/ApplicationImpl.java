package fine.fractals.machine;

import fine.fractals.data.annotation.EditMe;
import fine.fractals.formatter.Formatter;
import fine.fractals.windows.FinebrotWindow;
import fine.fractals.windows.MandelbrotWindow;
import fine.fractals.windows.dispatcher.UIKeyDispatcher;
import fine.fractals.windows.listener.UIMouseListener;
import fine.fractals.windows.listener.UIMouseWheelListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_WIDTH;
import static fine.fractals.fractal.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.machine.FractalEngineImpl.FractalEngine;
import static java.awt.Frame.ICONIFIED;

public class ApplicationImpl {

    private static final Logger log = LogManager.getLogger(ApplicationImpl.class);

    public static final String USER_HOME = System.getProperty("user.home");
    /**
     * Create the folder in your home directory or change the path
     */
    @EditMe
    public static final String FILE_PATH = USER_HOME + "/Fractals/";

    @EditMe
    public static final String DEBUG_PATH = USER_HOME + "/Fractals-debug/";

    @EditMe
    public static final boolean IGNORE_DEBUG_FILES = true;
    /**
     * How many pixels round specific element will be investigated for optimization.
     * If there is nothing interesting going on around specific pixel, the pixel will be ignored.
     */
    public static final int neighbours = 3;
    public static final double ZOOM = 0.98;
    public static final String APP_NAME = "_" + Formatter.now();
    public static final int coloringThreshold = 2;

    /**
     * Singleton instance
     */
    public static final ApplicationImpl Application;
    public static int COREs = Runtime.getRuntime().availableProcessors() - 1;
    public static boolean REPEAT = true;
    /* Increase this only in FractalEngine */
    public static int iteration = 0;
    /* identify zero and low-value elements as zero or noise */

    static {
        log.debug("init");
        Application = new ApplicationImpl();

        if (COREs < 1) {
            COREs = 1;
        }
        log.info("cores: " + COREs);
    }

    private MandelbrotWindow mandelbrotWindow;
    private FinebrotWindow finebrotWindow;

    private ApplicationImpl() {
        log.debug("constructor");
    }

    public void initUIWindows() {
        log.debug("listeners");
        final UIMouseListener uiMouseListener = new UIMouseListener();
        final UIKeyDispatcher uiKeyDispatcher = new UIKeyDispatcher();
        final UIMouseWheelListener uiMouseWheelListener = new UIMouseWheelListener();

        log.debug("windows");
        mandelbrotWindow = new MandelbrotWindow(uiMouseListener, uiMouseWheelListener, uiKeyDispatcher);
        finebrotWindow = new FinebrotWindow(uiMouseListener, uiMouseWheelListener, uiKeyDispatcher);

        log.debug("initUIWindows()");
        finebrotWindow.setMandelbrotWindow(mandelbrotWindow);
        mandelbrotWindow.setFinebrotWindow(finebrotWindow);

        /* screen resolution */
        final int scrWidth = 2560;
        final int scrHeight = 1440;
        /* set window positions */
        int panel = 50;
        int borders = 2;
        if (RESOLUTION_HEIGHT < scrHeight && RESOLUTION_WIDTH < scrWidth) {
            int top = scrHeight - panel - RESOLUTION_HEIGHT;
            if (top < 0) {
                top = 0;
            }
            finebrotWindow.frame.setLocation(scrWidth - (RESOLUTION_WIDTH + borders), top);
            mandelbrotWindow.frame.setLocation(scrWidth - (2 * (RESOLUTION_WIDTH + borders)), top);
        }
        if (2 * RESOLUTION_WIDTH > scrWidth) {
            finebrotWindow.frame.setState(ICONIFIED);
            mandelbrotWindow.frame.setLocation((scrWidth - (RESOLUTION_WIDTH + borders)) / 2, 0);
        }
    }

    public void execute() {
        log.debug("execute()");
        initUIWindows();
        FractalEngine.start();
    }

    public void repaintFinebrotWindow() {
        log.debug("repaintFinebrotWindow()");
        finebrotWindow.frame.repaint();
    }

    public void repaintMandelbrotWindow() {
        log.debug("repaintMandelbrotWindow()");
        mandelbrotWindow.frame.repaint();
    }

    public void zoomIn() {
        log.debug("zoomIn()");
        AreaMandelbrot.zoomIn();
        AreaFinebrot.zoomIn();
    }
}
