package fine.fractals.data;

@SuppressWarnings(value = "unused")
public enum ResolutionMultiplier {
    /**
     * single point in the center of Mandelbrot pixel
     */
    none,
    /**
     * two opposite square corners altering each calculation
     * center point remains only relevant re,im point for state of Mandelbrot domain pixel (hibernated, active, active new, etc)
     */
    one_square
}
