package fine.fractals.data.mem;

/**
 * Memory object for Phoenix fractal
 */
public class MemPhoenix extends Mem {

    /*
     * Values of previous calculation results
     */
    public double prev_prev_re;
    public double prev_prev_im;
    public double prev_re;
    public double prev_im;

    public MemPhoenix(double originRe, double originIm) {
        super(originRe, originIm);
    }
}
