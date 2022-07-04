package fine.fractals.context.finebrot;

import fine.fractals.data.Mem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.context.finebrot.FinebrotImpl.Finebrot;

public class DomainFinebrotImpl {

    private static final Logger log = LogManager.getLogger(DomainFinebrotImpl.class);

    private int counter = 0;

    /**
     * re; im
     */
    private final ArrayList<ArrayList<double[]>> paths = new ArrayList<>();

    public static final DomainFinebrotImpl DomainFinebrot;

    static {
        log.info("init");
        DomainFinebrot = new DomainFinebrotImpl();
    }

    private DomainFinebrotImpl() {
    }


    /*
     * All elements on escape path are already inside displayed area
     * Because they are filtered like that during calculation
     */
    public void addEscapePathInside(ArrayList<double[]> path) {
        paths.add(path);
    }

    public void domainToScreenGrid() {
        log.info("domainToScreenGrid");

        int removed = 0;
        int added = 0;

        final Mem mem = new Mem();

        double[] tmp;

        for (ArrayList<double[]> path : paths) {
            for (int i = 0; i < path.size() - 1; i++) {
                tmp = path.get(i);
                AreaFinebrot.domainToScreenCarry(mem, tmp[0], tmp[1]);

                if (mem.pxRe != Mem.NOT && mem.pxIm != Mem.NOT) {
                    added++;
                    Finebrot.add(mem.pxRe, mem.pxIm);
                }
            }
        }

        /* remove elements which moved our fo zoomed area */
        counter++;
        if (counter % 10 == 0) {
            for (ArrayList<double[]> path : paths) {
                if (path.removeIf(el -> !AreaFinebrot.contains(el[0], el[1]))) {
                    removed++;
                }
            }
            paths.removeIf(ArrayList::isEmpty);
        }

        log.info("* Removed: " + removed);
        log.info("* Added:   " + added);
    }
}
