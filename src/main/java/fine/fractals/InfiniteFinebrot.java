package fine.fractals;

import fine.fractals.color.euler.Palette3RGB;
import fine.fractals.data.Mem;
import fine.fractals.fractal.finebrot.infinite.FractalInfinite;

import static fine.fractals.context.ApplicationImpl.*;
import static fine.fractals.data.ResolutionMultiplier.none;
import static fine.fractals.data.ResolutionMultiplier.square_11;

public class InfiniteFinebrot extends FractalInfinite {

	public InfiniteFinebrot() {
		NAME = "InfiniteFinebrot";

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

	public static void main(String[] args) {

		FinebrotFractal = new InfiniteFinebrot();

		Palette3RGB.init();

		RESOLUTION_WIDTH = 1920;
		RESOLUTION_HEIGHT = 1080;
		RESOLUTION_MULTIPLIER = none;

		REPEAT = false;
		SAVE_IMAGES = false;

		Application.execute();
	}
}
