package fine.fractals.fractal;

import fine.fractals.data.Mem;

public class Finebrot_Side extends Fractal {

    public Finebrot_Side() {
        super("FineMandelbrot_Side_Zoom");
        ITERATION_MAX = 14800;
        ITERATION_MIN = 42;

        INIT_AREA_DOMAIN_SIZE = 6.0;
        INIT_DOMAIN_TARGET_re = -0.10675625916322415;
        INIT_DOMAIN_TARGET_im = -0.8914368889277283;

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