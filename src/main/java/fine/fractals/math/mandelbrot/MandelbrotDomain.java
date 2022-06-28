package fine.fractals.math.mandelbrot;

import fine.fractals.Application;
import fine.fractals.Main;
import fine.fractals.color.things.ScreenColor;
import fine.fractals.data.objects.Bool;
import fine.fractals.engine.FractalMachine;
import fine.fractals.math.AreaDomain;
import fine.fractals.math.AreaImage;
import fine.fractals.math.common.Element;
import fine.fractals.math.common.HH;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

public class MandelbrotDomain {

	private static final Logger log = LogManager.getLogger(MandelbrotDomain.class);

	Element[][] elementsScreen = new Element[Application.RESOLUTION_DOMAIN_WIDTH][Application.RESOLUTION_DOMAIN_HEIGHT];
	ArrayList<Element> elementsToRemember = new ArrayList<>();

	private AreaDomain areaDomain;
	private AreaImage areaImage;

	int conflictsResolved;
	Element bestMatch;
	int bestMatchAtT;
	int bestMatchAtX;
	double dist;

	int partStartT = 0;
	int partRowT = 0;
	// boolean wrapDomain = false;


	boolean domainNotFinished = true;

	static boolean maskDone = true;

	public MandelbrotDomain(AreaDomain areaDomain, AreaImage areaImage) {
		this.areaDomain = areaDomain;
		this.areaImage = areaImage;
	}

	public final void domainScreenCreateInitialization() {
		for (int t = 0; t < Application.RESOLUTION_DOMAIN_WIDTH; t++) {
			for (int x = 0; x < Application.RESOLUTION_DOMAIN_HEIGHT; x++) {
				Element element = new Element(areaDomain.screenToDomainReT(t), areaDomain.screenToDomainImX(x));
				elementsScreen[t][x] = element;
			}
		}
	}

