package fine.fractals.fractal;

import fine.fractals.data.Mem;
import fine.fractals.fractal.abst.FractalFinite;

public class Finebrot_Top extends FractalFinite {

    public Finebrot_Top() {
        super("Finebrot_Top");
        ITERATION_MAX = 14800;
        ITERATION_MIN = 42;

        INIT_MANDELBROT_AREA_SIZE = 6.0;
        INIT_MANDELBROT_TARGET_re = -1.40115518909199;
        INIT_MANDELBROT_TARGET_im = 0.0;

        INIT_FINEBROT_AREA_SIZE = INIT_MANDELBROT_AREA_SIZE;
        INIT_FINEBROT_TARGET_re = INIT_MANDELBROT_TARGET_re;
        INIT_FINEBROT_TARGET_im = INIT_MANDELBROT_TARGET_im;
    }

    public void math(Mem m, double originRe, double originIm) {
        m.square();
        m.plus(originRe, originIm);
    }
}
