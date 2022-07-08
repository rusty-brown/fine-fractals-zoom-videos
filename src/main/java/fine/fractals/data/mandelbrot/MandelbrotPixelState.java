package fine.fractals.data.mandelbrot;

public enum MandelbrotPixelState {

    ActiveNew,
    /**
     * It was moved to new Mandelbrot pixel during zoom()
     */
    ActiveMoved,
    ActiveFixed,
    ActiveRecalculate,

    /**
     * Path length was less than ITERATION_MIN
     */
    HibernatedFinishedTooShort,
    HibernatedFinishedLong,

    /**
     * Convergent elements of Mandelbrot set which don't produce any Finebrot data
     */
    HibernatedBlack,
    HibernatedBlackNeighbour,

}
