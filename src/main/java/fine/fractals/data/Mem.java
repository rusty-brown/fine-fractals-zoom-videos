package fine.fractals.data;

public class Mem {

	public double xTemp;

	public static final int NOT = 0;

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
		this.im = 2 * this.re * this.im;
		this.re = xTemp;
	}

	/**
	 * Q(a*b) = Q(a) * Q(b)
	 */
	public double quadrance() {
		return (this.re * this.re) + (this.im * this.im);
	}
}
