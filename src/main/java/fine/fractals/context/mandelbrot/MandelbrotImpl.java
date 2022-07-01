package fine.fractals.context.mandelbrot;

import fine.fractals.Main;
import fine.fractals.machine.FractalMachine;
import fine.fractals.concurent.PathThread;
import fine.fractals.data.MandelbrotElement;
import fine.fractals.data.misc.Bool;
import fine.fractals.fractal.Fractal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.ApplicationImpl.TEST_OPTIMIZATION_FIX_SIZE;
import static fine.fractals.context.FractalEngineImpl.calculationProgress;
import static fine.fractals.context.TargetImpl.Target;
import static fine.fractals.context.finebrot.DomainFinebrotImpl.DomainFinebrot;
import static fine.fractals.context.mandelbrot.DomainMandelbrotImpl.DomainMandelbrot;

public class MandelbrotImpl {

    private static final Logger log = LogManager.getLogger(MandelbrotImpl.class);

    public static MandelbrotImpl Mandelbrot;

    /*
     * MANDELBROT FRACTAL IS THE MandelbrotDomain
     */
    private MandelbrotImpl() {
    }

    static {
        log.info("init");
        Mandelbrot = new MandelbrotImpl();
    }

    /*
     * Calculate Domain Values
     */
    public void calculate() {
        log.info("calculate()");

        ArrayList<MandelbrotElement> domainPart;

        int index = 0;
        while (DomainMandelbrot.domainNotFinished) {
            domainPart = DomainMandelbrot.fetchDomainPart();

            log.info("calculate: " + domainPart.size() + ", domain part remains: " + DomainMandelbrot.domainNotFinished);

            final ExecutorService executor = Executors.newFixedThreadPool(Main.COREs);

            log.info("Start " + domainPart.size() + " threads");
            for (MandelbrotElement el : domainPart) {
                executor.execute(new PathThread(index++, el));
            }

            log.info("ExecutorService shut down");
            try {
                executor.shutdown();
                while (!executor.isTerminated()) {
                    log.info("ExecutorService not terminated");
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("ExecutorService shut down OK.");
        }
        DomainFinebrot.domainToScreenGrid();

        calculationProgress = "";

        DomainMandelbrot.domainNotFinished = true;

        Fractal.update();

        log.info("calculate() finished");
    }

    public void resetOptimizationSoft() {
        MandelbrotElement element;
        for (int t = 0; t < RESOLUTION_WIDTH; t++) {
            for (int x = 0; x < RESOLUTION_HEIGHT; x++) {
                element = DomainMandelbrot.elementsScreen[t][x];
                if (element.isHibernatedBlack() || element.isHibernatedBlack_Neighbour()) {
                    element.resetForOptimization();
                }
            }
        }
    }

    public void resetOptimizationHard() {
        // Application.colorPaletteMandelbrot.reset();
        MandelbrotElement element;
        for (int t = 0; t < RESOLUTION_WIDTH; t++) {
            for (int x = 0; x < RESOLUTION_HEIGHT; x++) {
                element = DomainMandelbrot.elementsScreen[t][x];
                if (!element.isActiveMoved()
                        && !element.isHibernatedFinished()
                        && !element.isHibernatedFinishedInside()) {
                    element.resetAsNew();
                }
            }
        }
    }

    /* Used for OneTarget */
    public MandelbrotElement getElementAt(int t, int x) {
        try {
            return DomainMandelbrot.elementsScreen[t][x];
        } catch (Exception e) {
            log.fatal("getElementAt()", e);
            return null;
        }
    }

    public void fixOptimizationBreak() {

        log.info(" === fixOptimizationBreak ===");

        /* Last tested pixel is Hibernated as Converged (Calculation finished) */
        Bool lastIsWhite = new Bool();
        /* Last tested pixel is Hibernated as Skipped for calculation (Deep black) */
        Bool lastIsBlack = new Bool();
        ArrayList<Integer> failedNumbersRe = new ArrayList<>();
        ArrayList<Integer> failedNumbersIm = new ArrayList<>();
        /* Test lines left and right */
        for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
            for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
                FractalMachine.testOptimizationBreakElement(xx, yy, DomainMandelbrot.elementsScreen[xx][yy], failedNumbersRe, failedNumbersIm, lastIsWhite, lastIsBlack);
            }
            lastIsBlack.setFalse();
            lastIsWhite.setFalse();
        }
        /* Test lines up and down */
        for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
            for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
                FractalMachine.testOptimizationBreakElement(xx, yy, DomainMandelbrot.elementsScreen[xx][yy], failedNumbersRe, failedNumbersIm, lastIsWhite, lastIsBlack);
            }
            lastIsBlack.setFalse();
            lastIsWhite.setFalse();
        }
        /* Fix failed positions */
        /* In worst case failed positions contains same position twice */
        int size = failedNumbersRe.size();
        for (int i = 0; i < size; i++) {
            // Time.now("FIXING: " + position.x + ". " + position.y);
            final int r = TEST_OPTIMIZATION_FIX_SIZE;
            for (int x = -r; x < r; x++) {
                for (int y = -r; y < r; y++) {
                    if ((x * x) + (y * y) < (r * r)) {
                        // These thing should be much optimized to not do same for points it was already done
                        FractalMachine.setActiveMovedIfBlack(failedNumbersRe.get(i) + x, failedNumbersIm.get(i) + y, DomainMandelbrot.elementsScreen);
                    }
                }
            }
        }
    }

    public void fixDomainOptimizationOnClick() {
        int xx = Target.getScreenFromCenterT();
        int yy = Target.getScreenFromCenterX();
        final int r = Main.neighbours;
        for (int x = -r; x < r; x++) {
            for (int y = -r; y < r; y++) {
                if ((x * x) + (y * y) < (r * r)) {
                    FractalMachine.setActiveToAddToCalculation(xx + x, yy + y, DomainMandelbrot.elementsScreen);
                }
            }
        }
    }

    public void createMask() {
        DomainMandelbrot.createMask();
    }

    public void domainForThisZoom() {
        DomainMandelbrot.domainForThisZoom();
    }
}
	
