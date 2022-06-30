package fine.fractals.fractal;

import fine.fractals.data.Mem;

public class FineMandelbrot_Top_Zoom extends Fractal {

    public FineMandelbrot_Top_Zoom() {
        super("FineMandelbrot_Top_Zoom");
        ITERATION_MAX = 14800;
        ITERATION_MIN = 42;

        INIT_AREA_DOMAIN_SIZE = 6.0;
        INIT_DOMAIN_TARGET_re = -1.40115518909199;
        INIT_DOMAIN_TARGET_im = 0.0;

        INIT_AREA_IMAGE_SIZE = INIT_AREA_DOMAIN_SIZE;
        INIT_IMAGE_TARGET_re = INIT_DOMAIN_TARGET_re;
        INIT_IMAGE_TARGET_im = INIT_DOMAIN_TARGET_im;
    }

    @Override
    public void math(Mem mem, double originRe, double originIm) {
        mem.square();
        mem.plus(originRe, originIm);
    }
}