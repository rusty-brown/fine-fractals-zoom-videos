package fine.fractals.fractal.longorbits;

import fine.fractals.fractal.FineMandelbrot;
import fine.fractals.fractal.Fractal;
import fine.fractals.math.AreaDomain;
import fine.fractals.math.Mathematician;
import fine.fractals.math.common.HH;

public class TiaraZoomSide extends Fractal {

	private Mathematician mathematician = new Mathematician();

	// This takes like 40+ hours
	// private static final int r = 10000;
	// private static final int m = 101; // million

	// seems OK 10h+
	// private static final int r = 10500;
	// private static final int m = 51;

	public TiaraZoomSide() {
		super("TiaraZoomSide");
		ITERATION_MAX = 180_000;
		ITERATION_MIN = 810;

		INIT_AREA_DOMAIN_SIZE = 1.5;
		INIT_DOMAIN_TARGET_re = 0.035;
		INIT_DOMAIN_TARGET_im = -0.755;

		INIT_AREA_IMAGE_SIZE = 0.4;
		INIT_IMAGE_TARGET_reT = 0.035;
		INIT_IMAGE_TARGET_imX = -0.755;


		// OPTIMIZE_SYMMETRY = true;

		ONLY_LONG_ORBITS = true;

		Mathematician.initialize();
	}

	@Override
	public void math(HH hh, double originReT, double originImX) {
		hh.square();
		hh.plus(originReT, originImX);
	}

	@Override
	public boolean optimize(double reT, double imX) {
		double delta = AreaDomain.ME.plank() * 10;
		if (-0.75 - reT < delta * 2 && reT + 0.75 < delta * 2) {
			// gap between circle and cardioid
			return true;
		}

		boolean a = FineMandelbrot.isOutsideCardioid(reT, imX) && FineMandelbrot.isOutsideCircle(reT, imX);
		if (a) return true;
		a = FineMandelbrot.isOutsideCardioid(reT + delta, imX) && FineMandelbrot.isOutsideCircle(reT + delta, imX);
		if (a) return true;
		a = FineMandelbrot.isOutsideCardioid(reT - delta, imX) && FineMandelbrot.isOutsideCircle(reT - delta, imX);
		if (a) return true;
		a = FineMandelbrot.isOutsideCardioid(reT, imX + delta) && FineMandelbrot.isOutsideCircle(reT, imX + delta);
		if (a) return true;
		a = FineMandelbrot.isOutsideCardioid(reT, imX - delta) && FineMandelbrot.isOutsideCircle(reT, imX - delta);
		return a;
	}

	@Override
	public void colorsFor(HH hh, int counter, int size) {
		hh.calculation.r = false;
		hh.calculation.g = false;
		hh.calculation.b = false;
		boolean change = false;

		if (mathematician.isPrime(counter)) {
			hh.calculation.r = true;
			change = true;
		}
		if (mathematician.isPrime(size)) {
			hh.calculation.g = true;
			change = true;
		}

		if (!change) {
			hh.calculation.b = true;
		}
	}


}