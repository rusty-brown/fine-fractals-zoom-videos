package fine.fractals.fractal.finebrot.common;

import fine.fractals.data.Stats;
import fine.fractals.data.mem.Mem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.finite.FractalFinite.PixelsFinebrot;

public class PathsFinebrotImpl extends PathsFinebrotCommonImpl {

    private static final Logger log = LogManager.getLogger(PathsFinebrotImpl.class);

    public PathsFinebrotImpl() {
        log.debug("constructor");
    }

    /*
     * All elements on escape path are already inside displayed area
     * Because they are filtered like that during calculation
     */
    public void addEscapePathLong(ArrayList<double[]> path) {
        paths.add(path);
    }

    public void domainToScreenGrid() {
        log.debug("domainToScreenGrid()");

        int pixelsTotal = 0;

        final Mem m = new Mem();
        double[] tmp;
        for (ArrayList<double[]> path : paths) {
            if (path != null) {
                for (int i = 0; i < path.size() - 1; i++) {
                    tmp = path.get(i);
                    AreaFinebrot.domainToScreenCarry(m, tmp[0], tmp[1]);
                    if (m.px != Mem.NOT && m.py != Mem.NOT) {
                        pixelsTotal++;
                        PixelsFinebrot.add(m.px, m.py);
                    }
                }
            } else {
                log.error("path can't be null");
            }
        }
        log.debug("pixelsTotal:   " + pixelsTotal);

        /* remove elements which moved our fo zoomed area */
        removeElementsOutside();

        Stats.pathsTotalAmount = paths.size();
        Stats.pixelsValueTotal = pixelsTotal;
    }
}
