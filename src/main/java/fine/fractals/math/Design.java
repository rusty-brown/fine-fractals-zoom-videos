package fine.fractals.math;

import fine.fractals.Main;
import fine.fractals.data.Dynamic;
import fine.fractals.data.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Design {

    public static Design ME;
    private static final Logger log = LogManager.getLogger(Design.class);

    public Screen screen;
    public Dynamic dynamic;

    private int counterAll = 0;

    private HashMap<Integer, Color> finalColors;

    public Design() {
        log.info("Design constructor");
        this.screen = new Screen();
        ME = this;

        this.dynamic = new Dynamic(screen);
    }

    public void calculationFinished() {

        dynamicToScreen();

        this.screen.updateMiniMax();

        int maxScrValue = this.screen.maxSrc();
        int minScrValue = this.screen.minSrc();

        log.info("update palette for orbital density");
        Main.colorPalette.update(maxScrValue, minScrValue);


        log.info("--------------");
        log.info("# " + counterAll);
        log.info("--------------");
        counterAll = 0;
    }

    /* All elements on escape path are already inside displayed image area */
    public void addEscapePathToSpectraNow(ArrayList<double[]> path) {
        dynamic.addEscapePathInside(path);
    }

    public void createValueIntegral() {

        this.finalColors = new HashMap<>();

        ColoredValueIntegral coloredValueIntegral = new ColoredValueIntegral(this.screen);
        /* pixelCountToColorByOneColor */
        int allPixels = Main.RESOLUTION_IMAGE_WIDTH * Main.RESOLUTION_IMAGE_HEIGHT;
        int allColorsCount = Main.colorPalette.colorResolution();
        final int fullColoring = ((int) ((double) allPixels / (double) allColorsCount));
        final int left = allPixels - (allColorsCount * fullColoring);

        log.info("allPixels: " + allPixels);
        log.info("allColorsCount: " + allColorsCount);
        log.info("fullColoring: " + fullColoring);
        log.info("left*: " + left);

        coloredValueIntegral.setColorLeft(left);
        coloredValueIntegral.setFullColoring(fullColoring);

        int colorIndex = 0;

        /* for all colors */
        while (colorIndex < allColorsCount) {
            int colorLeft = fullColoring;
            /* until there is any color left */
            while (colorLeft > 0) {
                colorLeft = coloredValueIntegral.applyPaint(colorIndex, colorLeft);
                if (colorLeft == 0) {
                    // no color left, switch to next color
                    colorIndex++;
                    break;
                }
            }
        }

        coloredValueIntegral.finalize(this.finalColors);
    }

    public Color colorAt(int t, int x) {
        int co = this.screen.colorFor(t, x);
        return this.finalColors.get(co);
    }

    public void clearScreenValues() {
        log.info("CLEAR SCREEN VALUES");

        this.screen.clear();

    }

    public void setAreaImage(AreaImage areaImage) {
        if (dynamic != null) {
            this.dynamic.setAreaImage(areaImage);
        }
    }

    public void dynamicToScreen() {
        log.info("remove elements outside start");
        this.dynamic.domainToScreenGrid();
    }

}
