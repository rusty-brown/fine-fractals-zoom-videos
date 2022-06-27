package fine.fractals.math;

import fine.fractals.Application;
import fine.fractals.Main;
import fine.fractals.data.Dynamic;
import fine.fractals.data.Screen;
import fine.fractals.data.objects.FastList;
import fine.fractals.fractal.Fractal;
import fine.fractals.math.common.HH;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.HashMap;

import static fine.fractals.color.things.Palette.rgb255;

public class Design {

    public static Design ME;
    private static final Logger log = LogManager.getLogger(Design.class);

    public Screen screen;
    public Dynamic dynamic;
    private AreaImage areaImage;

    private int counterAll = 0;
    private ColoredValueIntegral coloredValueIntegral;

    private HashMap<Integer, Color> finalColorsR;
    private HashMap<Integer, Color> finalColorsG;
    private HashMap<Integer, Color> finalColorsB;

    private int allPixels;
    private int allColorsCountR;
    private int allColorsCountG;
    private int allColorsCountB;

    private Color rr;
    private Color bb;
    private Color gg;

    public Design() {
        log.info("Design constructor");
        this.screen = new Screen();
        ME = this;

        if (!Application.DESIGN_STATIC) {
            this.dynamic = new Dynamic(screen);
        }
    }

    public void resetAsNew() {
        this.screen = new Screen();
    }

    public void calculationFinished() {

        if (!Application.DESIGN_STATIC) {
            dynamicToScreen();
        }


        this.screen.updateMiniMax();

        int[] maxScrValue3 = this.screen.maxSrc3();
        int[] minScrValue3 = this.screen.minSrc3();

        log.info("update palette for orbital density");
        Main.colorPalette.update3(maxScrValue3, minScrValue3);


        log.info("--------------");
        log.info("# " + counterAll);
        log.info("--------------");
        counterAll = 0;
    }

    /* All elements on escape path are already inside displayed image area */
    // TODO why, how, really?
    public void addEscapePathToSpectraNow(HH hh, FastList originPathReT, FastList originPathImX) {

        final int screenHalfX = Main.RESOLUTION_IMAGE_HIGHT / 2;
        final int size = originPathReT.size();

        int counter = 0;


        for (int i = 0; i < size; i++) {

            counter++;

            double reT = originPathReT.get(i);
            double imX = originPathImX.get(i);

            Fractal.ME.colorsFor(hh, counter, size);

            if (Application.DESIGN_STATIC) {

                /**
                 * This takes element from domain and calculates it's position on the screen.
                 * If the position is somewhere inside area Image then it is good.
                 *
                 * Contains is solved in Mandelbrot
                 */
                areaImage.domainToScreenCarry(hh, reT, imX);

                if (hh.calculation.pxRe != HH.NOT && hh.calculation.pxIm != HH.NOT) {

                    /** To make images of individual paths
                     if (onePathLimit) {
                     onePath[hh.calculation.pxT][hh.calculation.pxX] = 1;
                     try {
                     onePath[hh.calculation.pxT + 1][hh.calculation.pxX] = 1;
                     onePath[hh.calculation.pxT][hh.calculation.pxX + 1] = 1;
                     onePath[hh.calculation.pxT + 1][hh.calculation.pxX + 1] = 1;
                     } catch (Exception e) {
                     time.red("It's evil to ignore exceptions");
                     }
                     }*/

                    this.screen.add(hh);

                    if (Fractal.OPTIMIZE_SYMMETRY) {
                        /* reflect around screen half */
                        int x = hh.calculation.pxIm;

                        if (hh.calculation.pxIm < screenHalfX) x = screenHalfX + (screenHalfX - x);
                        if (hh.calculation.pxIm > screenHalfX) x = screenHalfX - (hh.calculation.pxIm - screenHalfX);
                        if (x == Main.RESOLUTION_IMAGE_HIGHT) x = Main.RESOLUTION_IMAGE_HIGHT - 1;

                        this.screen.add(hh.calculation.pxRe, x, hh);
                    }
                }
            } else {
                dynamic.addEscapePathInside(originPathReT, originPathImX);
            }
        }

        /** To make images of individual paths
         if (onePathLimit) {
         FractalMachine.saveImage(onePath);
         }
         */

        counterAll += counter;
    }

