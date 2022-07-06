package fine.fractals.glorious.gallery;

import fine.fractals.data.Mem;
import fine.fractals.fractal.abst.FractalInfinite;

public class InfiniteFinebrot extends FractalInfinite {

	public InfiniteFinebrot() {
		super("InfiniteFinebrot");
		ITERATION_MAX = 180_000;
		ITERATION_MIN = 3000;

		INIT_MANDELBROT_AREA_SIZE = 2.6;
		INIT_MANDELBROT_TARGET_re = -0.5;
		INIT_MANDELBROT_TARGET_im = 0.0;

		INIT_FINEBROT_AREA_SIZE = INIT_MANDELBROT_AREA_SIZE;
		INIT_FINEBROT_TARGET_re = INIT_MANDELBROT_TARGET_re;
		INIT_FINEBROT_TARGET_im = INIT_MANDELBROT_TARGET_im;
	}

	@Override
	public void math(Mem m, double originRe, double originIm) {
		m.square();
		m.plus(originRe, originIm);
	}
}
