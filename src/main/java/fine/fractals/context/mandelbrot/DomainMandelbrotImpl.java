package fine.fractals.context.mandelbrot;

import fine.fractals.data.MandelbrotElement;
import fine.fractals.data.Mem;
import fine.fractals.machine.FractalMachine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

import static fine.fractals.Main.*;
import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.images.FractalImage.MandelbrotMaskImage;
import static fine.fractals.mandelbrot.MandelbrotMaskColors.*;

class DomainMandelbrotImpl {

	private static final Logger log = LogManager.getLogger(DomainMandelbrotImpl.class);

	final MandelbrotElement[][] elementsScreen = new MandelbrotElement[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
	private final ArrayList<MandelbrotElement> elementsToRemember = new ArrayList<>();

	private MandelbrotElement bestMatch;
	private int bestMatchAtX;
	private int bestMatchAtY;
	private double dist;

	static boolean maskDone = true;

	private static boolean firstDomainExecution = true;

	static final DomainMandelbrotImpl DomainMandelbrot;

	private DomainMandelbrotImpl() {
	}

	static {
		log.info("init");
		DomainMandelbrot = new DomainMandelbrotImpl();
		log.info("initiate");
		DomainMandelbrot.domainScreenCreateInitialization();
	}

	public final void domainScreenCreateInitialization() {
		log.info("domainScreenCreateInitialization()");
		for (int x = 0; x < RESOLUTION_WIDTH; x++) {
			for (int y = 0; y < RESOLUTION_HEIGHT; y++) {
				MandelbrotElement element = new MandelbrotElement(AreaMandelbrot.screenToDomainRe(x), AreaMandelbrot.screenToDomainIm(y));
				elementsScreen[x][y] = element;
			}
		}
	}

	private boolean odd = true;

	public ArrayList<MandelbrotElement> fetchDomainFull() {

		log.info("DOMAIN created new");

		MandelbrotElement elementZero;
		final ArrayList<MandelbrotElement> domainFull = new ArrayList<>();

		for (int x = 0; x < RESOLUTION_WIDTH; x++) {
			for (int y = 0; y < RESOLUTION_HEIGHT; y++) {

				elementZero = elementsScreen[x][y];

				boolean isActive = elementZero != null && elementZero.isActiveAny();

				/* First calculation */
				if (isActive) {
					domainFull.add(elementZero);

					if (!firstDomainExecution) {
						// TODO try also with: OR FractalMachine.someNeighboursFinishedInside(t, x, elementsScreen);
						if (RESOLUTION_MULTIPLIER == 2) {
							final double d = AreaMandelbrot.plank() / 3;

							if (odd) {
								domainFull.add(new MandelbrotElement(
										elementZero.originRe + d,
										elementZero.originIm + d
								));
								domainFull.add(new MandelbrotElement(
										elementZero.originRe - d,
										elementZero.originIm - d
								));
							} else {
								domainFull.add(new MandelbrotElement(
										elementZero.originRe - d,
										elementZero.originIm + d
								));
								domainFull.add(new MandelbrotElement(
										elementZero.originRe + d,
										elementZero.originIm - d
								));
							}
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

	private void dropBestMatchToEmptyNeighbour(Mem mem, int x, int y, ArrayList<MandelbrotElement> conflictsOnPixel) {
		bestMatch = null;

		dist = 0;
		double distMin = 42;
		for (MandelbrotElement element : conflictsOnPixel) {
			/* up */
			distMin = tryBestMatch(mem, x - 1, y + 1, element, distMin);
			distMin = tryBestMatch(mem, x, y + 1, element, distMin);
			distMin = tryBestMatch(mem, x + 1, y + 1, element, distMin);
			/* left */
			distMin = tryBestMatch(mem, x - 1, y, element, distMin);
			/* center is already filled by bestMatch */
			/* right */
			distMin = tryBestMatch(mem, x + 1, y, element, distMin);
			/* bottom */
			distMin = tryBestMatch(mem, x - 1, y - 1, element, distMin);
			distMin = tryBestMatch(mem, x, y - 1, element, distMin);
			distMin = tryBestMatch(mem, x + 1, y - 1, element, distMin);
		}
		if (bestMatch != null) {
			conflictsOnPixel.remove(bestMatch);
			elementsScreen[bestMatchAtX][bestMatchAtY] = bestMatch;
		}
	}

	double tryBestMatch(Mem mem, int x, int y, MandelbrotElement element, double distMin) {
		if (emptyAt(x, y)) {
			AreaMandelbrot.screenToDomainCarry(mem, x, y);
			dist = dist(mem.re, mem.im, element.originRe, element.originIm);
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
		MandelbrotElement el = elementsScreen[x][y];
		if (el != null) {
			return el.getValue();
		}
		return null;
	}


	private MandelbrotElement bestMatch(Mem mem, int x, int y, ArrayList<MandelbrotElement> conflictsOnPixel) {
		double distMin = 42;
		MandelbrotElement ret = null;
		for (MandelbrotElement el : conflictsOnPixel) {
			AreaMandelbrot.screenToDomainCarry(mem, x, y);
			dist = dist(mem.re, mem.im, el.originRe, el.originIm);
			if (dist < distMin) {
				distMin = dist;
				ret = el;
			}
		}
		conflictsOnPixel.remove(ret);
		return ret;
	}

	/**
	 * This is not distance. It is quadrance
	 */
	double dist(double aRe, double aIm, double bRe, double bIm) {
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
					element = elementsScreen[x][y];
					if (element != null) {
						color = switch (element.getState()) {
							case ActiveMoved -> ACTIVE_MOVED;
							case ActiveNew -> ACTIVE_NEW;
							case ActiveRecalculate -> ACTIVE_RECALCULATE;
							case HibernatedBlack -> HIBERNATED_BLACK;
							case HibernatedBlackNeighbour -> HIBERNATED_BLACK_NEIGHBOR;
							case HibernatedFinished -> HIBERNATED_FINISHED_OUT;
							case HibernatedFinishedInside -> HIBERNATED_FINISHED_IN;
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

	/*-----------------------------------------------------------------------------*/


	/*
	 * This is called already after zoom
	 */
	public void domainForThisZoom() {

		/*
		 * Scan area elements (old positions from previous calculation) to be -
		 * moved to new positions (remembered) or skipped calculation for them.
		 */
		MandelbrotElement element;
		for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
			for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
				element = elementsScreen[xx][yy];
				if (AreaMandelbrot.contains(element.originRe, element.originIm)) {
					/* Move elements to new coordinates */
					if (element.isHibernatedBlack_Neighbour()) {
						element.setHibernatedBlack();
					} else if (!element.isHibernatedBlack()
							&& !element.isHibernatedBlack_Neighbour()
							&& !element.isFixed()
							&& FractalMachine.isVeryDeepBlack(xx, yy, elementsScreen)) {
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
		 * Delete all elements assigned to screen coordinates.
		 * Some are remembered and will be moved.
		 */
		for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
			for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
				elementsScreen[xx][yy] = null;
			}
		}

		/*
		 * Add remembered elements to their new position for new calculation
		 */
		int newPositionT;
		int newPositionX;
		MandelbrotElement done;

		Mem mem = new Mem();

		@SuppressWarnings(value = "unchecked")
		final ArrayList<MandelbrotElement>[][] conflicts = new ArrayList[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
		for (MandelbrotElement el : elementsToRemember) {

			AreaMandelbrot.domainToScreenCarry(mem, el.originRe, el.originIm);

			newPositionT = mem.pxRe;
			newPositionX = mem.pxIm;

			if (newPositionX != Mem.NOT && newPositionT != Mem.NOT) {
				done = elementsScreen[newPositionT][newPositionX];
				if (done != null) {
					/* Conflict */
					if (conflicts[newPositionT][newPositionX] == null) {
						conflicts[newPositionT][newPositionX] = new ArrayList<>();
					}
						conflicts[newPositionT][newPositionX].add(el);
				} else {
					/* OK; no conflict */
					elementsScreen[newPositionT][newPositionX] = el;
				}
			}
		}
		ArrayList<MandelbrotElement> conflictsOnPixel;
		/* Resolve found conflicts */
		/* More Elements hit same pixel after zoom */
		for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
			for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
				conflictsOnPixel = conflicts[xx][yy];
				if (conflictsOnPixel != null) {

					/* Add the initial conflict */
					element = elementsScreen[xx][yy];
					conflictsOnPixel.add(element);

					/* Find best match for the pixel with conflicts */
					elementsScreen[xx][yy] = bestMatch(mem, xx, yy, conflictsOnPixel);

					/* Find best match for pixels around */
					if (!conflictsOnPixel.isEmpty()) {
						dropBestMatchToEmptyNeighbour(mem, xx, yy, conflictsOnPixel);
					}
					if (!conflictsOnPixel.isEmpty()) {
						dropBestMatchToEmptyNeighbour(mem, xx, yy, conflictsOnPixel);
					}
					if (!conflictsOnPixel.isEmpty()) {
						dropBestMatchToEmptyNeighbour(mem, xx, yy, conflictsOnPixel);
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
				if (elementsScreen[xx][yy] == null) {
					AreaMandelbrot.screenToDomainCarry(mem, xx, yy);
					newElement = new MandelbrotElement(mem.re, mem.im);
					elementsScreen[xx][yy] = newElement;
				}
			}
		}

		/*
		 * Calculation for some positions should be skipped as they are to far away form any divergent position. (They are deep black)
		 * Skipp also calculation for their neighbours. (Black neighbour)
		 * Try to guess value of new elements if all values around them are the very similar.
		 */
		for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
			for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
				element = elementsScreen[xx][yy];
				if (element.isHibernatedBlack()) {
					final int r = 3;
					for (int x = -r; x < r; x++) {
						for (int y = -r; y < r; y++) {
							/* Set black neighbours in circle around deep black position */
							if ((x * x) + (y * y) < (r * r)) {
								FractalMachine.setAsDeepBlack(xx + x, yy - y, elementsScreen);
							}
						}
					}
				}
			}
		}
		elementsToRemember.clear();
	}
}
