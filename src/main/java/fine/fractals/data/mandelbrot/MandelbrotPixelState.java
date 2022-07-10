package fine.fractals.data.mandelbrot;

public enum MandelbrotPixelState {

    /**
     * 1.
     * Path length more than ITERATION_MIN, this element produced good data.
     * color = {@link MandelbrotMaskColors#FINISHED_SUCCESS}
     */
    FinishedSuccess,

    /**
     * 2.
     * New element just added to pixels
     * color = {@link MandelbrotMaskColors#ACTIVE_NEW}
     */
    ActiveNew,

    /**
     * 3.
     * Path length was less than ITERATION_MIN
     * color = {@link MandelbrotMaskColors#FINISHED_TOO_SHORT}
     */
    FinishedTooShort,


    /**
     * 4.
     * Path length reached ITERATION_MAX
     * color = {@link MandelbrotMaskColors#FINISHED_TOO_LONG}
     */
    FinishedTooLong,

    /**
     * 5.
     * Created as already hibernated, and won't be calculated.
     * No: Because it didn't have any good, data producing neighbours {@link #FinishedSuccess} near enough.
     * Yes: Because it had only {@link MandelbrotPixelState#FinishedTooLong} neighbours.
     * color = {@link MandelbrotMaskColors#HIBERNATED_DEEP_BLACK}
     */
    HibernatedDeepBlack,

}
