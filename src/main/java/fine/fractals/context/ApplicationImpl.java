package fine.fractals.context;

import fine.fractals.formatter.Formatter;
import fine.fractals.machine.CalculationThread;
import fine.fractals.windows.FinebrotWindow;
import fine.fractals.windows.MandelbrotWindow;
import fine.fractals.windows.dispatcher.UIKeyDispatcher;
import fine.fractals.windows.listener.UIMouseListener;
import fine.fractals.windows.listener.UIMouseWheelListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.FractalEngineImpl.FractalEngine;
import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;

public class ApplicationImpl {

    public static final double ZOOM = 0.98;
    /*
     * Distance in px around convergent element.
     * Dead pixels around divergent pixel elements will be recalculated.
     * Optimization mechanism will break for less than 4
     * Distance around is +- 4
     * This value may need to be increased for more complicated fractals
     */
    public static final int TEST_OPTIMIZATION_FIX_SIZE = 4;

    public static final String APP_NAME = "_" + Formatter.now();
    public static final String USER_HOME = System.getProperty("user.home");
    private static final Logger log = LogManager.getLogger(ApplicationImpl.class);


    public static boolean REPEAT = true;

    /* Increase this only in CalculationThread */
    public static int iteration = 0;

    private MandelbrotWindow mandelbrotWindow;
    private FinebrotWindow finebrotWindow;

    public static final ApplicationImpl Application;

    static {
        log.info("init");
        Application = new ApplicationImpl();
    }

    private ApplicationImpl() {
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

        /* set window positions*/
        int scrWidth = 1920;
        int scrHeight = 1080;
        if (RESOLUTION_HEIGHT < scrHeight && RESOLUTION_WIDTH < scrWidth) {
            int panel = 50;
            int borders = 2;
            int top = scrHeight - panel - RESOLUTION_HEIGHT;
            if (top < 0) {
                top = 0;
            }
            finebrotWindow.frame.setLocation(scrWidth - (RESOLUTION_WIDTH + borders), top);
            mandelbrotWindow.frame.setLocation(scrWidth - (2 * (RESOLUTION_WIDTH + borders)), top);
        }
    }

    public void execute() {
        log.debug("execute()");
        CalculationThread.calculate();
    }

    public void repaintWindows() {
        log.info("repaintWindows()");
        this.finebrotWindow.frame.repaint();
        this.mandelbrotWindow.frame.repaint();
    }

    public void repaintMandelbrotWindow() {
        log.debug("repaintMandelbrotWindow()");
        mandelbrotWindow.frame.repaint();
    }

    public void zoomIn() {
        log.info("zoomIn()");
        AreaMandelbrot.zoomIn();
        AreaFinebrot.zoomIn();
        FractalEngine.updateDomain();
    }
}
