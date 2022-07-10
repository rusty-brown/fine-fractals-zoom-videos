package fine.fractals.data.mandelbrot;

import static fine.fractals.data.mandelbrot.MandelbrotPixelState.ActiveNew;
import static fine.fractals.data.mandelbrot.MandelbrotPixelState.FinishedSuccess;
import static fine.fractals.data.mandelbrot.MandelbrotPixelState.FinishedTooLong;
import static fine.fractals.data.mandelbrot.MandelbrotPixelState.FinishedTooShort;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_MAX;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_MIN;

public class MandelbrotElement implements Comparable<MandelbrotElement> {

    public final double originRe;
    public final double originIm;

    private int value;
    private MandelbrotPixelState state;

    MandelbrotElement(double originRe, double originIm, MandelbrotPixelState state) {
        this.originRe = originRe;
        this.originIm = originIm;
        this.state = state;
    }

    public boolean isActiveNew() {
        return this.state == ActiveNew;
    }

    public boolean isFinishedTooShort() {
        return state == FinishedTooShort;
    }

    public boolean isFinishedSuccess() {
        return state == FinishedSuccess;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setHibernatedState(int iterator) {
        if (iterator < ITERATION_MIN) {
            state = FinishedTooShort;
            return;
        }
        if (iterator < ITERATION_MAX) {
            state = FinishedSuccess;
        } else {
            state = FinishedTooLong;
        }
    }

    public MandelbrotPixelState state() {
        return state;
    }

    /**
     * Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
     */
    @Override
    public int compareTo(MandelbrotElement e) {
        if (this == e) return 0;
        return this.state.compareTo(e.state);
    }

    public boolean hasWorseStateThen(MandelbrotElement e) {
        return this.compareTo(e) > 0;
    }
}
