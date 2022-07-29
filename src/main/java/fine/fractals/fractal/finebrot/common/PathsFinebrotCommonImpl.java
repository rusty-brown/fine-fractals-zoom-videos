package fine.fractals.fractal.finebrot.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.TOLERATE_PATH_LENGTH_min;

public abstract class PathsFinebrotCommonImpl {

    private static final Logger log = LogManager.getLogger(PathsFinebrotCommonImpl.class);

    /**
     * Calculation paths
     * Dynamic data for Finebrot fractal. These double[] data will be projected to in[][] pixels and then colored.
     * As zoom progress, points [re,im] are projected to new pixels [px,py] until they migrate out of the tiny finebrot Area.
     * Elements outside tiny finebrot Area are removed. Very short paths are also removed.
     * [re, im] representation as double[2] is better than 2x Double.
     */
    protected final ArrayList<ArrayList<double[]>> paths = new ArrayList<>();

    /**
     * All elements on calculation path are already inside displayed area
     * Because they are filtered like that during calculation
     */
    public abstract void addEscapePathLong(ArrayList<double[]> path);

    public abstract void translatePathsToPixelGrid();

    protected void removeElementsOutside() {
        log.debug("Remove elements which zoomed out");
        for (ArrayList<double[]> path : paths) {
            path.removeIf(el -> AreaFinebrot.isOutside(el[0], el[1]));
        }
        paths.removeIf(path -> path.size() < TOLERATE_PATH_LENGTH_min);
    }
}
