package fine.fractals.data;

import fine.fractals.Main;
import fine.fractals.math.common.HH;

public class Screen {

    private final int[][] elementsStaticScreen;
    private int maxScrValue;
    private int minScrValue;

    public Screen() {
        this.elementsStaticScreen = new int[Main.RESOLUTION_IMAGE_WIDTH][Main.RESOLUTION_IMAGE_HEIGHT];
    }

    public void updateMiniMax() {

        int minScrValue = Integer.MAX_VALUE;
        int maxScrValue = 0;

        for (int i = 0; i < Main.RESOLUTION_IMAGE_WIDTH; i++) {
            for (int j = 0; j < Main.RESOLUTION_IMAGE_HEIGHT; j++) {
                int v = this.elementsStaticScreen[i][j];
                if (minScrValue > v) {
                    minScrValue = v;
                }
                if (v > maxScrValue) {
                    maxScrValue = v;
                }
            }
        }

        this.maxScrValue = maxScrValue;
        this.minScrValue = minScrValue;
    }

    public int maxSrc() {
        return this.maxScrValue;
    }

    public int minSrc() {
        return this.minScrValue;
    }

    @Deprecated
    public void add(HH hh) {
        add(hh.calculation.pxRe, hh.calculation.pxIm);
    }

    public void add(int pxT, int pxX) {
        this.elementsStaticScreen[pxT][pxX] += 1;
    }

    public void clear() {
        for (int x = 0; x < Main.RESOLUTION_IMAGE_HEIGHT; x++) {
            for (int t = 0; t < Main.RESOLUTION_IMAGE_WIDTH; t++) {
                elementsStaticScreen[t][x] = 0;
            }
        }
    }

    public int colorFor(int t, int x) {
        return elementsStaticScreen[t][x];
    }

}
