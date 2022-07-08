package fine.fractals.data.mandelbrot;

public class MandelbrotElement extends MandelbrotElementAbstract {

	public final double originRe;
	public final double originIm;

	public MandelbrotElement(double originRe, double originIm) {
		this.originRe = originRe;
		this.originIm = originIm;
	}
}
