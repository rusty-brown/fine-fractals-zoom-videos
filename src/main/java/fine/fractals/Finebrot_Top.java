package fine.fractals;

import fine.fractals.color.PalettePurpleWhite;
import fine.fractals.data.Mem;
import fine.fractals.fractal.finebrot.finite.FractalFinite;

import static fine.fractals.context.ApplicationImpl.*;
import static fine.fractals.data.ResolutionMultiplier.square_alter;

public class Finebrot_Top extends FractalFinite {

    public Finebrot_Top() {
        NAME = "Finebrot_Top";

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

    public static void main(String[] args) {

        FinebortFractal = new Finebrot_Top();

        PalettePurpleWhite.init();

        REPEAT = true;
        SAVE_IMAGES = false;
        RESOLUTION_HEIGHT = 1000;
        RESOLUTION_WIDTH = 1000;
        RESOLUTION_MULTIPLIER = square_alter;

        Application.execute();
    }
}
