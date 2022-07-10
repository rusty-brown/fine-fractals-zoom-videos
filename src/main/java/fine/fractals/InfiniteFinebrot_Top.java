package fine.fractals;

import fine.fractals.color.PaletteBlackToWhite;
import fine.fractals.data.mem.Mem;
import fine.fractals.fractal.finebrot.finite.FractalFinite;

import static fine.fractals.color.common.PaletteImpl.Palette;
import static fine.fractals.data.mandelbrot.ResolutionMultiplier.none;
import static fine.fractals.machine.ApplicationImpl.Application;
import static fine.fractals.machine.ApplicationImpl.REPEAT;

public class InfiniteFinebrot_Top extends FractalFinite {

    public InfiniteFinebrot_Top() {
        NAME = "InfiniteFinebrot_Top";

        ITERATION_MAX = 180_000;
        ITERATION_MIN = 3000;

        INIT_MANDELBROT_AREA_SIZE = 2.5;
        INIT_MANDELBROT_TARGET_re = -0.5;
        INIT_MANDELBROT_TARGET_im = 0.0;

        INIT_FINEBROT_AREA_SIZE = 1.8;
        INIT_FINEBROT_TARGET_re = -1.0;
        INIT_FINEBROT_TARGET_im = 0.0;
    }

    public static void main(String[] args) {

        RESOLUTION_WIDTH = 1920;
        RESOLUTION_HEIGHT = 1080;
        RESOLUTION_MULTIPLIER = none;

        REPEAT = false;
        SAVE_IMAGES = false;
        FinebrotFractal = new InfiniteFinebrot_Top();
        Palette = new PaletteBlackToWhite();

        Application.execute();
    }

    @Override
    public void math(Mem m, double originRe, double originIm) {
        m.square();
        m.plus(originRe, originIm);
    }
}
