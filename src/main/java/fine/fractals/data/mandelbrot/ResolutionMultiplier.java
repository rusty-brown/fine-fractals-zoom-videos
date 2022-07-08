package fine.fractals.data.mandelbrot;

@SuppressWarnings(value = "unused")
public enum ResolutionMultiplier {
    /**
     * single point in the center of Mandelbrot pixel
     */
    none,

    /**
     * two opposite square corners altering each calculation
     * center point remains the only relevant re,im point for state of Mandelbrot domain pixel (hibernated, active, active new, etc)
     */
    square_alter,

    /**
     * Fill each pixel wil NxN points.
     * The center point is in the middle of a pixel, use odd numbers to fill pixels perfectly
     */
    square_3,
    square_5,
    square_11,
    square_51,
    square_101
}
