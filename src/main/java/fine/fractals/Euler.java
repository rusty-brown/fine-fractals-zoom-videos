package fine.fractals;

import fine.fractals.color.euler.Palette3RGB;
import fine.fractals.data.mem.MemEuler;
import fine.fractals.fractal.finebrot.euler.FractalEuler;

import static fine.fractals.color.common.PaletteEulerImpl.PaletteEuler3;
import static fine.fractals.data.mandelbrot.ResolutionMultiplier.none;
import static fine.fractals.machine.ApplicationImpl.Application;
import static fine.fractals.machine.ApplicationImpl.REPEAT;
import static fine.fractals.math.MathematicianImpl.Mathematician;

public final class Euler extends FractalEuler {

	public Euler() {
		NAME = "Euler";

		ITERATION_MAX = 80000;
		ITERATION_MIN = 42;

		INIT_MANDELBROT_AREA_SIZE = 4.0;
		INIT_MANDELBROT_TARGET_re = 0.0;
		INIT_MANDELBROT_TARGET_im = 0.0;

		INIT_FINEBROT_AREA_SIZE = INIT_MANDELBROT_AREA_SIZE;
		INIT_FINEBROT_TARGET_re = INIT_MANDELBROT_TARGET_re;
		INIT_FINEBROT_TARGET_im = INIT_MANDELBROT_TARGET_im;

		Mathematician.initPrimes();
	}

	@Override
	public void math(MemEuler m, double originRe, double originIm) {
		m.square();
		m.plus(originRe, originIm);
		m.euler();
		m.square();
		m.plus(originRe, originIm);
	}

	public static void main(String[] args) {

		RESOLUTION_WIDTH = 1920;
		RESOLUTION_HEIGHT = 1080;
		RESOLUTION_MULTIPLIER = none;

		REPEAT = false;
		SAVE_IMAGES = false;
		FinebrotFractal = new Euler();
		PaletteEuler3 = new Palette3RGB();

		Application.execute();
	}
}
