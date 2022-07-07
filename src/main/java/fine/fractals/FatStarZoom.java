package fine.fractals;

import fine.fractals.data.Mem;
import fine.fractals.fractal.finebrot.infinite.FractalInfinite;

import static fine.fractals.context.ApplicationImpl.*;
import static fine.fractals.context.ApplicationImpl.Application;
import static fine.fractals.data.ResolutionMultiplier.square_11;

public class FatStarZoom extends FractalInfinite {

	public FatStarZoom() {
		NAME = "FatStar";

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

	public static void main(String[] args) {

		FinebortFractal = new FatStar();

		REPEAT = false;
		SAVE_IMAGES = false;
		RESOLUTION_MULTIPLIER = square_11;

		Application.execute();
	}
}
