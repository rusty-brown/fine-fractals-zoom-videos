package fine.fractals.engine;

import fine.fractals.Application;
import fine.fractals.data.objects.Data;
import fine.fractals.ui.UIRefreshThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CalculationThread extends Thread {

    public static CalculationThread ME;
    private static final Logger log = LogManager.getLogger(CalculationThread.class);
    private static String message;

    private CalculationThread() {
        ME = this;
    }

    synchronized public static void calculate() {
        CalculationThread thread = new CalculationThread();
        thread.start();
        Application.ME.repaint();
    }

    public static void joinMe() {
        try {
            ME.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        do {
            Data.archive();
            log.info("Iteration: " + Application.iteration++);

            UIRefreshThread.runRefreshThread();

            /* Calculate Fractal values */
            FractalEngine.ME.calculateFromThread();

            if (Application.REPEAT) {
                Application.ME.zoomIn();
                Application.ME.repaint();
            }
            if (Application.iteration == 1) {
                /* Move to coordinates after initialization of FractalEngine, Mandelbrot initial domain */
                Application.ME.areaDomain.moveToInitialCoordinates();
                Application.ME.areaImage.moveToInitialCoordinates();
            }

        } while (Application.REPEAT);

        if (message != null) {
            log.info(message);
        }
    }
}
