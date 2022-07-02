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
	public double sizeRe;
	public double sizeIm;

	private final double[] numbersRe;
	private final double[] numbersIm;

	private double borderLowRe;
	private double borderLowIm;
	private double borderHighRe;
	private double borderHighIm;

	/* Plank's length */
	/* It depends on Height which is resolution domain Y */
	private double plank;

	private final int resolutionHalfRe;
	private final int resolutionHalfIm;

	public static AreaFinebrotImpl AreaFinebrot;

	static {
		log.info("init");
		AreaFinebrot = new AreaFinebrotImpl();
		log.info("initiate");
		AreaFinebrot.initiate();
	}

	private AreaFinebrotImpl() {
		this.resolutionHalfRe = RESOLUTION_WIDTH / 2;
		this.resolutionHalfIm = RESOLUTION_HEIGHT / 2;

		final double scrRatioX = (double) RESOLUTION_HEIGHT / (double) RESOLUTION_WIDTH;
		this.sizeRe = INIT_FINEBROT_AREA_SIZE;
		this.sizeIm = INIT_FINEBROT_AREA_SIZE * scrRatioX;

		this.centerRe = INIT_FINEBROT_TARGET_re;
		this.centerIm = INIT_FINEBROT_TARGET_im;
		this.plank = INIT_FINEBROT_AREA_SIZE / RESOLUTION_WIDTH;
		log.info("plank: " + plank);

		this.numbersRe = new double[RESOLUTION_WIDTH];
		this.numbersIm = new double[RESOLUTION_HEIGHT];
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

	public void domainToScreenCarry(Mem mem, double re, double im) {
		mem.pxRe = (int) Math.round((RESOLUTION_WIDTH * (re - this.centerRe) / this.sizeRe) + resolutionHalfRe);
		if (mem.pxRe >= RESOLUTION_WIDTH || mem.pxRe < 0) {
			mem.pxRe = Mem.NOT;
			return;
		}
		mem.pxIm = (int) Math.round(((RESOLUTION_HEIGHT * (im - this.centerIm)) / this.sizeIm) + resolutionHalfIm);
		if (mem.pxIm >= RESOLUTION_HEIGHT || mem.pxIm < 0) {
			mem.pxIm = Mem.NOT;
		}
	}


	public double screenToDomainCreateRe(int x) {
		return numbersRe[x];
	}

	public double screenToDomainCreateIm(int y) {
		return numbersIm[y];
	}

	/**
	 * call after Zoom in or out
	 */
	private void initiate() {
		this.borderLowRe = centerRe - (sizeRe / 2);
		this.borderHighRe = centerRe + (sizeRe / 2);
		this.borderLowIm = centerIm - (sizeIm / 2);
		this.borderHighIm = centerIm + (sizeIm / 2);
		calculatePoints();
	}

	public void zoomIn() {
		sizeRe = sizeRe * ZOOM;
		sizeIm = sizeIm * ZOOM;
		this.plank = sizeRe / RESOLUTION_WIDTH;
		initiate();
	}

	public String sizeReTString() {
		return Formatter.roundString(this.sizeRe);
	}

	public String sizeImString() {
		return Formatter.roundString(this.sizeIm);
	}

	public void moveToCoordinates() {
		this.centerRe = screenToDomainCreateRe(Target.getScreenFromCornerX());
		this.centerIm = screenToDomainCreateIm(Target.getScreenFromCornerY());
		log.info("Move to: " + this.centerRe + "," + this.centerIm);
	}

	/* Generate domain elements */
	private void calculatePoints() {
		for (int tt = 0; tt < RESOLUTION_WIDTH; tt++) {
			numbersRe[tt] = borderLowRe + (this.plank * tt);
		}
		for (int xx = 0; xx < RESOLUTION_HEIGHT; xx++) {
			numbersIm[xx] = borderLowIm + (this.plank * xx);
		}
	}

	/**
	 * move to zoom target
	 */
	public void moveToInitialCoordinates() {
		this.centerRe = INIT_FINEBROT_TARGET_re;
		this.centerIm = INIT_FINEBROT_TARGET_im;
	}
}
