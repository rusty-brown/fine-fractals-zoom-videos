package fine.fractals;

import fine.fractals.color.PaletteBlueToWhite;
import fine.fractals.data.mem.Mem;
import fine.fractals.fractal.finebrot.finite.FractalFinite;

import static fine.fractals.color.common.PaletteImpl.Palette;
import static fine.fractals.data.mandelbrot.ResolutionMultiplier.square_alter;
import static fine.fractals.machine.ApplicationImpl.Application;
import static fine.fractals.machine.ApplicationImpl.REPEAT;

public class Finebrot_Side extends FractalFinite {

    public Finebrot_Side() {
        NAME = "Finebrot_Side";

        ITERATION_MAX = 14800;
        ITERATION_MIN = 42;

        INIT_MANDELBROT_AREA_SIZE = 7.0;
        INIT_MANDELBROT_TARGET_re = -0.10675625916322415;
        INIT_MANDELBROT_TARGET_im = -0.8914368889277283;

        INIT_FINEBROT_AREA_SIZE = INIT_MANDELBROT_AREA_SIZE;
        INIT_FINEBROT_TARGET_re = INIT_MANDELBROT_TARGET_re;
        INIT_FINEBROT_TARGET_im = INIT_MANDELBROT_TARGET_im;
    }

    public static void main(String[] args) {

        RESOLUTION_WIDTH = 1920;
        RESOLUTION_HEIGHT = 1080;
        RESOLUTION_MULTIPLIER = square_alter;

        REPEAT = true;
        SAVE_IMAGES = false;
        FinebrotFractal = new Finebrot_Side();
        Palette = new PaletteBlueToWhite();

        Application.execute();
    }

    public void math(Mem m, double originRe, double originIm) {
        m.square();
        m.plus(originRe, originIm);
    }
}