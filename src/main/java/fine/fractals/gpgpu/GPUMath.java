package fine.fractals.gpgpu;

public abstract class GPUMath {

    public static final String quadrance = """
            (re * re) + (im * im);
            """;

    public static final String square = """
            temp = (re * re) - (im * im);
            im = 2 * re * im;
            re = temp;
            """;

    public static final String plusOrigin = """
             re = re + ore;
             im = im + oim;
            """;
}
