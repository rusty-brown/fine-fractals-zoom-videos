package fine.fractals.fractal.mandelbrot;

import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.data.mem.Mem;
import fine.fractals.machine.FractalMachine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

import static fine.fractals.context.ApplicationImpl.*;
import static fine.fractals.data.mandelbrot.MandelbrotMaskColors.*;
import static fine.fractals.data.mandelbrot.ResolutionMultiplier.none;
import static fine.fractals.data.mandelbrot.ResolutionMultiplier.square_alter;
import static fine.fractals.fractal.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.images.FractalImage.MandelbrotMaskImage;

class PixelsMandelbrotImpl {

	private static final Logger log = LogManager.getLogger(PixelsMandelbrotImpl.class);

	final MandelbrotElement[][] elementsStaticMandelbrot = new MandelbrotElement[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
	private final ArrayList<MandelbrotElement> elementsToRemember = new ArrayList<>();

	private MandelbrotElement bestMatch;
	private int bestMatchAtX;
	private int bestMatchAtY;
	private double dist;

	static boolean maskDone = true;

	private static boolean firstDomainExecution = true;

	static final PixelsMandelbrotImpl PixelsMandelbrot = new PixelsMandelbrotImpl();

	private PixelsMandelbrotImpl() {
	}

	public final void domainScreenCreateInitialization() {
		log.info("domainScreenCreateInitialization()");
		for (int x = 0; x < RESOLUTION_WIDTH; x++) {
			for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
				MandelbrotElement element = new MandelbrotElement(AreaMandelbrot.screenToDomainRe(x), AreaMandelbrot.screenToDomainIm(y));
				elementsStaticMandelbrot[x][y] = element;
			}
		}
	}

	private boolean odd = true;

	public ArrayList<MandelbrotElement> fetchDomainFull() {
		log.debug("fetchDomainFull()");

		MandelbrotElement elementZero;
		final ArrayList<MandelbrotElement> domainFull = new ArrayList<>();

		for (int x = 0; x < RESOLUTION_WIDTH; x++) {
			for (int y = 0; y < RESOLUTION_HEIGHT; y++) {

				elementZero = elementsStaticMandelbrot[x][y];

				boolean isActive = elementZero != null && elementZero.isActiveAny();

				/* First calculation */
				if (isActive) {
					domainFull.add(elementZero);

					if (!firstDomainExecution) {
						// TODO try also with: OR FractalMachine.someNeighboursFinishedLong(t, x, elementsScreen);
						if (RESOLUTION_MULTIPLIER != none) {
							wrap(domainFull, elementZero);
						}
					}
				}
			}
		}
		/* Don't do any wrapping the first time */
		firstDomainExecution = false;
		/* Switch wrapping the next time */
		odd = !odd;

		log.info("Domain elements to calculate: " + domainFull.size());
		return domainFull;
	}

	private void wrap(ArrayList<MandelbrotElement> domainFull, MandelbrotElement elementZero) {
		if (RESOLUTION_MULTIPLIER == square_alter) {
			final double d = AreaMandelbrot.plank() / 3;
			if (odd) {
				domainFull.add(new MandelbrotElement(elementZero.originRe + d, elementZero.originIm + d));
				domainFull.add(new MandelbrotElement(elementZero.originRe - d, elementZero.originIm - d));
			} else {
				domainFull.add(new MandelbrotElement(elementZero.originRe - d, elementZero.originIm + d));
				domainFull.add(new MandelbrotElement(elementZero.originRe + d, elementZero.originIm - d));
			}
		} else {
			final int multiplier;
			switch (RESOLUTION_MULTIPLIER) {
				case square_3 -> multiplier = 3;
				case square_5 -> multiplier = 5;
				case square_11 -> multiplier = 11;
				case square_51 -> multiplier = 51;
				case square_101 -> multiplier = 101;
				default -> throw new RuntimeException("unknown RESOLUTION_MULTIPLIER");
			}

			final double pn = AreaMandelbrot.plank() / multiplier;
			final int half = (multiplier - 1) / 2;
			/* This fills the pixel with multiple points */
			for (int x = -half; x <= half; x++) {
				for (int y = -half; y <= half; y++) {
					if (x != 0 || y != 0) {
						domainFull.add(new MandelbrotElement(elementZero.originRe + (x * pn), elementZero.originIm + (y * pn)));
					}
					/* else do nothing, there already is element 0 in the center of this pixel */
				}
			}
		}
	}

