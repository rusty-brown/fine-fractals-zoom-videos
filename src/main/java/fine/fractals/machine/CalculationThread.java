package fine.fractals.machine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.context.ApplicationImpl.*;
import static fine.fractals.context.FractalEngineImpl.FractalEngine;
import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;

public class CalculationThread extends Thread {

    private static final Logger log = LogManager.getLogger(CalculationThread.class);

    private CalculationThread() {
        log.info("init");
    }

    public static void calculate() {
        log.info("calculate");
        CalculationThread thread = new CalculationThread();
        thread.start();
        Application.repaintWindows();
    }

    @Override
    public void run() {
        log.info("run");
        do {
            log.info("Iteration: " + iteration++);

            /* Calculate Fractal values */
            FractalEngine.calculate();

            if (REPEAT) {
                Application.zoomIn();
                Application.repaintWindows();
            }
            if (iteration == 1) {
                /* Move to initial target coordinate after initialization of FractalEngine */
                AreaMandelbrot.moveToInitialCoordinates();
                AreaFinebrot.moveToInitialCoordinates();
            }

        } while (REPEAT);
    }
}
