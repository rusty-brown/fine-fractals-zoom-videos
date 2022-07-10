package fine.fractals.fractal.mandelbrot;

import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.machine.concurent.CalculationPathThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.FinebrotFractal;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.PathsFinebrot;
import static fine.fractals.fractal.mandelbrot.PixelsMandelbrotImpl.PixelsMandelbrot;
import static fine.fractals.machine.ApplicationImpl.Application;
import static fine.fractals.machine.ApplicationImpl.COREs;
import static java.util.concurrent.TimeUnit.MINUTES;

public class MandelbrotImpl {

    public static final MandelbrotImpl Mandelbrot = new MandelbrotImpl();
    private static final Logger log = LogManager.getLogger(MandelbrotImpl.class);

    private MandelbrotImpl() {
        log.debug("constructor");
    }

    /*
     * Calculate Domain Values
     */
    public void calculate() {
        log.info("calculate()");

        final ArrayList<ArrayList<MandelbrotElement>> domainFullChunkedAndWrapped = PixelsMandelbrot.fetchDomainWrappedParts();

        Collections.shuffle(domainFullChunkedAndWrapped);

        log.debug("calculate: " + domainFullChunkedAndWrapped.size() + " chunks");

        final ExecutorService executor = Executors.newFixedThreadPool(COREs);

        for (ArrayList<MandelbrotElement> part : domainFullChunkedAndWrapped) {
            executor.execute(new CalculationPathThread(part));
        }

        try {
            executor.shutdown();
            /* wait maximum 1 hour for frame to finish */
            boolean terminated = executor.awaitTermination(59, MINUTES);
            if (terminated) {
                log.info("ExecutorService is terminated");
            } else {
                log.fatal("ExecutorService NOT terminated");

                /*  1 hour */
                int countOneHour = 60 * 60;
                while (!executor.isTerminated()) {
                    log.info("ExecutorService not terminated <- " + countOneHour);
                    // wait 1s
                    Thread.sleep(1000);
                    countOneHour--;
                }
            }
        } catch (InterruptedException e) {
            log.error("Executor waiting interrupted.");
            System.exit(1);
        }

        PathsFinebrot.domainToScreenGrid();
        FinebrotFractal.update();
    }

    /* Used for OneTarget */
    public MandelbrotElement getElementAt(int x, int y) {
        try {
            return PixelsMandelbrot.elementsStaticMandelbrot[x][y];
        } catch (Exception e) {
            log.fatal("getElementAt()", e);
            return null;
        }
    }

    public void createMaskAndRepaint() {
        boolean change = PixelsMandelbrot.createMask();
        if (change) {
            Application.repaintMandelbrotWindow();
        }
    }

    public void domainForThisZoom() {
        PixelsMandelbrot.domainForThisZoom();
    }

    public void domainScreenCreateInitialization() {
        PixelsMandelbrot.domainScreenCreateInitialization();
    }
}
