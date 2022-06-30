package fine.fractals.mandelbrot;

public enum MandelbrotState {
    ActiveNew,
    ActiveMoved,
    ActiveFixed,
    ActiveRecalculate,

    HibernatedFinished,
    HibernatedFinishedInside,
    HibernatedBlack,
    HibernatedBlackNeighbour,

}
