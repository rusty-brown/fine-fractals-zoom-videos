package fine.fractals.fractal.finebrot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.context.ApplicationImpl.RESOLUTION_HEIGHT;
import static fine.fractals.context.ApplicationImpl.RESOLUTION_WIDTH;

public class PixelsFinebrotImpl {

    private final int[][] elementsStaticFinebrot;

    private static final Logger log = LogManager.getLogger(PixelsFinebrotImpl.class);

    public PixelsFinebrotImpl() {
        log.info("FinebrotImpl()");
        this.elementsStaticFinebrot = new int[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
    }

    public void add(int x, int y) {
        this.elementsStaticFinebrot[x][y] += 1;
    }

    public void clear() {
        for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
            for (int x = 0; x < RESOLUTION_WIDTH; x++) {
                elementsStaticFinebrot[x][y] = 0;
            }
        }
    }

    public int valueAt(int x, int y) {
        return elementsStaticFinebrot[x][y];
    }
}
