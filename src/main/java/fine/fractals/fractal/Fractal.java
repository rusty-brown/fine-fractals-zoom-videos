package fine.fractals.fractal;

import fine.fractals.data.Mem;
import fine.fractals.formatter.Formatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;

public abstract class Fractal {

	private static final Logger log = LogManager.getLogger(Fractal.class);

	public static String NAME;

	public static int ITERATION_MAX;
	public static int ITERATION_MIN;
	public static double INIT_AREA_IMAGE_SIZE;
	public static double INIT_IMAGE_TARGET_re;
	public static double INIT_IMAGE_TARGET_im;

	public static double INIT_AREA_DOMAIN_SIZE;
	public static double INIT_DOMAIN_TARGET_re;
	public static double INIT_DOMAIN_TARGET_im;

	/*
	 * 4 worked well for all fractals so far
	 * 4 is distance from (0, 0)
	 * This will be compared with quadrance
	 */
	public static int CALCULATION_BOUNDARY = 4;

	public Fractal(String name) {
		NAME = name;
	}

	/*
	 * Calculate only next position.
	 * Color can be known only after full path finished.
 	 */
	public abstract void math(Mem mem, double reT, double imX);

	private static int iterationMax0;

	public static void update() {
		log.info("update");
		if (iterationMax0 == 0) {
			iterationMax0 = ITERATION_MAX;
		}
		final double size0 = INIT_AREA_IMAGE_SIZE;
		final double sizeNow = AreaFinebrot.sizeImX;

		final double magn = (size0 / sizeNow); // length 8 4 2 1 1/2 1/4 1/8

		final double iterationDiv = (magn * iterationMax0) - iterationMax0;
		log.info("(" + magn + " | " + iterationDiv + ")");

		ITERATION_MAX = (int) (iterationMax0 + (iterationDiv / 5000.0));

		if (ITERATION_MAX > 300_000_000) {
			ITERATION_MAX = 300_000_000;
			log.info("ITERATION_MAX " + ITERATION_MAX);
		}

		log.info("Updated: " + Formatter.roundString(AreaFinebrot.sizeReT) + " : " + ITERATION_MAX);
	}

}
