package fine.fractals.fractal.abst;

import fine.fractals.data.MandelbrotElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;

public abstract class Fractal {

	private static final Logger log = LogManager.getLogger(Fractal.class);

	public static String NAME;

	public static int ITERATION_MAX;
	public static int ITERATION_MIN;
	public static double INIT_FINEBROT_AREA_SIZE;
	public static double INIT_FINEBROT_TARGET_re;
	public static double INIT_FINEBROT_TARGET_im;

	public static double INIT_MANDELBROT_AREA_SIZE;
	public static double INIT_MANDELBROT_TARGET_re;
	public static double INIT_MANDELBROT_TARGET_im;

	/*
	 * 4 worked well for all fractals so far
	 * 4 is distance from (0, 0)
	 * This will be compared with quadrance
	 */
	public static final int CALCULATION_BOUNDARY = 4;

	public Fractal(String name) {
		NAME = name;
	}

	private static int iterationMax0;

	public abstract boolean calculatePath(MandelbrotElement el, final ArrayList<double[]> path);

	public static void update() {
		log.debug("update()");
		if (iterationMax0 == 0) {
			iterationMax0 = ITERATION_MAX;
		}
		final double size0 = INIT_FINEBROT_AREA_SIZE;
		final double sizeNow = AreaFinebrot.sizeIm;

		final double magnification = (size0 / sizeNow); // length 8 4 2 1 1/2 1/4 1/8

		final double iterationDiv = (magnification * iterationMax0) - iterationMax0;
		log.debug("(" + magnification + " | " + iterationDiv + ")");

		ITERATION_MAX = (int) (iterationMax0 + (iterationDiv / 5000.0));

		if (ITERATION_MAX > 300_000_000) {
			ITERATION_MAX = 300_000_000;
		}

		log.info("ITERATION_MAX = " + ITERATION_MAX);
	}
}