	private void dropBestMatchToEmptyNeighbour(Mem m, int x, int y, ArrayList<MandelbrotElement> conflictsOnPixel) {
		bestMatch = null;
		dist = 0;
		double distMin = 42;
		for (MandelbrotElement element : conflictsOnPixel) {
			/* up */
			distMin = tryBestMatch(m, x - 1, y + 1, element, distMin);
			distMin = tryBestMatch(m, x, y + 1, element, distMin);
			distMin = tryBestMatch(m, x + 1, y + 1, element, distMin);
			/* left */
			distMin = tryBestMatch(m, x - 1, y, element, distMin);
			/* center is already filled by bestMatch */
			/* right */
			distMin = tryBestMatch(m, x + 1, y, element, distMin);
			/* bottom */
			distMin = tryBestMatch(m, x - 1, y - 1, element, distMin);
			distMin = tryBestMatch(m, x, y - 1, element, distMin);
			distMin = tryBestMatch(m, x + 1, y - 1, element, distMin);
		}
		if (bestMatch != null) {
			conflictsOnPixel.remove(bestMatch);
			elementsStaticMandelbrot[bestMatchAtX][bestMatchAtY] = bestMatch;
		}
	}

	double tryBestMatch(Mem m, int x, int y, MandelbrotElement element, double distMin) {
		if (emptyAt(x, y)) {
			AreaMandelbrot.screenToDomainCarry(m, x, y);
			dist = quad(m.re, m.im, element.originRe, element.originIm);
			if (dist < distMin) {
				distMin = dist;
				bestMatch = element;
				bestMatchAtX = x;
				bestMatchAtY = y;
			}
		}
		return distMin;
	}

	private boolean emptyAt(int x, int y) {
		return valueAt(x, y) != null;
	}

	public Integer valueAt(int x, int y) {
		if (x < 0 || y < 0 || x >= RESOLUTION_WIDTH || y >= RESOLUTION_HEIGHT) {
			return null;
		}
		MandelbrotElement el = elementsStaticMandelbrot[x][y];
		if (el != null) {
			return el.getValue();
		}
		return null;
	}


	private MandelbrotElement bestMatch(Mem m, int x, int y, ArrayList<MandelbrotElement> conflictsOnPixel) {
		double distMin = 42;
		MandelbrotElement ret = null;
		for (MandelbrotElement el : conflictsOnPixel) {
			AreaMandelbrot.screenToDomainCarry(m, x, y);
			dist = quad(m.re, m.im, el.originRe, el.originIm);
			if (dist < distMin) {
				distMin = dist;
				ret = el;
			}
		}
		conflictsOnPixel.remove(ret);
		return ret;
	}

	/**
	 * This is Quadrance
	 */
	double quad(double aRe, double aIm, double bRe, double bIm) {
		return (aRe - bRe) * (aRe - bRe) + (aIm - bIm) * (aIm - bIm);
	}

