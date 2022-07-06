package fine.fractals.fractal;

import fine.fractals.data.Mem;
import fine.fractals.fractal.abst.FractalFinite;

public class Finebrot_Side extends FractalFinite {

    public Finebrot_Side() {
        super("Finebrot_Side");
        ITERATION_MAX = 14800;
        ITERATION_MIN = 42;

        INIT_MANDELBROT_AREA_SIZE = 7.0;
        INIT_MANDELBROT_TARGET_re = -0.10675625916322415;
        INIT_MANDELBROT_TARGET_im = -0.8914368889277283;

        INIT_FINEBROT_AREA_SIZE = INIT_MANDELBROT_AREA_SIZE;
        INIT_FINEBROT_TARGET_re = INIT_MANDELBROT_TARGET_re;
        INIT_FINEBROT_TARGET_im = INIT_MANDELBROT_TARGET_im;
    }

    public void math(Mem m, double originRe, double originIm) {
        m.square();
        m.plus(originRe, originIm);
    }
}