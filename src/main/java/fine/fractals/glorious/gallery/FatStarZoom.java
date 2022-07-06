package fine.fractals.glorious.gallery;

import fine.fractals.data.Mem;
import fine.fractals.fractal.abst.FractalInfinite;

public class FatStarZoom extends FractalInfinite {

	public FatStarZoom() {
		super("FatStar");
		ITERATION_MAX = 81_000;
		ITERATION_MIN = 8;

		INIT_MANDELBROT_AREA_SIZE = 3.5;
		INIT_MANDELBROT_TARGET_re = 0.0;
		INIT_MANDELBROT_TARGET_im = 0.0;

		INIT_FINEBROT_AREA_SIZE = 0.15;
		INIT_FINEBROT_TARGET_re = 0.5425;
		INIT_FINEBROT_TARGET_im = -0.31;
	}

	@Override
	public void math(Mem m, double originRe, double originIm) {
		m.square();
		m.conjugation();
		m.square();
		m.plus(originRe, originIm);
	}
}
