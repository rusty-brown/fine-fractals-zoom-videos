package fine.fractals.concurent;

import fine.fractals.Main;
import fine.fractals.data.MandelbrotElement;
import fine.fractals.data.Mem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.context.finebrot.DomainFinebrotImpl.DomainFinebrot;
import static fine.fractals.fractal.Fractal.*;

public class PathThread implements Runnable {

    private static final Logger log = LogManager.getLogger(PathThread.class);

    private final MandelbrotElement el;

    private final Mem mem = new Mem();
    private final int myId;

    public PathThread(int myId, MandelbrotElement el) {
        log.trace("init " + myId);
        this.myId = myId;
        this.el = el;
    }

    public void run() {
        log.trace("run " + myId);
        int iterator = el.getLastIteration();

        mem.re = el.originRe;
        mem.im = el.originIm;

        final ArrayList<double[]> path = new ArrayList<>();

        while (mem.quadrance() < CALCULATION_BOUNDARY && iterator < ITERATION_MAX) {

            /*
             * Calculation happens only here
             */
            Main.FRACTAL.math(mem, el.originRe, el.originIm);

            if (AreaFinebrot.contains(mem)) {
                /* Calculation did not diverge */
                path.add(new double[]{mem.re, mem.im});
            }
            iterator++;
        }

        boolean pathTest = iterator < ITERATION_MAX;

        if (pathTest) {
            /* Element diverged */
            el.setValues(iterator);
            /* Don't set last iteration; I will need test if it was 0 bellow. It is set in last else */
            /* This state may latter change to hibernatedFinishedInside */
            el.setHibernatedFinished();
            /* Divergent paths for Design */

            /* PATH size may DIFFER based on contains */
            if (el.getLastIteration() == 0 && path.size() > ITERATION_MIN) {
                /* This isn't continuation of unfinished iteration from previous calculation */
                el.setHibernatedFinishedInside();

                DomainFinebrot.addEscapePathInside(path);
            }
        }
        log.trace("finished " + myId);
    }
}
