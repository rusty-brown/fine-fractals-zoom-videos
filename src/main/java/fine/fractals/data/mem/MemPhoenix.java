package fine.fractals.data.mem;

import static fine.fractals.fractal.finebrot.phoenix.FractalPhoenix.phoenix_initializer;

/**
 * Memory object for Phoenix fractal
 */
public class MemPhoenix extends Mem {

    /*
     * Values of previous calculation results
     */
    public double prev_prev_re = phoenix_initializer;
    public double prev_prev_im = phoenix_initializer;
    public double prev_re = phoenix_initializer;
    public double prev_im = phoenix_initializer;

    public MemPhoenix(double originRe, double originIm) {
        super(originRe, originIm);
    }
}
