package fine.fractals.data;

import fine.fractals.mandelbrot.MandelbrotState;

public class Element extends ElementAbstract {

	public final double originReT;
	public final double originImX;

	public double lastVisitedReT;
	public double lastVisitedImX;

	public Element(double originReT, double originImX) {
		this.originReT = originReT;
		this.originImX = originImX;
	}

	public void resetAsNew() {
		super.state = MandelbrotState.ActiveNew;

		this.lastVisitedReT = 0;
		this.lastVisitedImX = 0;

		super.lastIteration = 0;
		super.value = null;
	}
}
