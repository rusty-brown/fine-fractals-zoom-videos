package fine.fractals.engine;

import fine.fractals.Application;
import fine.fractals.Main;
import fine.fractals.data.Key;
import fine.fractals.data.objects.Data;
import fine.fractals.fractal.Fractal;
import fine.fractals.math.Design;
import fine.fractals.math.common.Element;
import fine.fractals.math.mandelbrot.Mandelbrot;
import fine.fractals.ui.OneTarget;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FractalEngine {

    private static final Logger log = LogManager.getLogger(FractalEngine.class);
    public static boolean calculationInProgress;
    public static String calculationProgress;
    public static String calculationText;
    public static int[] calculationProgressPoint = null;
    public static FractalEngine ME;
    private final BufferedImage designImage;

    private final Design design;
    private final Mandelbrot mandelbrot;

    private boolean updateDomain = false;

    public FractalEngine(BufferedImage designImage) {
        this.designImage = designImage;
        this.design = new Design();
        this.design.setAreaImage(Application.ME.areaImage);

        this.mandelbrot = new Mandelbrot(this.design, Application.ME.areaDomain, Application.ME.areaImage);
        FractalEngine.ME = this;
    }

    public static void save() {
        FractalMachine.saveImage(ME.designImage);
        Application.ME.repaint();
    }

    synchronized public void calculateFromThread() {

        log.info("calculateFromThread");

        Data.set(Key.A_ITERATION_MAX, Fractal.ITERATION_MAX);
        Data.set(Key.A_ITERATION_MIN, Fractal.ITERATION_MIN);

        design.clearScreenValues();

        calculationInProgress = true;

        // Calculate Design
        if (updateDomain) {
            mandelbrot.DOMAIN.domainForThisZoom();
            log.info("new domain done");
            updateDomain = false;
        }
        log.info("CALCULATE");
        mandelbrot.calculate();

        /* sort escape path to spectrum for ZOOM */
        // design.addAllEscapePathsToSpectra();


            if (Application.REPEAT) {
                /* Test if Optimization didn't break anything */
                mandelbrot.fixOptimizationBreak();
            }

        log.info("ScreenValuesToImages 1");
        screenValuesToImages();

        /* save file based on screen height; don't save it for testing */
        if (Main.RESOLUTION_IMAGE_WIDTH >= Application.RESOLUTION_IMAGE_SAVE_FOR) {
            log.info("Save images");
            FractalMachine.saveImage(designImage);
            log.info("Save images DONE");
        }
        log.info("DONE");
        calculationInProgress = false;
        calculationProgressPoint = null;
        Application.ME.repaint();
    }

    synchronized public void calculateFractalColoring() {
        log.info("calculateFractalColoring");
        calculationInProgress = true;
        // design.addAllEscapePathsToSpectra(); for ZOOM

        log.info("ScreenValuesToImages 2");
        screenValuesToImages();

        /* save file based on screen height; don't save it for testing */
        if (Main.RESOLUTION_IMAGE_WIDTH >= Application.RESOLUTION_IMAGE_SAVE_FOR) {
            log.info("Save images");
            FractalMachine.saveImage(designImage);
            log.info("Save images done");
        }

        log.info("DONE");
        calculationInProgress = false;
        calculationProgressPoint = null;
        Application.ME.repaint();
    }

    private void screenValuesToImages() {

        design.createValueIntegral();

        Color color;
        log.info("ScreenValuesToImages Design");
        for (int x = 0; x < Main.RESOLUTION_IMAGE_HEIGHT; x++) {
            for (int t = 0; t < Main.RESOLUTION_IMAGE_WIDTH; t++) {
                /* Design pattern */
                color = design.colorAt(t, x);
                designImage.setRGB(t, x, color.getRGB());
            }
        }
    }

    public void updateDomain() {
        this.updateDomain = true;
    }

    public void createMandelbrotMask(BufferedImage mandelbrotMask) {
        this.mandelbrot.DOMAIN.createMask(mandelbrotMask);
    }

    public void fixDomainOnClick(OneTarget target) {
        this.mandelbrot.fixDomainOptimizationOnClick(target.getScreenFromCornerT(), target.getScreenFromCornerX());
        Application.ME.repaint();
    }

    public Element getMandelbrotElementAt(int mousePositionT, int mousePositionX) {
        return this.mandelbrot.getElementAt(mousePositionT, mousePositionX);
    }
}
