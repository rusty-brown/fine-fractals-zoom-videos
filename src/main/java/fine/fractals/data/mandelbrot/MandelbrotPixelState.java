package fine.fractals.data.mandelbrot;

public enum MandelbrotPixelState {

    /**
     * 1.
     * Calculation path Finished with success in previous calculation iteration (zoom).
     * This is updated state from previous state {@link #FinishedSuccess}.
     * If there was a conflict when moving pixels to new location after zoomIn(), use this state.
     * There won't be any difference in Finebrot data, only in mandelbrot pixel state and color.
     * color = {@link MandelbrotMaskColors#FINISHED_SUCCESS_PAST}
     */
    FinishedSuccessPast,

    /**
     * 2.
     * Path length more than ITERATION_min, this element produced good data.
     * color = {@link MandelbrotMaskColors#FINISHED_SUCCESS}
     */
    FinishedSuccess,


    /**
     * 3.
     * New element just added to Mandelbrot Pixels
     * color = {@link MandelbrotMaskColors#ACTIVE_NEW}
     */
    ActiveNew,

    /**
     * 4.
     * Path length was less than ITERATION_min.
     * color = {@link MandelbrotMaskColors#FINISHED_TOO_SHORT}
     */
    FinishedTooShort,


    /**
     * 5.
     * Path length reached ITERATION_MAX.
     * It is considered as inside of Mandelbrot set.
     * color = {@link MandelbrotMaskColors#FINISHED_TOO_LONG}
     */
    FinishedTooLong,

    /**
     * 6.
     * Created as already hibernated, and won't be calculated.
     * It didn't have any good data producing neighbours {@link #FinishedSuccess} near enough.
     * It had only {@link #FinishedTooLong} neighbours.
     * color = {@link MandelbrotMaskColors#HIBERNATED_DEEP_BLACK}
     */
    HibernatedDeepBlack,

}
