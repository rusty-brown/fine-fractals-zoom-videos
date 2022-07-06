package fine.fractals.data;

import static fine.fractals.math.MathematicianImpl.Mathematician;

public class MemEuler extends Mem {

    public int iteration = 0;

    public void euler() {
        if (Mathematician.isPrime(iteration)) {
            re = 0.01 / re;
            im = 0.01 / im;
        }
        iteration++;
    }
}