	synchronized ArrayList<Element> fetchDomainPart() {

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

		for (t = partStartT; t < Application.RESOLUTION_DOMAIN_WIDTH; t++) {
			for (int x = 0; x < Application.RESOLUTION_DOMAIN_HEIGHT; x++) {

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

						areaDomain.wrap(elementZero, wrapping);

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
			for (int tt = 0; tt < Application.RESOLUTION_DOMAIN_WIDTH; tt++) {
				for (int xx = 0; xx < Application.RESOLUTION_DOMAIN_HEIGHT; xx++) {
					/* Go through domain and queue elements to be filtered by GPU */
					elementZero = elementsScreen[tt][xx];
					if (elementZero != null && elementZero.isActiveRecalculate()) {
						// don't set any state, will be set again properly by calculation

						areaDomain.wrap(elementZero, wrapping);
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

		// log.info(". GPU go");
		// GPU.filter(domainToFilter, cd);
		// log.info(". GPU go done");

		if (Main.RESOLUTION_MULTIPLIER == 1) {
			/* finished and there was NO wrapping */
			log.info("Domain EMPTY: No multiplier");
			domainNotFinished = false;
		} else if (wrapDomain && t == Application.RESOLUTION_DOMAIN_WIDTH) {
			/* finished and there was wrapping */
			log.info("Domain EMPTY: Multiplication finished");
			domainNotFinished = false;
		} else {
			// log.info("-");
			// log.info("Venture.RESOLUTION_MULTIPLIER " + Venture.RESOLUTION_MULTIPLIER);
			// log.info("wrapDomain " + wrapDomain);
			// log.info("t " + t);
			// log.info("Application.RESOLUTION_DOMAIN_T " + Application.RESOLUTION_DOMAIN_T);
			// log.info("-");
			log.info("Domain still not empty");
		}

		// TODO ? wrapDomain = true;
		log.info("WRAP DOMAIN " + wrapDomain);
		return domainPart;
		//}
	}

	private void dropBestMatchToEmptyNeighbour(HH hh, int t, int x, LinkedList<Element> conflictsOnPixel) {
		bestMatch = null;

		dist = 0;
		double distMin = 42;
		for (Element element : conflictsOnPixel) {
			/* up */
			distMin = tryBestMatch(hh, t - 1, x + 1, element, distMin);
			distMin = tryBestMatch(hh, t, x + 1, element, distMin);
			distMin = tryBestMatch(hh, t + 1, x + 1, element, distMin);
			/* left */
			distMin = tryBestMatch(hh, t - 1, x, element, distMin);
			/* center is already filled by bestMatch */
			/* right */
			distMin = tryBestMatch(hh, t + 1, x, element, distMin);
			/* bottom */
			distMin = tryBestMatch(hh, t - 1, x - 1, element, distMin);
			distMin = tryBestMatch(hh, t, x - 1, element, distMin);
			distMin = tryBestMatch(hh, t + 1, x - 1, element, distMin);
		}
		if (bestMatch != null) {
			conflictsOnPixel.remove(bestMatch);
			elementsScreen[bestMatchAtT][bestMatchAtX] = bestMatch;
			conflictsResolved++;
			// } else {
			// elements[xx][yy].setColor(Color.RED);
		}
	}

	double tryBestMatch(HH hh, int t, int x, Element element, double distMin) {
		if (emptyAt(t, x)) {
			areaDomain.screenToDomainCarry(hh, t, x);
			dist = dist(hh.calculation.re, hh.calculation.im, element.originReT, element.originImX);
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
		if (t < 0 || x < 0 || t >= Application.RESOLUTION_DOMAIN_WIDTH || x >= Application.RESOLUTION_DOMAIN_HEIGHT) {
			return null;
		}
		Element el = elementsScreen[t][x];
		if (el != null) {
			return el.getValue();
		}
		return null;
	}


	private Element bestMatch(HH hh, int t, int x, LinkedList<Element> conflictsOnPixel) {
		double distMin = 42;
		Element ret = null;
		for (Element el : conflictsOnPixel) {
			areaDomain.screenToDomainCarry(hh, t, x);
			dist = dist(hh.calculation.re, hh.calculation.im, el.originReT, el.originImX);
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

	public void createMask(BufferedImage maskImage) {
		if (maskDone) {
			maskDone = false;
			Element element;
			Color color;

			for (int t = 0; t < Application.RESOLUTION_DOMAIN_WIDTH; t++) {
				for (int x = 0; x < Application.RESOLUTION_DOMAIN_HEIGHT; x++) {
					element = elementsScreen[t][x];
					if (element != null && element.getColor() != null) {
						color = element.getColor();
					} else {
						if (element != null) {
							switch (element.getState()) {
								case ActiveMoved:
									color = ScreenColor._MOVED;
									break;
								case ActiveNew:
									color = ScreenColor._ACTIVE_NEW;
									break;
								case HibernatedBlack:
									color = ScreenColor._HIBERNATED_BLACK;
									break;
								case HibernatedBlackNeighbour:
									color = ScreenColor._HIBERNATED_BLACK_NEIGHBOR;
									break;
								case HibernatedFinished:
									color = ScreenColor._FINISHED_OUTSIDE;
									break;
								case HibernatedFinishedInside:
									color = ScreenColor._FINISHED_INSIDE;
									break;
								case ActiveFixed:
									color = ScreenColor._ACTIVE_FIXED;
									break;
								default:
									color = ScreenColor._ERROR;
									break;
							}
						} else {
							color = ScreenColor._NULL;
						}
					}
					maskImage.setRGB(t, x, color.getRGB());
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
		int valuesRemembered = 0;
		int newHibernatedBlack = 0;
		long allValues = 0;
		Element element;
		for (int yy = 0; yy < Application.RESOLUTION_DOMAIN_HEIGHT; yy++) {
			for (int xx = 0; xx < Application.RESOLUTION_DOMAIN_WIDTH; xx++) {
				element = elementsScreen[xx][yy];
				if (areaDomain.contains(element.originReT, element.originImX)) {
					/* Move elements to new coordinates */
					if (element.isHibernatedBlack_Neighbour()) {
						element.setHibernatedBlack();
					} else if (!element.isHibernatedBlack()
							&& !element.isHibernatedBlack_Neighbour()
							&& !element.isFixed()
							&& FractalMachine.isVeryDeepBlack(xx, yy, elementsScreen)) {
						element.setHibernatedBlack();
						newHibernatedBlack++;
					} else {
						if (element.isActiveNew()) {
							element.setActiveMoved();
						}
					}
					elementsToRemember.add(element);
					value = element.getValue();
					if (value != 0) {
						allValues += value;
						valuesRemembered++;
					}
				}
				/* clear color set for debug */
				element.setColor(null);
			}
		}


		/*
		 * Delete all elements assigned to screen coordinates.
		 * Some are remembered and will be moved.
		 */
		for (int yy = 0; yy < Application.RESOLUTION_DOMAIN_HEIGHT; yy++) {
			for (int xx = 0; xx < Application.RESOLUTION_DOMAIN_WIDTH; xx++) {
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

		HH hh = new HH();

		LinkedList<Element>[][] conflicts = new LinkedList[Application.RESOLUTION_DOMAIN_WIDTH][Application.RESOLUTION_DOMAIN_HEIGHT];
		for (Element el : elementsToRemember) {


			areaDomain.domainToScreenCarry(hh, el.originReT, el.originImX);

			newPositionT = hh.calculation.pxRe;
			newPositionX = hh.calculation.pxIm;

			if (newPositionX != HH.NOT && newPositionT != HH.NOT) {
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
		for (int yy = 0; yy < Application.RESOLUTION_DOMAIN_HEIGHT; yy++) {
			for (int xx = 0; xx < Application.RESOLUTION_DOMAIN_WIDTH; xx++) {
				conflictsOnPixel = conflicts[xx][yy];
				if (conflictsOnPixel != null) {

					/* Add the initial conflict */
					element = elementsScreen[xx][yy];
					conflictsOnPixel.add(element);

					/* Find best match for the pixel with conflicts */
					elementsScreen[xx][yy] = bestMatch(hh, xx, yy, conflictsOnPixel);

					/* Find best match for pixels around */
					if (!conflictsOnPixel.isEmpty()) {
						dropBestMatchToEmptyNeighbour(hh, xx, yy, conflictsOnPixel);
					}
					if (!conflictsOnPixel.isEmpty()) {
						dropBestMatchToEmptyNeighbour(hh, xx, yy, conflictsOnPixel);
					}
					if (!conflictsOnPixel.isEmpty()) {
						dropBestMatchToEmptyNeighbour(hh, xx, yy, conflictsOnPixel);
					}
					conflicts[xx][yy] = null;
				}
			}
		}

		/*
		 * Create new elements on positions where nothing was moved to
		 */
		int createdNewElements = 0;
		Element newElement;
		for (int yy = 0; yy < Application.RESOLUTION_DOMAIN_HEIGHT; yy++) {
			for (int xx = 0; xx < Application.RESOLUTION_DOMAIN_WIDTH; xx++) {
				if (elementsScreen[xx][yy] == null) {
					areaDomain.screenToDomainCarry(hh, xx, yy);
					newElement = new Element(hh.calculation.re, hh.calculation.im);
					elementsScreen[xx][yy] = newElement;
					createdNewElements++;
				}
			}
		}

		/*
		 * Calculation for some positions should be skipped as they are to far away form any divergent position. (They are deep black)
		 * Skipp also calculation for their neighbours. (Black neighbour)
		 * Try to guess value of new elements if all values around them are the very similar.
		 */
		for (int yy = 0; yy < Application.RESOLUTION_DOMAIN_HEIGHT; yy++) {
			for (int xx = 0; xx < Application.RESOLUTION_DOMAIN_WIDTH; xx++) {
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

		// Time.now(".Guessed count:   " + guessedCount);
		elementsToRemember.clear();
	}

	public void resetOptimizationSoft() {
		Element element;
		for (int yy = 0; yy < Application.RESOLUTION_DOMAIN_HEIGHT; yy++) {
			for (int xx = 0; xx < Application.RESOLUTION_DOMAIN_WIDTH; xx++) {
				element = elementsScreen[xx][yy];
				if (element.isHibernatedBlack() || element.isHibernatedBlack_Neighbour()) {
					element.resetForOptimization();
				}
			}
		}
	}

	public void resetOptimizationHard() {

		Element element;
		for (int yy = 0; yy < Application.RESOLUTION_DOMAIN_HEIGHT; yy++) {
			for (int xx = 0; xx < Application.RESOLUTION_DOMAIN_WIDTH; xx++) {
				element = elementsScreen[xx][yy];
				if (!element.isActiveMoved() && !element.isHibernatedFinished() && !element.isHibernatedFinishedInside()) {
					element.resetAsNew();
				}
			}
		}
	}

	public boolean isOptimizationBreakAndFix() {
		/* Last tested pixel is Hibernated as Converged (Calculation finished) */
		Bool lastIsWhite = new Bool();
		/* Last tested pixel is Hibernated as Skipped for calculation (Deep black) */
		Bool lastIsBlack = new Bool();
		ArrayList<Integer> failedNumbersRe = new ArrayList<>();
		ArrayList<Integer> failedNumbersIm = new ArrayList<>();
		/* Test lines left and right */
		for (int yy = 0; yy < Application.RESOLUTION_DOMAIN_HEIGHT; yy++) {
			for (int xx = 0; xx < Application.RESOLUTION_DOMAIN_WIDTH; xx++) {
				FractalMachine.testOptimizationBreakElement(xx, yy, elementsScreen[xx][yy], failedNumbersRe, failedNumbersIm, lastIsWhite, lastIsBlack);
			}
			lastIsBlack.setFalse();
			lastIsWhite.setFalse();
		}
		/* Test lines up and down */
		for (int xx = 0; xx < Application.RESOLUTION_DOMAIN_WIDTH; xx++) {
			for (int yy = 0; yy < Application.RESOLUTION_DOMAIN_HEIGHT; yy++) {
				FractalMachine.testOptimizationBreakElement(xx, yy, elementsScreen[xx][yy], failedNumbersRe, failedNumbersIm, lastIsWhite, lastIsBlack);
			}
			lastIsBlack.setFalse();
			lastIsWhite.setFalse();
		}
		/* Fix failed positions */
		/* In worst case failed positions contains same position twice */
		int size = failedNumbersRe.size();
		for (int i = 0; i < size; i++) {
			// Time.now("FIXING: " + position.x + ". " + position.y);
			final int r = Application.TEST_OPTIMIZATION_FIX_SIZE;
			for (int x = -r; x < r; x++) {
				for (int y = -r; y < r; y++) {
					if ((x * x) + (y * y) < (r * r)) {
						// These thing should be much optimized to not do same for points it was already done
						FractalMachine.setActiveMovedIfBlack(failedNumbersRe.get(i) + x, failedNumbersIm.get(i) + y, elementsScreen);
					}
				}
			}
		}
		return !failedNumbersRe.isEmpty();
	}

}
