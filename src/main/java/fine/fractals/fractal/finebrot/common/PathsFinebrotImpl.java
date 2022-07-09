package fine.fractals.fractal.finebrot.common;

import fine.fractals.data.mem.Mem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.finite.FractalFinite.PixelsFinebrot;

public class PathsFinebrotImpl extends PathsFinebrotCommonImpl {

    private static final Logger log = LogManager.getLogger(PathsFinebrotImpl.class);

    private int counter = 0;

    /**
     * re; im
     * double[2] is better than 2x Double
     */
    private final ArrayList<ArrayList<double[]>> paths = new ArrayList<>();

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
        log.info("domainToScreenGrid()");

        int removed = 0;
        int added = 0;

        final Mem m = new Mem();
        double[] tmp;
        for (ArrayList<double[]> path : paths) {
            if (path != null) {
                for (int i = 0; i < path.size() - 1; i++) {
                    tmp = path.get(i);
                    AreaFinebrot.domainToScreenCarry(m, tmp[0], tmp[1]);
                    if (m.px != Mem.NOT && m.py != Mem.NOT) {
                        added++;
                        PixelsFinebrot.add(m.px, m.py);
                    }
                }
            } else {
                log.error("path can't be null");
            }
        }

        /* remove elements which moved our fo zoomed area */
        counter++;
        if (counter % 5 == 0) {
            log.debug("Remove elements which zoomed out");
            for (ArrayList<double[]> path : paths) {
                if (path.removeIf(el -> !AreaFinebrot.contains(el[0], el[1]))) {
                    removed++;
                }
            }
            paths.removeIf(ArrayList::isEmpty);
        }
        log.debug("* Removed: " + removed);
        log.debug("* Added:   " + added);
    }
}
