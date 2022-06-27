package fine.fractals.math.common;

import fine.fractals.math.Mathematician;

/**
 * Do calculation only on HH.
 */
public class HH {

	public double xTemp;
	public double yTemp;

	/**
	 * calculation Thread context
	 */
	public Calculation calculation = new Calculation();

	public static int NOT = Integer.MIN_VALUE;

	protected int it = 0;

	public void res() {
		it = 0;
	}


	public class Calculation {

		public Calculation() {
		}

		public double re;
		public double im;

		public int pxRe;
		public int pxIm;

		/* For fractals coloring to screen */
		public boolean r;
		public boolean g;
		public boolean b;
	}

	public HH() {
	}

	public void plus(double re, double im) {
		calculation.re = calculation.re + re;
		calculation.im = calculation.im + im;
	}

	public void multiplyBy(double re, double im) {
		xTemp = (calculation.re * re) - (calculation.im * im);
		yTemp = (calculation.re * im) + (re * calculation.im);

		calculation.re = xTemp;
		calculation.im = yTemp;
	}

	public void square() {
		xTemp = (calculation.re * calculation.re) - (calculation.im * calculation.im);
		yTemp = 2 * calculation.re * calculation.im;

		calculation.re = xTemp;
		calculation.im = yTemp;
	}

	public void squareA() {
		double t = calculation.re;
		double x = calculation.im;
		xTemp = (t * t) - (x * x);
		yTemp = 4 * t * x;

		calculation.re = xTemp;
		calculation.im = yTemp;
	}


	public void plusInvert() {
		double a = calculation.re;
		double b = calculation.im;

		double quadrance = (a * a) + (b * b);

		xTemp = a / quadrance;
		yTemp = -b / quadrance;

		calculation.re = calculation.re + xTemp;
		calculation.im = calculation.im + yTemp;
	}

	public void minusInvert() {
		double a = calculation.re;
		double b = calculation.im;

		double quadrance = (a * a) + (b * b);

		xTemp = a / quadrance;
		yTemp = -b / quadrance;

		calculation.re = calculation.re - xTemp;
		calculation.im = calculation.im - yTemp;
	}

	public void multiplyByScalar(double scalar) {
		calculation.re = calculation.re * scalar;
		calculation.im = calculation.im * scalar;
	}

	/**
	 * Q(a*b) = Q(a) * Q(b)
	 */
	public double quadrance() {
		return (calculation.re * calculation.re) + (calculation.im * calculation.im);
	}

	public String quadranceGPU() {
		return "(reT * reT) + (imX * imX) + (imY * imY) + (imZ * imZ)";
	}

	public void innerProduct(double reT, double imX) {
		calculation.re = calculation.re * reT;
		calculation.im = calculation.im * imX;
	}

	public void conjugation() {
		calculation.im = -calculation.im;
	}

	public void inverse() {
		double q = quadrance();
		conjugation();
		calculation.re /= q;
		calculation.im /= q;
	}

	// TODO
	public void rotate(HH center) {

		center.conjugation();

		this.multiplyBy(center.calculation.re, center.calculation.im);
		center.multiplyBy(this.calculation.re, this.calculation.im);

		double q = center.quadrance();

		this.calculation.re /= q;
		this.calculation.im /= q;
	}

	/* (a + ib)^3 */
	public void binomial3() {
		xTemp = ((calculation.re * calculation.re * calculation.re) - (3 * calculation.re * calculation.im * calculation.im));
		yTemp = ((3 * calculation.re * calculation.re * calculation.im) - (calculation.im * calculation.im * calculation.im));
		calculation.re = xTemp;
		calculation.im = yTemp;
	}

	/* (a + ib)^4 */
	public void binomial4() {
		xTemp = ((calculation.re * calculation.re * calculation.re * calculation.re)
				- (6 * calculation.re * calculation.re * calculation.re * calculation.im)
				+ (calculation.im * calculation.re * calculation.im * calculation.im));
		yTemp = ((4 * calculation.re * calculation.re * calculation.re * calculation.im)
				- (4 * calculation.re * calculation.im * calculation.im * calculation.im));
		calculation.re = xTemp;
		calculation.im = yTemp;
	}

	/* (a + ib)^5 */
	public void binomial5() {
		xTemp = ((calculation.re * calculation.re * calculation.re * calculation.re * calculation.re)
				- (10 * calculation.re * calculation.re * calculation.re * calculation.im * calculation.im)
				+ (5 * calculation.re * calculation.im * calculation.im * calculation.im * calculation.im));
		yTemp = ((5 * calculation.re * calculation.re * calculation.re * calculation.re * calculation.im)
				- (10 * calculation.re * calculation.re * calculation.im * calculation.im * calculation.im)
				+ (calculation.im * calculation.im * calculation.im * calculation.im * calculation.im));
		calculation.re = xTemp;
		calculation.im = yTemp;
	}


	@Deprecated
	public void exp() {
		xTemp = Math.exp(calculation.re) * Math.cos(calculation.im);
		yTemp = Math.exp(calculation.re) * Math.sin(calculation.im);
		calculation.re = xTemp;
		calculation.im = yTemp;
	}

	public void reciprocal() {
		double scale = calculation.re * calculation.re + calculation.im * calculation.im;
		xTemp = calculation.re / scale;
		yTemp = -calculation.im / scale;
		calculation.re = xTemp;
		calculation.im = yTemp;
	}

	public double reciprocalCreateReT() {
		double scale = calculation.re * calculation.re + calculation.im * calculation.im;
		return calculation.re / scale;
	}

	public double reciprocalCreateImX() {
		double scale = calculation.re * calculation.re + calculation.im * calculation.im;
		return -calculation.im / scale;
	}

	public void hodgepodgeA() {
		xTemp = calculation.re * (1 - calculation.re) - (calculation.im) * (1 - calculation.im);
		yTemp = calculation.re * (1 - calculation.im) * (calculation.re) * (1 - calculation.im);
		calculation.re = xTemp;
		calculation.im = yTemp;
	}

	public void loverChange(Mathematician mathematician) {
		if (mathematician.isPrime(it)) {
			if (calculation.re < 0) {
				calculation.im = calculation.im * 2;
			} else {
				calculation.im = calculation.im / 2;
			}
		}
		it++;
	}

	public void kingChange(Mathematician mathematician) {
		if (mathematician.isPrime(it)) {
			calculation.im = -calculation.im;
		}
		it++;
	}

	public void glassChange(Mathematician mathematician) {
		double x = calculation.im;
		if (mathematician.isFibonacci(it)) {
			calculation.re = x * x;
		}
		it++;
	}

	public void crossChange(Mathematician mathematician) {
		double x = calculation.im;
		double t = calculation.re;
		if (mathematician.isPrime(it)) {
			calculation.re = 0.01 / x;
			calculation.im = 0.01 / t;
		}
		it++;
	}


	public void primeChange(Mathematician mathematician) {
		if (mathematician.isPrime(it)) {
			if (calculation.re > 0) {
				calculation.im = -calculation.im;
			}
		}
		it++;
	}

	public void threeChange() {
		if (it % 3 == 0) {
			calculation.re = -calculation.re;
		}
		it++;
	}

	public void circleInversion(double t, double x) {
		double d = (t * t) + (x * x);
		calculation.re = t / d;
		calculation.im = x / d;
	}
}
