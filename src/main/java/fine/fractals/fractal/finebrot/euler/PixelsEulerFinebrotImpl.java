package fine.fractals.fractal.finebrot.euler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.context.ApplicationImpl.RESOLUTION_HEIGHT;
import static fine.fractals.context.ApplicationImpl.RESOLUTION_WIDTH;

public class PixelsEulerFinebrotImpl {

    public enum Spectra {red, green, blue}

    private final int[][] elementsStaticScreenRed;
    private final int[][] elementsStaticScreenGreen;
    private final int[][] elementsStaticScreenBlue;

    private static final Logger log = LogManager.getLogger(PixelsEulerFinebrotImpl.class);

    public PixelsEulerFinebrotImpl() {
        this.elementsStaticScreenRed = new int[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
        this.elementsStaticScreenGreen = new int[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
        this.elementsStaticScreenBlue = new int[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
    }

    public void add(int x, int y, int spectra) {
        switch (spectra) {
            case 0 -> elementsStaticScreenRed[x][y] += 1;
            case 1 -> elementsStaticScreenGreen[x][y] += 1;
            case 2 -> elementsStaticScreenBlue[x][y] += 1;
        }
    }

    public void clear() {
        for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
            for (int x = 0; x < RESOLUTION_WIDTH; x++) {
                elementsStaticScreenRed[x][y] = 0;
                elementsStaticScreenGreen[x][y] = 0;
                elementsStaticScreenBlue[x][y] = 0;
            }
        }
    }

    public int valueAt(int x, int y, Spectra spec) {
        switch (spec) {
            case red -> {
                return elementsStaticScreenRed[x][y];
            }
            case green -> {
                return elementsStaticScreenGreen[x][y];
            }
            case blue -> {
                return elementsStaticScreenBlue[x][y];
            }
        }
        throw new RuntimeException("unknown spectra");
    }

    public void set(int x, int y, Spectra spec, int colorValue) {
        switch (spec) {
            case red -> elementsStaticScreenRed[x][y] = colorValue;
            case green -> elementsStaticScreenGreen[x][y] = colorValue;
            case blue -> elementsStaticScreenBlue[x][y] = colorValue;
        }
    }
}
