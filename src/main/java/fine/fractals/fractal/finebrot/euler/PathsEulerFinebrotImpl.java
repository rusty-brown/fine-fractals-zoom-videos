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
        log.debug("* Added:   " + added);

        /* remove elements which moved our fo zoomed area */
        removeElementsOutside();
    }
}
