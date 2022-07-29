package fine.fractals.data.mem;

/**
 * Memory object to carry calculation results.
 */
public class Mem {

    public double re;
    public double im;
    /**
     * pixel x
     */
    public int px;
    /**
     * pixel y
     */
    public int py;
    /**
     * to validate [re,im] <-> [px,py] translation
     */
    public boolean good;

    public Mem() {
    }

    public Mem(double originRe, double originIm) {
        this.re = originRe;
        this.im = originIm;
    }

    public void plus(double r, double i) {
        re = re + r;
        im = im + i;
    }

    public void square() {
        double temp = (re * re) - (im * im);
        im = 2 * re * im;
        re = temp;
    }

    /**
     * Q(a*b) = Q(a) * Q(b)
     */
    public double quadrance() {
        return (re * re) + (im * im);
    }

    public void conjugation() {
        im = -im;
    }
}
