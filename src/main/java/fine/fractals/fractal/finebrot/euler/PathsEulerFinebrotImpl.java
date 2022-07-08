package fine.fractals.fractal.finebrot.euler;

import fine.fractals.data.mem.Mem;
import fine.fractals.data.mem.MemEuler;
import fine.fractals.fractal.finebrot.common.PathsFinebrotCommonImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.euler.FractalEuler.PixelsEulerFinebrot;
import static java.util.Objects.requireNonNull;

public class PathsEulerFinebrotImpl extends PathsFinebrotCommonImpl {

    private static final Logger log = LogManager.getLogger(PathsEulerFinebrotImpl.class);

    private int counter = 0;

    /**
     * re; im
     * double[2] is better than 2x Double
     */
    private final ArrayList<ArrayList<double[]>> paths = new ArrayList<>();

    public PathsEulerFinebrotImpl() {
    }

    /**
     * All elements on escape path are already inside displayed area
     * Because they are filtered like that during calculation
     */
    public void addEscapePathLong(ArrayList<double[]> path) {
        requireNonNull(path, "Path can't be null;");
        paths.add(path);
    }

    public void domainToScreenGrid() {
        log.debug("constructor");

        int removed = 0;
        int added = 0;

        final MemEuler m = new MemEuler();
        double[] tmp;
        for (ArrayList<double[]> path : paths) {
            for (int i = 0; i < path.size() - 1; i++) {
                tmp = path.get(i);
                AreaFinebrot.domainToScreenCarry(m, tmp[0], tmp[1]);
                if (m.px != Mem.NOT && m.py != Mem.NOT) {
                    added++;
                    FractalEuler.colorsFor(m, i, path.size());
                    PixelsEulerFinebrot.add(m.px, m.py, m.spectra);
                }
            }
        }

        /* remove elements which moved our from zoomed area */
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
