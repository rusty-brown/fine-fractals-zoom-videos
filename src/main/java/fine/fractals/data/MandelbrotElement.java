package fine.fractals.data;

public class MandelbrotElement extends MandelbrotElementAbstract {

	public final double originRe;
	public final double originIm;

	// TODO
	public double lastVisitedRe;
	public double lastVisitedIm;

	public MandelbrotElement(double originRe, double originIm) {
		this.originRe = originRe;
		this.originIm = originIm;
	}
}
