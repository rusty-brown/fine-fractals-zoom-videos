package fine.fractals.fractal.mandelbrot;

import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.fractal.finebrot.common.FinebrotCpu;
import fine.fractals.machine.concurent.CalculationPathThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static fine.fractals.fractal.mandelbrot.PixelsMandelbrotImpl.PixelsMandelbrot;
import static fine.fractals.machine.ApplicationImpl.COREs;
import static java.util.concurrent.TimeUnit.MINUTES;

public class MandelbrotImpl extends MandelbrotCommonImpl {

    private static final Logger log = LogManager.getLogger(MandelbrotImpl.class);

    private final FinebrotCpu finebrotFractal;

    public MandelbrotImpl(FinebrotCpu finebrotFractal) {
        log.debug("constructor");
        this.finebrotFractal = finebrotFractal;
    }

    /*
     * Calculate Domain Values
     */
    @Override
    public void calculate() {
        log.debug("calculate()");

        final ArrayList<ArrayList<MandelbrotElement>> domainFullChunkedAndWrapped = PixelsMandelbrot.fullDomainAsWrappedParts();
        Collections.shuffle(domainFullChunkedAndWrapped);

        final ExecutorService executor = Executors.newFixedThreadPool(COREs);
        for (ArrayList<MandelbrotElement> part : domainFullChunkedAndWrapped) {
            /*
             * Calculate independently each domain chunk
             */
            executor.execute(new CalculationPathThread(finebrotFractal, part));
        }

        try {
            executor.shutdown();
            /* wait maximum 1 hour for frame to finish */
            boolean terminated = executor.awaitTermination(59, MINUTES);
            if (terminated) {
                log.debug("ExecutorService is terminated");
            } else {
                log.error("ExecutorService NOT terminated");

                /* 1 hour */
                int countOneHour = 60 * 60;
                while (!executor.isTerminated()) {
                    log.error("ExecutorService not terminated <- " + countOneHour);
                    // wait 1s
                    Thread.sleep(1000);
                    countOneHour--;
                }
            }
        } catch (InterruptedException e) {
            log.error("Executor waiting interrupted.");
            System.exit(1);
        }
    }
}
