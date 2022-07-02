package fine.fractals.fractal;

import fine.fractals.data.Mem;

public class Finebrot_Top extends Fractal {

    public Finebrot_Top() {
        super("FineMandelbrot_Top_Zoom");
        ITERATION_MAX = 14800;
        ITERATION_MIN = 42;

        INIT_MANDELBROT_AREA_SIZE = 6.0;
        INIT_MANDELBROT_TARGET_re = -1.40115518909199;
        INIT_MANDELBROT_TARGET_im = 0.0;

        INIT_FINEBROT_AREA_SIZE = INIT_MANDELBROT_AREA_SIZE;
        INIT_FINEBROT_TARGET_re = INIT_MANDELBROT_TARGET_re;
        INIT_FINEBROT_TARGET_im = INIT_MANDELBROT_TARGET_im;
    }

    @Override
    public void math(Mem mem, double originRe, double originIm) {
        mem.square();
        mem.plus(originRe, originIm);
    }
}