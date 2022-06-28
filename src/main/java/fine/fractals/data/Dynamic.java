package fine.fractals.data;

import fine.fractals.math.AreaImage;
import fine.fractals.math.common.HH;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Dynamic {

    private static final Logger log = LogManager.getLogger(Dynamic.class);

    /**
     * re; im
     */
    private final ArrayList<ArrayList<double[]>> paths = new ArrayList<>();

    private AreaImage areaImage;
    private final Screen screen;

    public Dynamic(Screen screen) {
        this.screen = screen;
    }

    public void setAreaImage(AreaImage areaImage) {
        this.areaImage = areaImage;
    }

    /* All elements on escape path are already inside displayed area */
    // TODO why?, how?, really?
    public synchronized void addEscapePathInside(ArrayList<double[]> path) {
        paths.add(path);
    }

    public void domainToScreenGrid() {

        log.info("domainToScreenGrid");

        int removed = 0;
        int added = 0;

        HH hh = new HH();

        double[] tmp;

        for (ArrayList<double[]> path : paths) {

            for (int i = path.size() - 1; i >= 0; --i) {

                tmp = path.get(i);
                this.areaImage.domainToScreenCarry(hh, tmp[0], tmp[1]);

                if (hh.calculation.pxRe != HH.NOT && hh.calculation.pxIm != HH.NOT) {
                    /* Multiple elements hit same pixel */
                    added++;
                    this.screen.add(hh);
                } else {
                    /* remove im on rowRe */
                    removed++;
                    path.remove(i);

                    i--;
                }
            }
            if (path.isEmpty()) {
                paths.remove(path);
            }
        }

        log.info("* Removed: " + removed);
        log.info("* Added: " + added);
    }
}
