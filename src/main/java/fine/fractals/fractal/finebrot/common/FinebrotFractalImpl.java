package fine.fractals.fractal.finebrot.common;

import fine.fractals.data.annotation.ThreadSafe;
import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.perfect.coloring.common.PerfectColorDistributionAbstract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;

public abstract class FinebrotFractalImpl {

	/**
	 * Instantiated by specific fractal
	 * A specific fractal which is going to be calculated
	 */
	public static FinebrotFractalImpl FinebrotFractal;
	/**
	 * Instantiated by fractal type
	 */
	public static PerfectColorDistributionAbstract PerfectColorDistribution;
	/**
	 * Instantiated by fractal type
	 */
	public static PathsFinebrotAbstract PathsFinebrot;

	private static final Logger log = LogManager.getLogger(FinebrotFractalImpl.class);

	public static String NAME;

	public static int ITERATION_MAX;
	public static int ITERATION_MIN;
	public static double INIT_FINEBROT_AREA_SIZE;
	public static double INIT_FINEBROT_TARGET_re;
	public static double INIT_FINEBROT_TARGET_im;

	public static double INIT_MANDELBROT_AREA_SIZE;
	public static double INIT_MANDELBROT_TARGET_re;
	public static double INIT_MANDELBROT_TARGET_im;

	/**
	 * 4 worked well for all fractals so far
	 * 4 is distance from (0, 0)
	 */
	public static final int CALCULATION_BOUNDARY = 4;

	public FinebrotFractalImpl() {
		log.info("Fractal " + this.getClass().getSimpleName());
	}

	private int iterationMax0;

	@ThreadSafe
	public abstract boolean calculatePath(MandelbrotElement el, final ArrayList<double[]> path);

	public void update() {
		log.debug("update()");
		if (iterationMax0 == 0) {
			iterationMax0 = ITERATION_MAX;
		}
		final double size0 = INIT_FINEBROT_AREA_SIZE;
		final double sizeNow = AreaFinebrot.sizeIm;
		final double magnification = (size0 / sizeNow); // length 8 4 2 1 1/2 1/4 1/8
		final double iterationDiv = (magnification * iterationMax0) - iterationMax0;
		log.debug("magnification" + magnification);
		log.debug("iterationDiv" + iterationDiv);

		ITERATION_MAX = (int) (iterationMax0 + (iterationDiv / 5000.0));
		if (ITERATION_MAX > 300_000_000) {
			ITERATION_MAX = 300_000_000;
		}
		log.info("ITERATION_MAX = " + ITERATION_MAX);
	}
}
