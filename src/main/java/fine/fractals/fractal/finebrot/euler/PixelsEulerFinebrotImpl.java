package fine.fractals.fractal.finebrot.euler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.context.ApplicationImpl.RESOLUTION_HEIGHT;
import static fine.fractals.context.ApplicationImpl.RESOLUTION_WIDTH;

public class PixelsEulerFinebrotImpl {

    private final int[][] elementsStaticScreenR;
    private final int[][] elementsStaticScreenG;
    private final int[][] elementsStaticScreenB;

    private static final Logger log = LogManager.getLogger(PixelsEulerFinebrotImpl.class);

    public PixelsEulerFinebrotImpl() {
        this.elementsStaticScreenR = new int[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
        this.elementsStaticScreenG = new int[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
        this.elementsStaticScreenB = new int[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
    }

    public void add(int x, int y, int spectra) {
        switch (spectra) {
            case 0 -> elementsStaticScreenR[x][y] += 1;
            case 1 -> elementsStaticScreenG[x][y] += 1;
            case 2 -> elementsStaticScreenB[x][y] += 1;
        }
    }

    public void clear() {
        for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
            for (int x = 0; x < RESOLUTION_WIDTH; x++) {
                elementsStaticScreenR[x][y] = 0;
                elementsStaticScreenG[x][y] = 0;
                elementsStaticScreenB[x][y] = 0;
            }
        }
    }

    public int valueAt(int x, int y, int spectra) {
        switch (spectra) {
            case 0 -> {
                return elementsStaticScreenR[x][y];
            }
            case 1 -> {
                return elementsStaticScreenG[x][y];
            }
            case 2 -> {
                return elementsStaticScreenB[x][y];
            }
        }
        throw new RuntimeException("unknown spectra");
    }
}
