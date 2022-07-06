package fine.fractals.data;

public class Mem {

	public static final int NOT = 0;

	public double re;
	public double im;
	/**
	 * pixel x
	 */
	public int px;

	/**
	 * pixel y
	 */
	public int py;

	public Mem() {
	}

	public void plus(double r, double i) {
		re = re + r;
		im = im + i;
	}

	public void square() {
		double temp = (re * re) - (im * im);
		im = 2 * re * im;
		re = temp;
	}

	/**
	 * Q(a*b) = Q(a) * Q(b)
	 */
	public double quadrance() {
		return (re * re) + (im * im);
	}

	public void conjugation() {
		im = -im;
	}
}
