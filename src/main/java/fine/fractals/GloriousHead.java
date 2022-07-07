package fine.fractals;

import fine.fractals.data.MemPhoenix;
import fine.fractals.fractal.finebrot.phoenix.FractalPhoenix;

import static fine.fractals.context.ApplicationImpl.*;
import static fine.fractals.data.ResolutionMultiplier.square_11;

public class GloriousHead extends FractalPhoenix {

	public GloriousHead() {
		NAME = "GloriousHead";

		c = 0.35;
		p = -0.25;

		ITERATION_MAX = 2500;
		ITERATION_MIN = 8;

		INIT_MANDELBROT_AREA_SIZE = 2.5;
		INIT_MANDELBROT_TARGET_re = -0.25;
		INIT_MANDELBROT_TARGET_im = 0.0;

		INIT_FINEBROT_AREA_SIZE = INIT_MANDELBROT_AREA_SIZE;
		INIT_FINEBROT_TARGET_re = INIT_MANDELBROT_TARGET_re;
		INIT_FINEBROT_TARGET_im = INIT_MANDELBROT_TARGET_im;
	}

	@Override
	public void math(MemPhoenix m, double originRe, double originIm) {
		m.square();

		m.re += c;
		m.re += p * m.prev_prev_re;
		m.im += p * m.prev_prev_im;

		/* previous iteration */
		m.prev_prev_re = m.prev_re;
		m.prev_prev_im = m.prev_im;
		m.prev_re = m.re;
		m.prev_im = m.im;

		m.plus(originRe, originIm);
	}

	public static void main(String[] args) {

		FinebortFractal = new GloriousHead();

		REPEAT = false;
		SAVE_IMAGES = false;
		RESOLUTION_MULTIPLIER = square_11;

		Application.execute();
	}
}
