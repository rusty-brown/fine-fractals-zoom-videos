package fine.fractals.data;

import fine.fractals.data.objects.FastList;
import fine.fractals.data.objects.Missing;
import fine.fractals.fractal.Fractal;
import fine.fractals.math.AreaImage;
import fine.fractals.math.common.Element;
import fine.fractals.math.common.HH;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Dynamic {

    private static final Logger log = LogManager.getLogger(Dynamic.class);

    /**
     * re; im; color
     */
    private final LinkedHashMap<Double, LinkedHashMap<Double, Integer>> dynamicRowRe = new LinkedHashMap<>();

    private AreaImage areaImage;
    private final Screen screen;

    public Dynamic(Screen screen) {
        this.screen = screen;
    }

    public void setAreaImage(AreaImage areaImage) {
        this.areaImage = areaImage;
    }

    /* All elements on escape path are already inside displayed area */
    // TODO why?, how?, really?
    public synchronized void addEscapePathInside(FastList originPathRe, FastList originPathIm) {

        LinkedHashMap<Double, Integer> dynamicRowIm;
        double im;

        HH hh = new HH();
        int size = originPathRe.size();

        int i = 0;
        for (Double re : originPathRe) {
            dynamicRowIm = this.dynamicRowRe.get(re);
            im = originPathIm.get(i);

            Fractal.ME.colorsFor(hh, i + 1, size);
            Integer pathElementColor = Screen.hhColorToInt(hh);


            if (dynamicRowIm != null) {
                if (!dynamicRowIm.containsKey(im)) {
                    /* Almost all rowIm have length one */
                    /* Remove different paths hit same element */
                    dynamicRowIm.put(im, pathElementColor);
                }
            } else {
                dynamicRowIm = new LinkedHashMap<>();
                dynamicRowIm.put(im, pathElementColor);
                this.dynamicRowRe.put(re, dynamicRowIm);
            }
            i++;
        }
    }

    public void addIfMissing(LinkedList<Missing> missingList) {
        double x;
        double xNew;
        double y;
        double originRe = 0;
        double originIm = 0;
        int iterator;
        Element element;

        ArrayList<Double> missingOriginPathRe = new ArrayList<>();
        ArrayList<Double> missingOriginPathIm = new ArrayList<>();

        HH hh = new HH();

        int missingNo = 1;
        int addedMissingElementsTotal = 0;
        int wastedMissingElementsTotal = 0;

        for (Missing missing : missingList) {
            int addedMissingElements = 0;
            int wastedMissingElements = 0;
            element = missing.element;
            x = 0;
            y = 0;
            /* Some hits inside may have been found already by iterating from IterationLast to current IterationMax */
            originRe = element.originReT;
            originIm = element.originImX;
            iterator = 0;

            /* Original */
            while (iterator < missing.iterateTo) {

                // TODO
                xNew = (x * x) - (y * y) + originRe;
                hh.calculation.im = 2 * x * y + originIm;
                hh.calculation.re = xNew;

                if (areaImage.contains(hh)) {
                    missingOriginPathRe.add(x);
                    missingOriginPathIm.add(y);
                }
                iterator++;
            }

            if (missing.originPathRe.size() + missingOriginPathRe.size() > Fractal.ITERATION_MAX) {
                // Add Missing  path to Fractal

                element.setHibernatedFinishedInside();
                missing.originPathRe.list().addAll(missingOriginPathRe);
                missing.originPathIm.list().addAll(missingOriginPathIm);

                // TODO ?
                // this.addEscapePathInside(missing.originPathRe, missing.originPathIm);

                addedMissingElements += missing.originPathRe.size();
                addedMissingElementsTotal += addedMissingElements;
            } else {
                // Don't add this path
                wastedMissingElements += iterator;
                wastedMissingElementsTotal += wastedMissingElements;
            }
            missingOriginPathRe.clear();
            missingOriginPathIm.clear();
            missingNo++;
        }
    }

    public void domainToScreenGrid() {

        int removed = 0;

        HH hh = new HH();
        double im;

        Set<Double> rowRe = this.dynamicRowRe.keySet();
        for (Double re : rowRe) {
            LinkedHashMap<Double, Integer> dynamicRowIm = this.dynamicRowRe.get(re);

            if (dynamicRowIm != null) {

                Iterator<Double> imIterator = dynamicRowIm.keySet().iterator();

                while (imIterator.hasNext()) {
                    im = imIterator.next();

                    int color = dynamicRowIm.get(im);
                    Screen.intToHHColor(color, hh);

                    this.areaImage.domainToScreenCarry(hh, re, im);

                    if (hh.calculation.pxRe != HH.NOT && hh.calculation.pxIm != HH.NOT) {
                        /* Multiple elements hit same pixel */

                        this.screen.add(hh);

                    } else {
                        /* remove im on rowRe */
                        removed++;
                        imIterator.remove();
                    }
                }
            } else {
                rowRe.remove(re);
            }
        }
        log.info("* Removed: " + removed);

        /* Elements im on rowRw were remove already while iterating with Iterator.remove */

        /* Remove full rowRe, if it moved outside of area with all its row Ims */
        final double left = areaImage.left();
        final double right = areaImage.right();


        this.dynamicRowRe.entrySet().removeIf(entry -> entry.getKey() < left || entry.getKey() > right);
        /* Remove rowsRe where all Ims were removed */
        this.dynamicRowRe.entrySet().removeIf(entry -> entry.getValue().size() == 0);


        log.info("TEST IF EXTRA ELEMENTS ARE REMOVED");
        areaImage.writeBorders();
        Set<Double> rowReTest = this.dynamicRowRe.keySet();
        for (Double re : rowReTest) {
            LinkedHashMap<Double, Integer> dynamicRowImTest = this.dynamicRowRe.get(re);
            Iterator<Double> imIterator = dynamicRowImTest.keySet().iterator();

            while (imIterator.hasNext()) {
                im = imIterator.next();

                if (!areaImage.contains(re, im)) {
                    System.out.println("REMOVE: " + re + " ; " + im);
                    imIterator.remove();
                }
            }
        }
        log.info("done.");
    }

}
