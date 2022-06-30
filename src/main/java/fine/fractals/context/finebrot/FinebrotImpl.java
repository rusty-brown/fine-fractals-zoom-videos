package fine.fractals.context.finebrot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;

public class FinebrotImpl {

    private final int[][] elementsStaticScreen;

    private static final Logger log = LogManager.getLogger(FinebrotImpl.class);
    /**
     * Screen
     */
    public static FinebrotImpl Finebrot;

    private FinebrotImpl() {
        this.elementsStaticScreen = new int[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
    }

    static {
        log.info("init");
        Finebrot = new FinebrotImpl();
    }

    public void add(int pxT, int pxX) {
        this.elementsStaticScreen[pxT][pxX] += 1;
    }

    public void clear() {
        for (int x = 0; x < RESOLUTION_HEIGHT; x++) {
            for (int t = 0; t < RESOLUTION_WIDTH; t++) {
                elementsStaticScreen[t][x] = 0;
            }
        }
    }

    public int valueAt(int t, int x) {
        return elementsStaticScreen[t][x];
    }

}
