package fine.fractals;

import fine.fractals.color.PaletteBlackToWhite;
import fine.fractals.data.mem.Mem;
import fine.fractals.fractal.finebrot.finite.FractalFinite;

import static fine.fractals.color.common.PaletteImpl.Palette;
import static fine.fractals.data.mandelbrot.ResolutionMultiplier.none;
import static fine.fractals.machine.ApplicationImpl.Application;
import static fine.fractals.machine.ApplicationImpl.REPEAT;

public class Lotus extends FractalFinite {

    public Lotus() {
        NAME = "Lotus";

        ITERATION_MAX = 8000;
        ITERATION_min = 42;

        INIT_MANDELBROT_AREA_SIZE = 9.5;
        INIT_MANDELBROT_TARGET_re = 0.67748277351478;
        INIT_MANDELBROT_TARGET_im = -1.18770078111202;

        INIT_FINEBROT_AREA_SIZE = INIT_MANDELBROT_AREA_SIZE;
        INIT_FINEBROT_TARGET_re = INIT_MANDELBROT_TARGET_re;
        INIT_FINEBROT_TARGET_im = INIT_MANDELBROT_TARGET_im;
    }

    public static void main(String[] args) {

        RESOLUTION_WIDTH = 1920;
        RESOLUTION_HEIGHT = 1080;
        RESOLUTION_MULTIPLIER = none;

        REPEAT = false;
        SAVE_IMAGES = false;
        FinebrotFractal = new Lotus();
        Palette = new PaletteBlackToWhite();

        Application.execute();
    }

    @Override
    public void math(Mem m, double originRe, double originIm) {
        m.conjugation();
        m.square();
        m.plus(originRe, originIm);
    }
}
