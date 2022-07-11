package fine.fractals.fractal.finebrot.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;

public abstract class PathsFinebrotCommonImpl {

    private static final Logger log = LogManager.getLogger(PathsFinebrotCommonImpl.class);

    /**
     * re; im
     * double[2] is better than 2x Double
     */
    protected final ArrayList<ArrayList<double[]>> paths = new ArrayList<>();

    public abstract void addEscapePathLong(ArrayList<double[]> path);

    public abstract void domainToScreenGrid();


    protected void removeElementsOutside() {
        log.debug("Remove elements which zoomed out");
        for (ArrayList<double[]> path : paths) {
            if (path.removeIf(el -> AreaFinebrot.isOutside(el[0], el[1]))) {
            }
        }
        paths.removeIf(l -> l.size() < 7);
    }
}
