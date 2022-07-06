package fine.fractals.glorious.gallery;

import fine.fractals.data.MemEuler;
import fine.fractals.fractal.abst.FractalEuler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.Main.*;
import static fine.fractals.context.ApplicationImpl.Application;
import static fine.fractals.context.ApplicationImpl.REPEAT;
import static fine.fractals.data.ResolutionMultiplier.square_3;
import static fine.fractals.math.MathematicianImpl.Mathematician;

public class Euler extends FractalEuler {

	private static final Logger log = LogManager.getLogger(Euler.class);

	public Euler() {
		super("Science");
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

		FRACTAL = new Euler();

		REPEAT = false;
		SAVE_IMAGES = true;
		RESOLUTION_HEIGHT = 1080;
		RESOLUTION_WIDTH = 1920;
		RESOLUTION_MULTIPLIER = square_3;

		if (COREs < 1) {
			COREs = 1;
		}
		log.info("cores: " + COREs);

		log.info("Euler start");
		Application.execute();

		log.info("Euler finished");
	}
}
