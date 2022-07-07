package fine.fractals.machine.concurent;

import fine.fractals.data.MandelbrotElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.common.FinebrotFractalImpl.*;

public class PathThread implements Runnable {

    @SuppressWarnings(value = "unused")
    private static final Logger log = LogManager.getLogger(PathThread.class);

    private final MandelbrotElement el;

    public PathThread(MandelbrotElement el) {
        this.el = el;
    }

    public void run() {

        final ArrayList<double[]> path = new ArrayList<>();

        final boolean pathTest = FinebrotFractal.calculatePath(el, path);

        if (pathTest) {
            el.setHibernatedFinished();
            /*
             * Removed lastIteration, lastVisitedRe, lastVisitedIm
             * There isn't continuation of unfinished iteration from previous calculation (ITERATION_MAX increased)
             * The element is going to migrate out of screen soon with its path anyway.
             */
            if (path.size() > ITERATION_MIN) {
                el.setHibernatedFinishedLong();
                PathsFinebrot.addEscapePathLong(path);
            }
        }
    }
}
