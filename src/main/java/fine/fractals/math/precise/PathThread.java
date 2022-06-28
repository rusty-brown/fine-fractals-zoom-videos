package fine.fractals.math.precise;

import fine.fractals.Main;
import fine.fractals.fractal.Fractal;
import fine.fractals.math.AreaImage;
import fine.fractals.math.Design;
import fine.fractals.math.common.Element;
import fine.fractals.math.common.HH;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class PathThread implements Runnable {

    private static final Logger log = LogManager.getLogger(PathThread.class);

    private final int myId;
    private final Element el;
    private final AreaImage areaImage;
    private final Design design;

    private final HH hh = new HH();

    public PathThread(int myId, Element el, AreaImage areaImage, Design design) {
        this.myId = myId;
        this.el = el;
        this.areaImage = areaImage;
        this.design = design;
    }

    public void run() {

        int iterator = el.getLastIteration();

        hh.calculation.re = el.originReT;
        hh.calculation.im = el.originImX;

        ArrayList<double[]> path = new ArrayList<>();

        while (hh.quadrance() < Fractal.CALCULATION_BOUNDARY && iterator < Fractal.ITERATION_MAX) {

            /*
             * Calculation happens only here
             */
            Main.FRACTAL.math(hh, el.originReT, el.originImX);

            /** There are Three possible contains: Domain zoomed, Image, Domain full */
            if (areaImage.contains(hh)) {
                /* Calculation did not diverge */
                path.add(new double[]{hh.calculation.re, hh.calculation.im});
            }
            iterator++;
        }

        // TODO <= ?
        boolean pathTest = iterator < Fractal.ITERATION_MAX;


        if (pathTest) {
            /* Element diverged */
            el.setValues(iterator);
            /* Don't set last iteration; I will need test if it was 0 bellow. It is set in last else */
            /* This state may latter change to hibernatedFinishedInside */
            el.setHibernatedFinished();
            /* Divergent paths for Design */

            /** PATH size may DIFFER based on contains */
            if (el.getLastIteration() == 0 && path.size() > Fractal.ITERATION_MIN) {

                /* This isn't continuation of unfinished iteration from previous calculation */

                el.setHibernatedFinishedInside();

                design.addEscapePathToSpectraNow(path);
            }
        }
    }

}
