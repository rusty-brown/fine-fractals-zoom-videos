package fine.fractals;

import fine.fractals.color.PaletteBlackToWhite;
import fine.fractals.data.Mem;
import fine.fractals.fractal.finebrot.finite.FractalFinite;

import static fine.fractals.context.ApplicationImpl.*;
import static fine.fractals.data.ResolutionMultiplier.none;

public class Lotus extends FractalFinite {

    public Lotus() {
        NAME = "Lotus";

        ITERATION_MAX = 8000;
        ITERATION_MIN = 42;

        INIT_MANDELBROT_AREA_SIZE = 3.5;
        INIT_MANDELBROT_TARGET_re = -0.2;
        INIT_MANDELBROT_TARGET_im = 0.0;

        INIT_FINEBROT_AREA_SIZE = INIT_MANDELBROT_AREA_SIZE;
        INIT_FINEBROT_TARGET_re = INIT_MANDELBROT_TARGET_re;
        INIT_FINEBROT_TARGET_im = INIT_MANDELBROT_TARGET_im;
    }

    @Override
    public void math(Mem m, double originRe, double originIm) {
        m.conjugation();
        m.square();
        m.plus(originRe, originIm);
    }

    public static void main(String[] args) {

        FinebrotFractal = new Lotus();

        PaletteBlackToWhite.init();

        RESOLUTION_WIDTH = 1920;
        RESOLUTION_HEIGHT = 1080;
        RESOLUTION_MULTIPLIER = none;

        REPEAT = false;
        SAVE_IMAGES = false;

        Application.execute();
    }
}
