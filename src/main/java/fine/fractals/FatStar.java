package fine.fractals;

import fine.fractals.data.Mem;
import fine.fractals.fractal.finebrot.infinite.FractalInfinite;

import static fine.fractals.context.ApplicationImpl.*;
import static fine.fractals.context.ApplicationImpl.Application;
import static fine.fractals.data.ResolutionMultiplier.none;
import static fine.fractals.data.ResolutionMultiplier.square_11;

public class FatStar extends FractalInfinite {

	public FatStar() {
		NAME = "FatStar";

		ITERATION_MAX = 22000;
		ITERATION_MIN = 42;

		INIT_MANDELBROT_AREA_SIZE = 3.5;
		INIT_MANDELBROT_TARGET_re = 0.0;
		INIT_MANDELBROT_TARGET_im = 0.0;

		INIT_FINEBROT_AREA_SIZE = INIT_MANDELBROT_AREA_SIZE;
		INIT_FINEBROT_TARGET_re = INIT_MANDELBROT_TARGET_re;
		INIT_FINEBROT_TARGET_im = INIT_MANDELBROT_TARGET_im;
	}

	@Override
	public void math(Mem m, double originRe, double originIm) {
		m.square();
		m.conjugation();
		m.square();
		m.plus(originRe, originIm);
	}

	public static void main(String[] args) {

		FinebortFractal = new FatStar();

		REPEAT = false;
		SAVE_IMAGES = false;
		RESOLUTION_MULTIPLIER = square_11;

		Application.execute();
	}
}