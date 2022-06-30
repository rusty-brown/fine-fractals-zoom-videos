package fine.fractals.context.mandelbrot;

import fine.fractals.Main;
import fine.fractals.data.Element;
import fine.fractals.data.Mem;
import fine.fractals.engine.FractalMachine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.images.FractalImage.MandelbrotMaskImage;
import static fine.fractals.mandelbrot.MandelbrotMaskColors.*;

class DomainMandelbrotImpl {

	private static final Logger log = LogManager.getLogger(DomainMandelbrotImpl.class);

	public final Element[][] elementsScreen = new Element[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
	private ArrayList<Element> elementsToRemember = new ArrayList<>();

	private int conflictsResolved;
	private Element bestMatch;
	private int bestMatchAtT;
	private int bestMatchAtX;
	private double dist;

	private int partStartT = 0;
	private int partRowT = 0;


	public boolean domainNotFinished = true;

	static boolean maskDone = true;

	public static DomainMandelbrotImpl DomainMandelbrot;

	private DomainMandelbrotImpl() {
	}

	static {
		log.info("init");
		DomainMandelbrot = new DomainMandelbrotImpl();
		log.info("initiate");
		System.out.println(AreaMandelbrot);
		DomainMandelbrot.domainScreenCreateInitialization();
		System.out.println(AreaMandelbrot);
	}

	public final void domainScreenCreateInitialization() {
		for (int t = 0; t < RESOLUTION_WIDTH; t++) {
			for (int x = 0; x < RESOLUTION_HEIGHT; x++) {
				Element element = new Element(AreaMandelbrot.screenToDomainReT(t), AreaMandelbrot.screenToDomainImX(x));
				elementsScreen[t][x] = element;
			}
		}
	}

	// TODO
	synchronized public ArrayList<Element> fetchDomainPart() {

		log.info("DOMAIN created new");

		Element elementZero;
		ArrayList<Element> domainPart = new ArrayList<>();

		/**
		 * Only interesting will be put to cd.domainGPUFiltered ... Some element diverged in their neighbourhood
		 * Central element was already calculated without wrapping
		 */
		Element[] wrapping = null;

		final boolean wrapDomain = Main.RESOLUTION_MULTIPLIER > 2;
		log.info("wrapDomainS " + wrapDomain);

		if (wrapDomain) {
			wrapping = new Element[Main.RESOLUTION_MULTIPLIER * Main.RESOLUTION_MULTIPLIER];
		}

		final int MAX = 4_000_000;
		long elementCountPart = 0;

		long wrappedCount = 0;
		long notWrappedCount = 0;

		log.info("partStartT: " + partStartT);
		partRowT = 0;
		int t;

		for (t = partStartT; t < RESOLUTION_WIDTH; t++) {
			for (int x = 0; x < RESOLUTION_HEIGHT; x++) {

				elementZero = elementsScreen[t][x];

				boolean isActive = elementZero != null && elementZero.isActiveAny();

				if (!wrapDomain) {
					/* First calculation */
					if (isActive) {
						elementCountPart++;
						domainPart.add(elementZero);
					}
				} else {
					/* Wrapping of first calculation */
					boolean shouldBeWrapped = elementZero != null;// && FractalMachine.someNeighboursFinishedInside(t, x, elementsScreen);
					if (shouldBeWrapped) {
						wrappedCount++;

						AreaMandelbrot.wrap(elementZero, wrapping);

						for (Element el : wrapping) {
							elementCountPart++;
							domainPart.add(el);
						}
					} else {
						notWrappedCount++;
					}
				}
			}
			partRowT++;
			if (elementCountPart >= MAX) {
				log.info("TO MANY");
				/* Domain has to many elements. Do calculation for this part*/
				partStartT = t;

				/* Continue with this part of domain */

				log.info("DOMAIN PART LIMIT: " + elementCountPart);
				log.info("partStartT: " + partStartT);

				/* stop creating domain and calculate fractal with this part */
				break;
			}
		}
		if (wrapDomain) {
			/* domain optimization fixes */
			int reWrapped = 0;
			for (int tt = 0; tt < RESOLUTION_WIDTH; tt++) {
				for (int xx = 0; xx < RESOLUTION_HEIGHT; xx++) {
					/* Go through domain and queue elements to be filtered by GPU */
					elementZero = elementsScreen[tt][xx];
					if (elementZero != null && elementZero.isActiveRecalculate()) {
						// don't set any state, will be set again properly by calculation

						AreaMandelbrot.wrap(elementZero, wrapping);
						for (Element el : wrapping) {
							reWrapped++;
							domainPart.add(el);
						}
					}
				}
			}
			log.info(">reWrapped " + reWrapped);
		}

		log.info("notWrappedCount " + notWrappedCount);
		log.info("wrappedCount " + wrappedCount);

		if (Main.RESOLUTION_MULTIPLIER == 1) {
			/* finished and there was NO wrapping */
			log.info("Domain EMPTY: No multiplier");
			domainNotFinished = false;
		} else if (wrapDomain && t == RESOLUTION_WIDTH) {
			/* finished and there was wrapping */
			log.info("Domain EMPTY: Multiplication finished");
			domainNotFinished = false;
		} else {
			log.info("Domain still not empty");
		}

		// TODO ? wrapDomain = true;
		log.info("WRAP DOMAIN " + wrapDomain);
		return domainPart;
		//}
	}

	private void dropBestMatchToEmptyNeighbour(Mem mem, int t, int x, LinkedList<Element> conflictsOnPixel) {
		bestMatch = null;

		dist = 0;
		double distMin = 42;
		for (Element element : conflictsOnPixel) {
			/* up */
			distMin = tryBestMatch(mem, t - 1, x + 1, element, distMin);
			distMin = tryBestMatch(mem, t, x + 1, element, distMin);
			distMin = tryBestMatch(mem, t + 1, x + 1, element, distMin);
			/* left */
			distMin = tryBestMatch(mem, t - 1, x, element, distMin);
			/* center is already filled by bestMatch */
			/* right */
			distMin = tryBestMatch(mem, t + 1, x, element, distMin);
			/* bottom */
			distMin = tryBestMatch(mem, t - 1, x - 1, element, distMin);
			distMin = tryBestMatch(mem, t, x - 1, element, distMin);
			distMin = tryBestMatch(mem, t + 1, x - 1, element, distMin);
		}
		if (bestMatch != null) {
			conflictsOnPixel.remove(bestMatch);
			elementsScreen[bestMatchAtT][bestMatchAtX] = bestMatch;
			conflictsResolved++;
		}
	}

	double tryBestMatch(Mem mem, int t, int x, Element element, double distMin) {
		if (emptyAt(t, x)) {
			AreaMandelbrot.screenToDomainCarry(mem, t, x);
			dist = dist(mem.re, mem.im, element.originReT, element.originImX);
			if (dist < distMin) {
				distMin = dist;
				bestMatch = element;
				bestMatchAtT = t;
				bestMatchAtX = x;
			}
		}
		return distMin;
	}

	private boolean emptyAt(int t, int x) {
		return valueAt(t, x) != null;
	}

	public Integer valueAt(int t, int x) {
		if (t < 0 || x < 0 || t >= RESOLUTION_WIDTH || x >= RESOLUTION_HEIGHT) {
			return null;
		}
		Element el = elementsScreen[t][x];
		if (el != null) {
			return el.getValue();
		}
		return null;
	}


	private Element bestMatch(Mem mem, int t, int x, LinkedList<Element> conflictsOnPixel) {
		double distMin = 42;
		Element ret = null;
		for (Element el : conflictsOnPixel) {
			AreaMandelbrot.screenToDomainCarry(mem, t, x);
			dist = dist(mem.re, mem.im, el.originReT, el.originImX);
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
	double dist(double hhReT, double hhImX, double hReT, double hImX) {
		return (hhReT - hReT) * (hhReT - hReT) + (hhImX - hImX) * (hhImX - hImX);
	}

	public void createMask() {
		log.info("createMask");
		if (maskDone) {
			maskDone = false;
			Element element;
			Color color;

			for (int t = 0; t < RESOLUTION_WIDTH; t++) {
				for (int x = 0; x < RESOLUTION_HEIGHT; x++) {
					element = elementsScreen[t][x];
					if (element != null && element.getColor() != null) {
						color = element.getColor();
					} else {
						if (element != null) {
							color = switch (element.getState()) {
								case ActiveMoved -> MOVED;
								case ActiveNew -> ACTIVE_NEW;
								case HibernatedBlack -> HIBERNATED_BLACK;
								case HibernatedBlackNeighbour -> HIBERNATED_BLACK_NEIGHBOR;
								case HibernatedFinished -> FINISHED_OUT;
								case HibernatedFinishedInside -> FINISHED_IN;
								case ActiveFixed -> ACTIVE_FIXED;
								default -> ERROR;
							};
						} else {
							color = NULL;
						}
					}
					MandelbrotMaskImage.setRGB(t, x, color.getRGB());
				}
			}
			maskDone = true;
		} else {
			log.info("mask don't");
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
		int value;
		Element element;
		for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
			for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
				element = elementsScreen[xx][yy];
				if (AreaMandelbrot.contains(element.originReT, element.originImX)) {
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
					value = element.getValue();
				}
				/* clear color set for debug */
				element.setColor(null);
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
		int conflictsFound = 0;
		Integer newPositionT;
		Integer newPositionX;
		Element done;

		int addedToNewPositions = 0;

		Mem mem = new Mem();

		LinkedList<Element>[][] conflicts = new LinkedList[RESOLUTION_WIDTH][RESOLUTION_HEIGHT];
		for (Element el : elementsToRemember) {

			AreaMandelbrot.domainToScreenCarry(mem, el.originReT, el.originImX);

			newPositionT = mem.pxRe;
			newPositionX = mem.pxIm;

			if (newPositionX != Mem.NOT && newPositionT != Mem.NOT) {
				done = elementsScreen[newPositionT][newPositionX];
				if (done != null) {
					/* Conflict */
					if (conflicts[newPositionT][newPositionX] == null) {
						conflicts[newPositionT][newPositionX] = new LinkedList<>();
					}
					conflictsFound++;
					// el.setColor(Color.BLACK);
					conflicts[newPositionT][newPositionX].add(el);
				} else {
					/* OK; no conflict */
					elementsScreen[newPositionT][newPositionX] = el;
					addedToNewPositions++;
				}
			}
		}
		conflictsResolved = 0;
		LinkedList conflictsOnPixel;
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
		Element newElement;
		for (int yy = 0; yy < RESOLUTION_HEIGHT; yy++) {
			for (int xx = 0; xx < RESOLUTION_WIDTH; xx++) {
				if (elementsScreen[xx][yy] == null) {
					AreaMandelbrot.screenToDomainCarry(mem, xx, yy);
					newElement = new Element(mem.re, mem.im);
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