    public void createValueIntegral() {

        this.finalColorsR = new HashMap<>();
        this.finalColorsG = new HashMap<>();
        this.finalColorsB = new HashMap<>();

        this.coloredValueIntegral = new ColoredValueIntegral(this.screen);
        /** pixelCountToColorByOneColor */
        this.allPixels = Main.RESOLUTION_IMAGE_WIDTH * Main.RESOLUTION_IMAGE_HIGHT;
        this.allColorsCountR = Main.colorPalette.colorResolutionR();
        this.allColorsCountG = Main.colorPalette.colorResolutionG();
        this.allColorsCountB = Main.colorPalette.colorResolutionB();
        final int fullColoringR = ((int) ((double) allPixels / (double) allColorsCountR));
        final int fullColoringG = ((int) ((double) allPixels / (double) allColorsCountG));
        final int fullColoringB = ((int) ((double) allPixels / (double) allColorsCountB));
        final int leftR = allPixels - (allColorsCountR * fullColoringR);
        final int leftG = allPixels - (allColorsCountG * fullColoringG);
        final int leftB = allPixels - (allColorsCountB * fullColoringB);

        log.info("allPixels: " + allPixels);
        log.info("allColorsCount: " + allColorsCountR + "," + allColorsCountG + "," + allColorsCountB);
        log.info("fullColoring: " + fullColoringR + "," + fullColoringG + "," + fullColoringB);
        log.info("left*: " + leftR + ", " + leftG + ", " + leftB);

        this.coloredValueIntegral.setLeftR(leftR);
        this.coloredValueIntegral.setLeftG(leftG);
        this.coloredValueIntegral.setLeftB(leftB);
        this.coloredValueIntegral.setFullColoringR(fullColoringR);
        this.coloredValueIntegral.setFullColoringG(fullColoringG);
        this.coloredValueIntegral.setFullColoringB(fullColoringB);


        int colorIndexR = 0;

        /* for all colors R */
        while (colorIndexR < allColorsCountR) {
            int colorLeft = fullColoringR;
            /* until there is any color left */
            while (colorLeft > 0) {
                colorLeft = coloredValueIntegral.applyPaintR(colorIndexR, colorLeft);
                if (colorLeft == 0) {
                    // no color left, switch to next color
                    colorIndexR++;
                    break;
                }
            }
        }

        int colorIndexG = 0;
        /* for all colors G */
        while (colorIndexG < allColorsCountG) {
            int colorLeft = fullColoringG;
            /* until there is any color left */
            while (colorLeft > 0) {
                colorLeft = coloredValueIntegral.applyPaintG(colorIndexG, colorLeft);
                if (colorLeft == 0) {
                    // no color left, switch to next color
                    colorIndexG++;
                    break;
                }
            }
        }

        int colorIndexB = 0;
        /* for all colors B */
        while (colorIndexB < allColorsCountB) {
            int colorLeft = fullColoringB;
            /* until there is any color left */
            while (colorLeft > 0) {
                colorLeft = coloredValueIntegral.applyPaintB(colorIndexB, colorLeft);
                if (colorLeft == 0) {
                    // no color left, switch to next color
                    colorIndexB++;
                    break;
                }
            }
        }


        this.coloredValueIntegral.finalize(this.finalColorsR, this.finalColorsG, this.finalColorsB);
    }

    public Color colorAt(int t, int x) {

        int r = this.screen.red(t, x);
        int g = this.screen.green(t, x);
        int b = this.screen.blue(t, x);

        rr = this.finalColorsR.get(r);
        gg = this.finalColorsG.get(g);
        bb = this.finalColorsB.get(b);

        r = (rr.getRed() + gg.getRed() + bb.getRed()) / 3;
        g = (rr.getGreen() + gg.getGreen() + bb.getGreen()) / 3;
        b = (rr.getBlue() + gg.getBlue() + bb.getBlue()) / 3;

        if (r > rgb255) {
            r = rgb255;
        }
        if (g > rgb255) {
            g = rgb255;
        }
        if (b > rgb255) {
            b = rgb255;
        }

        return new Color(r, g, b);
    }

    public void clearScreenValues() {
        log.info("CLEAR SCREEN VALUES");

        this.screen.clear();

    }

    public void setAreaImage(AreaImage areaImage) {
        this.areaImage = areaImage;
        if (dynamic != null) {
            this.dynamic.setAreaImage(areaImage);
        }
    }


	/* TODO
	public void addIfMissing(LinkedList<Missing> missingList) {
		double imX;
		double xNew;
		double y;
		double originRe;
		double originIm;
		int iterator;
		Element element;

		ArrayList<Double> missingOriginPathRe = new ArrayList<>();
		ArrayList<Double> missingOriginPathIm = new ArrayList<>();

		int missingNo = 1;
		int addedMissingElementsTotal = 0;
		int wastedMissingElementsTotal = 0;

		for (Missing missing : missingList) {
			int addedMissingElements = 0;
			int wastedMissingElements = 0;
			element = missing.element;
			imX = 0;
			y = 0;
			// Some hits inside may have been found already by iterating from IterationLast to current IterationMax
			originRe = element.originRe();
			originIm = element.originIm();
			iterator = 0;

            // Original
			while (iterator < missing.iterateTo) {

				xNew = (imX * imX) - (y * y) + originRe;
				y = 2 * imX * y + originIm;
				imX = xNew;


				if (areaImage.contains(imX, y)) {
					missingOriginPathRe.add(imX);
					missingOriginPathIm.add(y);
				}
				iterator++;
			}

			if (missing.originPathRe.size() + missingOriginPathRe.size() > Application.iterationMinGet()) {
				// Add Missing  path to Fractal

				element.setHibernatedFinishedInside();
				missing.originPathRe.addAll(missingOriginPathRe);
				missing.originPathIm.addAll(missingOriginPathIm);

                // Add missing to Design here
				this.addEscapePathInside(missing.originPathRe, missing.originPathIm);


				addedMissingElements += missing.originPathRe.size();
				addedMissingElementsTotal += addedMissingElements;
				Time.debug("* addedMissingForElements Yes " + missingNo + " of " + missingList.size() + ": " + addedMissingElements + " " + Data.par(addedMissingElementsTotal));
			} else {
				// Don're add this path
				wastedMissingElements += iterator;
				wastedMissingElementsTotal += wastedMissingElements;
				Time.debug("* addedMissingForElements No  " + missingNo + " of " + missingList.size() + ": " + wastedMissingElements + " " + Data.par(wastedMissingElementsTotal));
			}
			missingOriginPathRe.clear();
			missingOriginPathIm.clear();
			missingNo++;
		}
	}
*/

    public void dynamicToScreen() {
        log.info("remove elements outside start");
        this.dynamic.domainToScreenGrid();
    }

}
