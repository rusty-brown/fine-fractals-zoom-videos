package fine.fractals;

import fine.fractals.data.Mem;
import fine.fractals.fractal.finebrot.finite.FractalFinite;

import static fine.fractals.context.ApplicationImpl.*;
import static fine.fractals.data.ResolutionMultiplier.square_11;

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

    @Override
    public void math(Mem m, double originRe, double originIm) {
        m.square();
        m.plus(originRe, originIm);
    }

    public static void main(String[] args) {

        FinebortFractal = new InfiniteFinebrot_Top();

        REPEAT = false;
        SAVE_IMAGES = false;
        RESOLUTION_MULTIPLIER = square_11;

        Application.execute();
    }
}
