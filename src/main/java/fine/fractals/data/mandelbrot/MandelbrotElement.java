package fine.fractals.data.mandelbrot;

import static fine.fractals.data.mandelbrot.MandelbrotPixelState.ActiveNew;
import static fine.fractals.data.mandelbrot.MandelbrotPixelState.FinishedSuccess;
import static fine.fractals.data.mandelbrot.MandelbrotPixelState.FinishedSuccessPast;
import static fine.fractals.data.mandelbrot.MandelbrotPixelState.FinishedTooLong;
import static fine.fractals.data.mandelbrot.MandelbrotPixelState.FinishedTooShort;
import static fine.fractals.data.mandelbrot.MandelbrotPixelState.HibernatedDeepBlack;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_MAX;
import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_min;

public class MandelbrotElement implements Comparable<MandelbrotElement> {

    public final double originRe;
    public final double originIm;

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
    public boolean isHibernated() {
        return state == FinishedTooShort || state == HibernatedDeepBlack;
    }

    public boolean isFinishedSuccessAny() {
        return state == FinishedSuccessPast || state == FinishedSuccess;
    }

    public boolean isFinishedSuccessNow() {
        return state == FinishedSuccess;
    }

    public boolean isFinishedSuccessPast() {
        return state == FinishedSuccessPast;
    }

    public void setFinishedState(int iterator, int pathLength) {
        if (iterator == ITERATION_MAX) {
            state = FinishedTooLong;
            return;
        }
        if (pathLength < ITERATION_min) {
            state = FinishedTooShort;
            return;
        }
        state = FinishedSuccess;
    }

    public MandelbrotPixelState state() {
        return state;
    }

    public void past() {
        if (state == FinishedSuccess) {
            state = FinishedSuccessPast;
        }
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
