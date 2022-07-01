package fine.fractals.data;

import static fine.fractals.mandelbrot.MandelbrotState.ActiveNew;

public class MandelbrotElement extends MandelbrotElementAbstract {

	public final double originRe;
	public final double originIm;

	public double lastVisitedRe;
	public double lastVisitedIm;

	public MandelbrotElement(double originRe, double originIm) {
		this.originRe = originRe;
		this.originIm = originIm;
	}

	public void resetAsNew() {
		super.state = ActiveNew;

		this.lastVisitedRe = 0;
		this.lastVisitedIm = 0;

		super.lastIteration = 0;
		super.value = null;
	}
}
