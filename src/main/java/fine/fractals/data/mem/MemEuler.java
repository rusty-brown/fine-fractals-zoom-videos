package fine.fractals.data.mem;

import fine.fractals.fractal.finebrot.euler.PixelsEulerFinebrotImpl;

import static fine.fractals.math.MathematicianImpl.Mathematician;

/**
 * Memory object for Euler fractal
 */
public class MemEuler extends Mem {

    public int iteration = 0;

    public PixelsEulerFinebrotImpl.Spectra spectra;

    public MemEuler() {
    }

    public MemEuler(double originRe, double originIm) {
        super(originRe, originIm);
    }

    public void euler() {
        if (Mathematician.isPrime(++iteration)) {
            re = 0.01 / re;
            im = 0.01 / im;
        }
    }
}