	public void createMask() {
		log.debug("createMask()");
		if (maskDone) {
			maskDone = false;
			MandelbrotElement element;
			Color color;

			for (int x = 0; x < RESOLUTION_WIDTH; x++) {
				for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
					element = elementsStaticMandelbrot[x][y];
					if (element != null) {
						color = switch (element.getState()) {
							case ActiveMoved -> ACTIVE_MOVED;
							case ActiveNew -> ACTIVE_NEW;
							case ActiveRecalculate -> ACTIVE_RECALCULATE;
							case HibernatedBlack -> HIBERNATED_BLACK;
							case HibernatedBlackNeighbour -> HIBERNATED_BLACK_NEIGHBOR;
							case HibernatedFinishedTooShort -> HIBERNATED_FINISHED_TOO_SHORT;
							case HibernatedFinishedLong -> HIBERNATED_FINISHED_LONG;
							case ActiveFixed -> ACTIVE_FIXED;
						};
					} else {
						color = NULL;
					}
					MandelbrotMaskImage.setRGB(x, y, color.getRGB());
				}
			}
			maskDone = true;
		} else {
			log.error("createMask() refresh way too fast");
		}
	}

	/*
	 * This is called already after zoom
	 */
	public void domainForThisZoom() {
		/*
		 * Scan area elements (old positions from previous calculation)
		 * They will be moved to new positions (remembered)
		 * And calculation will be skipped for them.
		 */
		MandelbrotElement element;
		for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
			for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
				element = elementsStaticMandelbrot[xx][yy];
				if (AreaMandelbrot.contains(element.originRe, element.originIm)) {
					/* Move elements to new coordinates */
					if (element.isHibernatedBlack_Neighbour()) {
						element.setHibernatedBlack();
					} else if (!element.isHibernatedBlack()
							&& !element.isHibernatedBlack_Neighbour()
							&& !element.isFixed()
							&& FractalMachine.isVeryDeepBlack(xx, yy, elementsStaticMandelbrot)) {
						element.setHibernatedBlack();
					} else {
						if (element.isActiveNew()) {
							element.setActiveMoved();
						}
					}
					elementsToRemember.add(element);
				}
			}
		}

		/*
		 * Delete all elements assigned to Mandelbrot coordinates.
		 * Some are remembered and will be moved.
		 */
		for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
			for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
				elementsStaticMandelbrot[xx][yy] = null;
			}
		}

		/*
		 * Add remembered elements to their new position for new calculation
		 */
		int newPositionT;
		int newPositionX;
		MandelbrotElement done;
		Mem m = new Mem();

		@SuppressWarnings(value = "unchecked")
		final ArrayList<MandelbrotElement>[][] conflicts = new ArrayList[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
		for (MandelbrotElement el : elementsToRemember) {

			AreaMandelbrot.domainToScreenCarry(m, el.originRe, el.originIm);
			newPositionT = m.px;
			newPositionX = m.py;

			if (newPositionX != Mem.NOT && newPositionT != Mem.NOT) {
				done = elementsStaticMandelbrot[newPositionT][newPositionX];
				if (done != null) {
					/* Conflict */
					if (conflicts[newPositionT][newPositionX] == null) {
						conflicts[newPositionT][newPositionX] = new ArrayList<>();
					}
					conflicts[newPositionT][newPositionX].add(el);
				} else {
					/* OK; no conflict */
					elementsStaticMandelbrot[newPositionT][newPositionX] = el;
				}
			}
		}
		ArrayList<MandelbrotElement> conflictsOnPixel;
		/*
		 * Resolve conflicts
		 * Because of zooming, multiple Elements may have moved to the same pixel
		 */
		for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
			for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
				conflictsOnPixel = conflicts[xx][yy];
				if (conflictsOnPixel != null) {

					/* Add the initial conflict */
					element = elementsStaticMandelbrot[xx][yy];
					conflictsOnPixel.add(element);

					/* Find best match for the pixel with conflicts */
					elementsStaticMandelbrot[xx][yy] = bestMatch(m, xx, yy, conflictsOnPixel);

					/* Find best match for pixels around */
					if (!conflictsOnPixel.isEmpty()) {
						dropBestMatchToEmptyNeighbour(m, xx, yy, conflictsOnPixel);
					}
					if (!conflictsOnPixel.isEmpty()) {
						dropBestMatchToEmptyNeighbour(m, xx, yy, conflictsOnPixel);
					}
					if (!conflictsOnPixel.isEmpty()) {
						dropBestMatchToEmptyNeighbour(m, xx, yy, conflictsOnPixel);
					}
					conflicts[xx][yy] = null;
				}
			}
		}

		/*
		 * Create new elements on positions where nothing was moved to
		 */
		MandelbrotElement newElement;
		for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
			for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
				if (elementsStaticMandelbrot[xx][yy] == null) {
					AreaMandelbrot.screenToDomainCarry(m, xx, yy);
					newElement = new MandelbrotElement(m.re, m.im);
					elementsStaticMandelbrot[xx][yy] = newElement;
				}
			}
		}

		/*
		 * Calculation for some positions should be skipped as they are to far away form any divergent position. (They are deep black)
		 * Skipp also calculation for their neighbours. (Black neighbour)
		 */
		for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
			for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
				element = elementsStaticMandelbrot[xx][yy];
				if (element.isHibernatedBlack()) {
					final int r = 3;
					for (int x = -r; x < r; x++) {
						for (int y = -r; y < r; y++) {
							/* Set black neighbours in circle around deep black position */
							if ((x * x) + (y * y) < (r * r)) {
								FractalMachine.setAsDeepBlack(xx + x, yy - y, elementsStaticMandelbrot);
							}
						}
					}
				}
			}
		}
		elementsToRemember.clear();
	}
}
