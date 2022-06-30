package fine.fractals.engine;

import fine.fractals.Main;
import fine.fractals.fractal.Fractal;
import fine.fractals.data.Element;
import fine.fractals.data.Mem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.context.finebrot.DomainFinebrotImpl.DomainFinebrot;
import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;

public class PathThread implements Runnable {

    private static final Logger log = LogManager.getLogger(PathThread.class);

    private final int myId;
    private final Element el;

    private final Mem mem = new Mem();

    public PathThread(int myId, Element el) {
        this.myId = myId;
        this.el = el;
    }

    public void run() {

        int iterator = el.getLastIteration();

        mem.re = el.originReT;
        mem.im = el.originImX;

        final ArrayList<double[]> path = new ArrayList<>();

        while (mem.quadrance() < Fractal.CALCULATION_BOUNDARY && iterator < Fractal.ITERATION_MAX) {

            /*
             * Calculation happens only here
             */
            Main.FRACTAL.math(mem, el.originReT, el.originImX);

            if (AreaFinebrot.contains(mem)) {
                /* Calculation did not diverge */
                path.add(new double[]{mem.re, mem.im});
            }
            iterator++;
        }

        boolean pathTest = iterator < Fractal.ITERATION_MAX;

        if (pathTest) {
            /* Element diverged */
            el.setValues(iterator);
            /* Don't set last iteration; I will need test if it was 0 bellow. It is set in last else */
            /* This state may latter change to hibernatedFinishedInside */
            el.setHibernatedFinished();
            /* Divergent paths for Design */

            /* PATH size may DIFFER based on contains */
            if (el.getLastIteration() == 0 && path.size() > Fractal.ITERATION_MIN) {
                /* This isn't continuation of unfinished iteration from previous calculation */
                el.setHibernatedFinishedInside();

                DomainFinebrot.addEscapePathInside(path);
            }
        }
    }
}
