package fine.fractals.context.finebrot;

import fine.fractals.data.Mem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.ListIterator;

import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.context.finebrot.FinebrotImpl.Finebrot;

public class DomainFinebrotImpl {

    private static final Logger log = LogManager.getLogger(DomainFinebrotImpl.class);

    private int counter = 0;

    /**
     * re; im
     */
    private final ArrayList<ArrayList<Double>> paths = new ArrayList<>();

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
    public void addEscapePathInside(ArrayList<Double> path) {
        paths.add(path);
    }

    public void domainToScreenGrid() {
        log.info("domainToScreenGrid");

        int removed = 0;
        int added = 0;

        final Mem mem = new Mem();

        double re;
        double im;

        for (ArrayList<Double> path : paths) {
            for (int i = 0; i < path.size() - 1; i++) {
                re = path.get(i);
                im = path.get(++i);
                AreaFinebrot.domainToScreenCarry(mem, re, im);

                if (mem.pxRe != Mem.NOT && mem.pxIm != Mem.NOT) {
                    added++;
                    Finebrot.add(mem.pxRe, mem.pxIm);
                }
            }
        }

        /* remove elements which moved our fo zoomed area */
        counter++;
        if (counter % 10 == 0) {
            for (ArrayList<Double> path : paths) {

                final ListIterator<Double> it = path.listIterator();
                while(it.hasNext()) {
                    re = it.next();
                    if (!AreaFinebrot.containsRe(re)) {
                        /* remove re & im */
                        it.remove();
                        it.next();
                        it.remove();
                    } else {
                        im = it.next();
                        if (!AreaFinebrot.containsIm(im)) {
                            /* remove re & im */
                            it.remove();
                            it.previous();
                            it.remove();
                        }
                    }
                }
            }
            paths.removeIf(ArrayList::isEmpty);
        }

        log.info("* Removed: " + removed);
        log.info("* Added:   " + added);
    }
}
