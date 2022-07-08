package fine.fractals.fractal.mandelbrot;

import fine.fractals.data.MandelbrotElement;
import fine.fractals.data.misc.Bool;
import fine.fractals.machine.FractalMachine;
import fine.fractals.machine.concurent.PathThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static fine.fractals.context.ApplicationImpl.*;
import static fine.fractals.context.TargetImpl.Target;
import static fine.fractals.fractal.finebrot.common.FinebrotFractalImpl.FinebrotFractal;
import static fine.fractals.fractal.finebrot.common.FinebrotFractalImpl.PathsFinebrot;
import static fine.fractals.fractal.mandelbrot.PixelsMandelbrotImpl.PixelsMandelbrot;
import static java.util.concurrent.TimeUnit.MINUTES;

public class MandelbrotImpl {

    private static final Logger log = LogManager.getLogger(MandelbrotImpl.class);

    public static final MandelbrotImpl Mandelbrot = new MandelbrotImpl();

    private MandelbrotImpl() {
    }

    /*
     * Calculate Domain Values
     */
    public void calculate() {

        log.info("calculate()");
        final ArrayList<MandelbrotElement> domainFull = PixelsMandelbrot.fetchDomainFull();

        log.info("calculate: " + domainFull.size() + ", which is full domain");
        final ExecutorService executor = Executors.newFixedThreadPool(COREs);

        log.info("Start " + domainFull.size() + " threads");
        for (MandelbrotElement el : domainFull) {
            executor.execute(new PathThread(el));
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
        log.info("ExecutorService end.");

        PathsFinebrot.domainToScreenGrid();
        FinebrotFractal.update();

        log.info("calculate() finished");
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

    public void fixOptimizationBreak() {
        log.info("fixOptimizationBreak()");

        /* Last tested pixel is Hibernated as Converged (Calculation finished) */
        Bool lastIsWhite = new Bool();
        /* Last tested pixel is Hibernated as Skipped for calculation (Deep black) */
        Bool lastIsBlack = new Bool();
        ArrayList<Integer> failedNumbersRe = new ArrayList<>();
        ArrayList<Integer> failedNumbersIm = new ArrayList<>();
        /* Test lines left and right */
        for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
            for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
                FractalMachine.testOptimizationBreakElement(xx, yy, PixelsMandelbrot.elementsStaticMandelbrot[xx][yy], failedNumbersRe, failedNumbersIm, lastIsWhite, lastIsBlack);
            }
            lastIsBlack.setFalse();
            lastIsWhite.setFalse();
        }
        /* Test lines up and down */
        for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
            for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
                FractalMachine.testOptimizationBreakElement(xx, yy, PixelsMandelbrot.elementsStaticMandelbrot[xx][yy], failedNumbersRe, failedNumbersIm, lastIsWhite, lastIsBlack);
            }
            lastIsBlack.setFalse();
            lastIsWhite.setFalse();
        }
        /* Fix failed positions */
        /* In worst case failed positions contains same position twice */
        int size = failedNumbersRe.size();
        for (int i = 0; i < size; i++) {
            final int r = TEST_OPTIMIZATION_FIX_SIZE;
            for (int x = -r; x < r; x++) {
                for (int y = -r; y < r; y++) {
                    if ((x * x) + (y * y) < (r * r)) {
                        /* These optimizations should be much better optimized. This touches points which were already fixed. */
                        FractalMachine.setActiveMovedIfBlack(failedNumbersRe.get(i) + x, failedNumbersIm.get(i) + y, PixelsMandelbrot.elementsStaticMandelbrot);
                    }
                }
            }
        }
    }

    public void fixDomainOptimizationOnClick() {
        int xx = Target.getScreenFromCenterX();
        int yy = Target.getScreenFromCenterY();
        final int r = neighbours;
        for (int x = -r; x < r; x++) {
            for (int y = -r; y < r; y++) {
                if ((x * x) + (y * y) < (r * r)) {
                    FractalMachine.setActiveToAddToCalculation(xx + x, yy + y, PixelsMandelbrot.elementsStaticMandelbrot);
                }
            }
        }
    }

    public void createMask() {
        PixelsMandelbrot.createMask();
    }

    public void domainForThisZoom() {
        PixelsMandelbrot.domainForThisZoom();
    }

    public void domainScreenCreateInitialization() {
        PixelsMandelbrot.domainScreenCreateInitialization();
    }
}
	
