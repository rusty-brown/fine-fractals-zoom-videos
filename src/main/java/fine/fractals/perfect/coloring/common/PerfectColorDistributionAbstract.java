package fine.fractals.perfect.coloring.common;

import fine.fractals.data.finebrot.FinebrotPixel;

import java.util.Comparator;

import static fine.fractals.context.TargetImpl.Target;
import static java.lang.Integer.compare;
import static java.lang.Math.abs;

/**
 * Method used for perfect coloring is
 * - Gather all screen pixels and order them by value
 * - Count how many pixels should be colored by each color from spectrum
 * - Zero elements and noise color by the lowest color
 * - Color all significant pixels ordered by value
 */
public abstract class PerfectColorDistributionAbstract {

    protected final double px = Target.re();
    protected final Comparator<FinebrotPixel> comparator = (a, b) -> {
        int c = compare(a.pixelValue(), b.pixelValue());
        if (c == 0) {
            return Double.compare(abs(a.px() - px), abs(b.px() - px));
        }
        return c;
    };

    abstract public void perfectlyColorFinebrotValues();

}
