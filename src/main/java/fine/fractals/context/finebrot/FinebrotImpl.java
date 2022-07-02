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
    public static final FinebrotImpl Finebrot;

    private FinebrotImpl() {
        this.elementsStaticScreen = new int[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
    }

    static {
        log.info("init");
        Finebrot = new FinebrotImpl();
    }

    public void add(int x, int y) {
        this.elementsStaticScreen[x][y] += 1;
    }

    public void clear() {
        for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
            for (int x = 0; x < RESOLUTION_WIDTH; x++) {
                elementsStaticScreen[x][y] = 0;
            }
        }
    }

    public int valueAt(int x, int y) {
        return elementsStaticScreen[x][y];
    }

}
