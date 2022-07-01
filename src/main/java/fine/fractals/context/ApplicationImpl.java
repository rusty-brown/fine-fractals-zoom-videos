package fine.fractals.context;

import fine.fractals.formatter.Formatter;
import fine.fractals.machine.CalculationThread;
import fine.fractals.windows.ApplicationWindow;
import fine.fractals.windows.FractalWindow;
import fine.fractals.windows.dispatcher.UIKeyDispatcher;
import fine.fractals.windows.listener.UIMouseListener;
import fine.fractals.windows.listener.UIMouseWheelListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.FractalEngineImpl.FractalEngine;
import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.context.mandelbrot.MandelbrotImpl.Mandelbrot;

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

    public static int RESOLUTION_IMAGE_SAVE_FOR = 720;

    public static boolean REPEAT = true;

    /* Increase this only in CalculationThread */
    public static int iteration = 0;

    private final ApplicationWindow applicationWindow;
    private final FractalWindow fractalWindow;

    public static ApplicationImpl Application;

    static {
        log.info("init");
        Application = new ApplicationImpl();
    }

    private ApplicationImpl() {
        final UIMouseListener uiMouseListener = new UIMouseListener();
        final UIKeyDispatcher uiKeyDispatcher = new UIKeyDispatcher();
        final UIMouseWheelListener uiMouseWheelListener = new UIMouseWheelListener();

        applicationWindow = new ApplicationWindow(uiMouseListener, uiMouseWheelListener, uiKeyDispatcher);
        fractalWindow = new FractalWindow(uiMouseListener, uiMouseWheelListener, uiKeyDispatcher);
    }

    private void initUIWindows() {
        fractalWindow.setApplicationWindow(applicationWindow);
        applicationWindow.setDesignWindow(fractalWindow);
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
            fractalWindow.frame.setLocation(scrWidth - (RESOLUTION_WIDTH + borders), top);
            applicationWindow.frame.setLocation(scrWidth - (2 * (RESOLUTION_WIDTH + borders)), top);
        }
    }

    public void execute() {
        initUIWindows();

        CalculationThread.calculate();

        startRefreshTimer();

        Mandelbrot.createMask();
        Application.repaintMandelbrotWindow();
    }

    private void startRefreshTimer() {
        final Timer timer = new Timer(2000, evt -> {
            log.info("repaint mandelbrot mask - timer");
            Mandelbrot.createMask();
            Application.repaintMandelbrotWindow();
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.start();
    }

    public void repaintWindows() {
        log.info("repaint");
        this.fractalWindow.frame.repaint();
        this.applicationWindow.frame.repaint();
    }

    public void repaintMandelbrotWindow() {
        applicationWindow.frame.repaint();
    }

    public void zoomIn() {
        log.info("ZOOM IN");
        AreaMandelbrot.zoomIn();
        AreaFinebrot.zoomIn();
        FractalEngine.updateDomain();
    }
}