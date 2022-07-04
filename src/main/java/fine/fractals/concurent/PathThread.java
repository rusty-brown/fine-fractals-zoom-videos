package fine.fractals.concurent;

import fine.fractals.data.MandelbrotElement;
import fine.fractals.data.Mem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.Main.FRACTAL;
import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.context.finebrot.DomainFinebrotImpl.DomainFinebrot;
import static fine.fractals.fractal.Fractal.*;

public class PathThread implements Runnable {

    @SuppressWarnings(value = "unused")
    private static final Logger log = LogManager.getLogger(PathThread.class);

    private final MandelbrotElement el;

    public PathThread(MandelbrotElement el) {
        this.el = el;
    }

    public void run() {
        int iterator = 0;
        final Mem mem = new Mem();

        mem.re = el.originRe;
        mem.im = el.originIm;

        /* double[2] consumes less memory than 2x Double */
        final ArrayList<double[]> path = new ArrayList<>();

        int success = 0;
        while (mem.quadrance() < CALCULATION_BOUNDARY && iterator < ITERATION_MAX) {

            /*
             * Calculation happens only here
             */
            FRACTAL.math(mem, el.originRe, el.originIm);

            if (AreaFinebrot.contains(mem)) {
                /* Calculation did not diverge */
                path.add(new double[]{mem.re, mem.im});
                success++;
            } else {
                if (iterator == 500) {
                    if (success < 6) {
                        // log.info("Fast fail " + success);

                        // TODO set proper el state
                        el.setHibernatedBlack();
                        return;
                    }
                }
            }
            iterator++;
        }

        boolean pathTest = iterator < ITERATION_MAX;

        if (pathTest) {
            /* Element diverged */
            el.setValues(iterator);
            el.setHibernatedFinished();
            /*
             * Removed lastIteration, lastVisitedRe, lastVisitedIm
             * TODO add them back but with clear way of managing these new data, path lengths, etc
             */
            if (path.size() > ITERATION_MIN) {

                /* This isn't continuation of unfinished iteration from previous calculation */
                el.setHibernatedFinishedLong();

                DomainFinebrot.addEscapePathLong(path);
            }
        }
    }
}
