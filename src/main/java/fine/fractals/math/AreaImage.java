package fine.fractals.math;

import fine.fractals.Application;
import fine.fractals.fractal.Fractal;
import fine.fractals.math.common.HH;
import fine.fractals.ui.Formatter;
import fine.fractals.ui.OneTarget;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AreaImage {

	private static final Logger log = LogManager.getLogger(AreaImage.class);

	/* position of the centre of image area */
	public double centerReT;
	public double centerImX;

	/* size of image area */
	public double sizeReT;
	public double sizeImX;

	private final double[] numbersReT;
	private final double[] numbersImX;

	private double borderLowRe;
	private double borderLowIm;
	private double borderHighRe;
	private double borderHighIm;

	/* Plank's length */
	/* It depends on Height which is resolution domain Y */
	private double plank;

	private int resolutionT;
	private int resolutionX;
	private int resolutionHalfT;
	private int resolutionHalfX;

	public AreaImage(double size, double centerReT, double centerImX, int resolutionT, int resolutionX) {
		this.resolutionT = resolutionT;
		this.resolutionX = resolutionX;
		this.resolutionHalfT = this.resolutionT / 2;
		this.resolutionHalfX = this.resolutionX / 2;

		double scrRatioX = (double) resolutionX / (double) resolutionT;

		this.sizeReT = size;
		this.sizeImX = size * scrRatioX;


		this.centerReT = centerReT;
		this.centerImX = centerImX;
		this.plank = size / resolutionT;

		log.info("plank: " + plank);

		this.numbersReT = new double[resolutionT];
		this.numbersImX = new double[resolutionX];

		// Transformation.init(0);
		initiate();
	}

	public boolean contains(HH hh) {
		return hh.calculation.re > this.borderLowRe
				&& hh.calculation.re < this.borderHighRe
				&& hh.calculation.im > this.borderLowIm
				&& hh.calculation.im < this.borderHighIm;
	}

	public boolean contains(double re, double im) {
		return re > this.borderLowRe
				&& re < this.borderHighRe
				&& im > this.borderLowIm
				&& im < this.borderHighIm;
	}

	public void writeBorders() {
		System.out.println(this.borderLowRe + " --> " + this.borderHighRe);
		System.out.println(this.borderLowIm + " --> " + this.borderHighIm);
	}

	public boolean domainToScreenCarry(HH hh, double reT, double imX) {
		hh.calculation.pxRe = (int) Math.round((resolutionT * (reT - this.centerReT) / this.sizeReT) + resolutionHalfT);
		if (hh.calculation.pxRe >= resolutionT || hh.calculation.pxRe < 0) {
			hh.calculation.pxRe = HH.NOT;
			return false;
		}

		hh.calculation.pxIm = (int) Math.round(((resolutionX * (imX - this.centerImX)) / this.sizeImX) + resolutionHalfX);
		if (hh.calculation.pxIm >= resolutionX || hh.calculation.pxIm < 0) {
			hh.calculation.pxIm = HH.NOT;
			return false;
		}
		return true;
	}


	/** circle inversion **/
	public void transformCircle(HH hh, double re, double im) {
		double cDiam = 0.25;
		double cOriginRe = -1;
		double cOriginIm = 0;

		double kk = cDiam * cDiam;
		double abs = (re - cOriginRe) * (re - cOriginRe) + (im - cOriginIm) * (im - cOriginIm);
		hh.calculation.re = kk * (re - cOriginRe) / abs;
		hh.calculation.im = kk * (im - cOriginIm) / abs;
	}


	public double screenToDomainCreateReT(int t) {
		return numbersReT[t];
	}

	public double screenToDomainCreateImX(int x) {
		return numbersImX[x];
	}

	public void screenToDomainCarry(HH hh, int t, int x) {
		hh.calculation.re = numbersReT[t];
		hh.calculation.im = numbersImX[x];
	}


	/**
	 * call after Zoom in or out
	 */
	private void initiate() {
		this.borderLowRe = centerReT - (sizeReT / 2);
		this.borderHighRe = centerReT + (sizeReT / 2);
		this.borderLowIm = centerImX - (sizeImX / 2);
		this.borderHighIm = centerImX + (sizeImX / 2);

		// this.borderLow.imY = center.imY - (size.imY / 2);
		// this.borderHigh.imY = center.imY + (size.imY / 2);
		// this.borderLow.imZ = center.imZ - (size.imZ / 2);
		// this.borderHigh.imZ = center.imZ + (size.imZ / 2);

		calculatePoints();
	}

	public void zoomIn() {
		sizeReT = sizeReT * Application.ZOOM;
		sizeImX = sizeImX * Application.ZOOM;
		this.plank = sizeReT / resolutionT;
		initiate();
	}

	public String sizeReTString() {
		return Formatter.roundString(this.sizeReT);
	}

	public String sizeImXString() {
		return Formatter.roundString(this.sizeImX);
	}

	public void moveToCoordinates(OneTarget target) {
		this.centerReT = screenToDomainCreateReT(target.getScreenFromCornerT());
		this.centerImX = screenToDomainCreateImX(target.getScreenFromCornerX());
		//this.center.imY = h.imY;
		//this.center.imZ = h.imZ;
		log.info("Move to: " + this.centerReT + "," + this.centerImX);
	}

	/* Generate domain elements */
	private void calculatePoints() {
		for (int tt = 0; tt < resolutionT; tt++) {
			numbersReT[tt] = borderLowRe + (this.plank * tt);
		}
		for (int xx = 0; xx < resolutionX; xx++) {
			numbersImX[xx] = borderLowIm + (this.plank * xx);
		}
	}

	/**
	 * move to zoom target
	 */
	public void moveToInitialCoordinates() {
		this.centerReT = Fractal.INIT_IMAGE_TARGET_reT;
		this.centerImX = Fractal.INIT_IMAGE_TARGET_imX;
	}
}
