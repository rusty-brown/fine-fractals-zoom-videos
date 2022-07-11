package fine.fractals.images;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.NAME;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_WIDTH;
import static fine.fractals.machine.ApplicationImpl.APP_NAME;
import static fine.fractals.machine.ApplicationImpl.DEBUG_PATH;
import static fine.fractals.machine.ApplicationImpl.FILE_PATH;
import static fine.fractals.machine.ApplicationImpl.IGNORE_DEBUG_FILES;
import static fine.fractals.machine.ApplicationImpl.iteration;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static javax.imageio.ImageWriteParam.MODE_EXPLICIT;

public class FractalImages {

    private static final Logger log = LogManager.getLogger(FractalImages.class);

    public static final BufferedImage FinebrotImage = new BufferedImage(RESOLUTION_WIDTH, RESOLUTION_HEIGHT, TYPE_INT_RGB);

    public static final BufferedImage MandelbrotMaskImage = new BufferedImage(RESOLUTION_WIDTH, RESOLUTION_HEIGHT, TYPE_INT_RGB);

    public static void saveImages() {
        log.debug("saveImages()");
        try {
            final String finebrotName = FILE_PATH + NAME + APP_NAME + iteration() + ".jpg";
            log.debug("Finebrot image: " + finebrotName);
            saveImage(finebrotName, FinebrotImage);

            if (!IGNORE_DEBUG_FILES) {
                final String mandelbrotName = DEBUG_PATH + NAME + APP_NAME + iteration() + "_mandelbrot.jpg";
                log.debug("Mandelbrot image: " + mandelbrotName);
                saveImage(mandelbrotName, MandelbrotMaskImage);
            }

            log.debug("saved.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void saveImage(String fractalName, BufferedImage fractalImage) throws IOException {
        final JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
        jpegParams.setCompressionMode(MODE_EXPLICIT);
        jpegParams.setCompressionQuality(0.98f);

        final ImageWriter iw = ImageIO.getImageWritersByFormatName("jpg").next();
        try (FileImageOutputStream fis = new FileImageOutputStream(new File(fractalName))) {
            iw.setOutput(fis);
            iw.write(null, new IIOImage(fractalImage, null, null), jpegParams);
            fis.flush();
            iw.dispose();
        }
    }

    private static String iteration() {
        return "_" + String.format("%06d", iteration);
    }
}
