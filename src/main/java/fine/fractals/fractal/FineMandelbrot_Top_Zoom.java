package fine.fractals.fractal;

import fine.fractals.math.Mathematician;
import fine.fractals.math.common.HH;

public class FineMandelbrot_Top_Zoom extends Fractal {

    private Mathematician mathematician = new Mathematician();

    public FineMandelbrot_Top_Zoom() {
        super("FineMandelbrot_Top_Zoom");
        ITERATION_MAX = 14800;
        ITERATION_MIN = 42;

        INIT_AREA_DOMAIN_SIZE = 6.0;
        INIT_DOMAIN_TARGET_re = -1.40115518909199;
        INIT_DOMAIN_TARGET_im = 0.0;

        INIT_AREA_IMAGE_SIZE = INIT_AREA_DOMAIN_SIZE;
        INIT_IMAGE_TARGET_reT = INIT_DOMAIN_TARGET_re;
        INIT_IMAGE_TARGET_imX = INIT_DOMAIN_TARGET_im;

        Mathematician.initialize();
    }

    @Override
    public void math(HH hh, double originRe, double originIm) {
        hh.square();
        hh.plus(originRe, originIm);
    }
}