package fine.fractals.data;

import static java.lang.Integer.MIN_VALUE;

public class Mem {

	public double xTemp;
	public double yTemp;

	public static final int NOT = MIN_VALUE;

	public double re;
	public double im;
	public int pxRe;
	public int pxIm;

	public Mem() {
	}

	public void plus(double re, double im) {
		this.re = this.re + re;
		this.im = this.im + im;
	}

	public void square() {
		xTemp = (this.re * this.re) - (this.im * this.im);
		yTemp = 2 * this.re * this.im;

		this.re = xTemp;
		this.im = yTemp;
	}

	/**
	 * Q(a*b) = Q(a) * Q(b)
	 */
	public double quadrance() {
		return (this.re * this.re) + (this.im * this.im);
	}
}
