package fine.fractals.images;

import java.awt.image.BufferedImage;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class FractalImage {

    public static final BufferedImage FinebrotImage = new BufferedImage(RESOLUTION_WIDTH, RESOLUTION_HEIGHT, TYPE_INT_RGB);

    public static final BufferedImage MandelbrotMaskImage = new BufferedImage(RESOLUTION_WIDTH, RESOLUTION_HEIGHT, TYPE_INT_RGB);
}
