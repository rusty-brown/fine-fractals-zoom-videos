package fine.fractals.fractal.finebrot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;

import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_WIDTH;

public class PixelsFinebrotImpl {

    private static final Logger log = LogManager.getLogger(PixelsFinebrotImpl.class);
    private final int[][] elementsStaticFinebrot;

    public PixelsFinebrotImpl() {
        log.debug("constructor");
        log.debug("[" + RESOLUTION_WIDTH + "][" + RESOLUTION_HEIGHT + "]");
        elementsStaticFinebrot = new int[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
    }

    public void add(int x, int y) {
        elementsStaticFinebrot[x][y] += 1;
    }

    public void clear() {
        log.debug("clear");
        for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
            for (int x = 0; x < RESOLUTION_WIDTH; x++) {
                elementsStaticFinebrot[x][y] = 0;
            }
        }
    }

    public int valueAt(int x, int y) {
        return elementsStaticFinebrot[x][y];
    }

    public int bestFourChunksValue() {
        log.debug("bestFourChunksValue()");
        final int chunkSizeX = RESOLUTION_WIDTH / 20;
        final int chunkSizeY = RESOLUTION_HEIGHT / 20;
        final ArrayList<Integer> values = new ArrayList<>(20 * 20);
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                values.add(chunkValue(
                                x * chunkSizeX, (x + 1) * chunkSizeX,
                                y * chunkSizeY, (y + 1) * chunkSizeY
                        )
                );
            }
        }
        values.sort(Collections.reverseOrder());

        int sum = 0;
        for (int i = 0; i < 4; i++) {
            int v = values.get(i);
            sum += v;
        }
        log.debug("bestFourChunksValue() sum: " + sum);
        return sum;
    }

    private int chunkValue(int xFrom, int xTo, int yFrom, int yTo) {
        int sum = 0;
        for (int x = xFrom; x < xTo; x++) {
            for (int y = yFrom; y < yTo; y++) {
                sum += elementsStaticFinebrot[x][y];
            }
        }
        return sum;
    }
}
