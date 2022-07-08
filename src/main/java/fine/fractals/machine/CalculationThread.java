package fine.fractals.machine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.machine.ApplicationImpl.*;
import static fine.fractals.machine.FractalEngineImpl.FractalEngine;

public class CalculationThread extends Thread {

    private static final Logger log = LogManager.getLogger(CalculationThread.class);

    private CalculationThread() {
        log.debug("constructor");
    }

    public static void calculate() {
        log.info("calculate");
        CalculationThread thread = new CalculationThread();
        thread.start();
    }

    @Override
    public void run() {
        do {
            log.info("Iteration: " + iteration++);

            FractalEngine.calculate();

            if (REPEAT) {
                Application.zoomIn();
            }

            if (iteration == 1) {
                AreaMandelbrot.moveToInitialCoordinates();
                AreaFinebrot.moveToInitialCoordinates();
            }
            Application.repaintWindows();
        } while (REPEAT);
    }
}
