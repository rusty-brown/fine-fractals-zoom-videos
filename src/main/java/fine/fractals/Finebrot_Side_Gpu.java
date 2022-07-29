package fine.fractals;

import fine.fractals.color.PaletteBlueToWhite;
import fine.fractals.fractal.finebrot.common.FinebrotGPU;

import static fine.fractals.color.common.PaletteImpl.Palette;
import static fine.fractals.data.mandelbrot.ResolutionMultiplier.none;
import static fine.fractals.machine.ApplicationImpl.Application;
import static fine.fractals.machine.ApplicationImpl.REPEAT;

/*
 * Unstable GPU Test
 */
public class Finebrot_Side_Gpu extends FinebrotGPU {

    public Finebrot_Side_Gpu() {
        NAME = "Finebrot_Side_Gpu";

        ITERATION_MAX = 14800;
        ITERATION_min = 42;

        INIT_MANDELBROT_AREA_SIZE = 7.0;
        INIT_MANDELBROT_TARGET_re = -0.10675625909097;
        INIT_MANDELBROT_TARGET_im = -0.89143688885247;

        INIT_FINEBROT_AREA_SIZE = INIT_MANDELBROT_AREA_SIZE;
        INIT_FINEBROT_TARGET_re = INIT_MANDELBROT_TARGET_re;
        INIT_FINEBROT_TARGET_im = INIT_MANDELBROT_TARGET_im;
    }

    public static void main(String[] args) {

        RESOLUTION_WIDTH = 1280;
        RESOLUTION_HEIGHT = 720;
        RESOLUTION_MULTIPLIER = none;

        REPEAT = true;
        SAVE_IMAGES = false;
        FinebrotFractal = new Finebrot_Side_Gpu();
        Palette = new PaletteBlueToWhite();

        Application.execute();
    }
}