package fine.fractals.context.finebrot;

import fine.fractals.data.Mem;
import fine.fractals.formatter.Formatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.ApplicationImpl.ZOOM;
import static fine.fractals.context.TargetImpl.Target;
import static fine.fractals.fractal.Fractal.*;

public class AreaFinebrotImpl {

	private static final Logger log = LogManager.getLogger(AreaFinebrotImpl.class);

	/* position of the centre of image area */
	public double centerRe;
	public double centerIm;

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

	public static AreaFinebrotImpl AreaFinebrot;

	static {
		log.info("init");
		AreaFinebrot = new AreaFinebrotImpl();
		log.info("initiate");
		AreaFinebrot.initiate();
	}

	private AreaFinebrotImpl() {

		this.resolutionT = RESOLUTION_WIDTH;
		this.resolutionX = RESOLUTION_HEIGHT;
		this.resolutionHalfT = this.resolutionT / 2;
		this.resolutionHalfX = this.resolutionX / 2;

		double scrRatioX = (double) resolutionX / (double) resolutionT;

		this.sizeReT = INIT_AREA_IMAGE_SIZE;
		this.sizeImX = INIT_AREA_IMAGE_SIZE * scrRatioX;


		this.centerRe = INIT_IMAGE_TARGET_re;
		this.centerIm = INIT_IMAGE_TARGET_im;
		this.plank = INIT_AREA_IMAGE_SIZE / resolutionT;

		log.info("plank: " + plank);

		this.numbersReT = new double[resolutionT];
		this.numbersImX = new double[resolutionX];
	}

	public boolean contains(Mem mem) {
		return mem.re > this.borderLowRe
				&& mem.re < this.borderHighRe
				&& mem.im > this.borderLowIm
				&& mem.im < this.borderHighIm;
	}

	public boolean contains(double re, double im) {
		return re > this.borderLowRe
				&& re < this.borderHighRe
				&& im > this.borderLowIm
				&& im < this.borderHighIm;
	}

	public boolean domainToScreenCarry(Mem mem, double reT, double imX) {
		mem.pxRe = (int) Math.round((resolutionT * (reT - this.centerRe) / this.sizeReT) + resolutionHalfT);
		if (mem.pxRe >= resolutionT || mem.pxRe < 0) {
			mem.pxRe = Mem.NOT;
			return false;
		}

		mem.pxIm = (int) Math.round(((resolutionX * (imX - this.centerIm)) / this.sizeImX) + resolutionHalfX);
		if (mem.pxIm >= resolutionX || mem.pxIm < 0) {
			mem.pxIm = Mem.NOT;
			return false;
		}
		return true;
	}


	public double screenToDomainCreateReT(int t) {
		return numbersReT[t];
	}

	public double screenToDomainCreateImX(int x) {
		return numbersImX[x];
	}

	/**
	 * call after Zoom in or out
	 */
	private void initiate() {
		this.borderLowRe = centerRe - (sizeReT / 2);
		this.borderHighRe = centerRe + (sizeReT / 2);
		this.borderLowIm = centerIm - (sizeImX / 2);
		this.borderHighIm = centerIm + (sizeImX / 2);
		calculatePoints();
	}

	public void zoomIn() {
		sizeReT = sizeReT * ZOOM;
		sizeImX = sizeImX * ZOOM;
		this.plank = sizeReT / resolutionT;
		initiate();
	}

	public String sizeReTString() {
		return Formatter.roundString(this.sizeReT);
	}

	public String sizeImXString() {
		return Formatter.roundString(this.sizeImX);
	}

	public void moveToCoordinates() {
		this.centerRe = screenToDomainCreateReT(Target.getScreenFromCornerT());
		this.centerIm = screenToDomainCreateImX(Target.getScreenFromCornerX());
		log.info("Move to: " + this.centerRe + "," + this.centerIm);
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
		this.centerRe = INIT_IMAGE_TARGET_re;
		this.centerIm = INIT_IMAGE_TARGET_im;
	}
}
