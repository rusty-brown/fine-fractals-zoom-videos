package fine.fractals.machine;

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

import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.NAME;
import static fine.fractals.images.FractalImage.FinebrotImage;
import static fine.fractals.images.FractalImage.MandelbrotMaskImage;
import static fine.fractals.machine.ApplicationImpl.APP_NAME;
import static fine.fractals.machine.ApplicationImpl.DEBUG_PATH;
import static fine.fractals.machine.ApplicationImpl.FILE_PATH;
import static fine.fractals.machine.ApplicationImpl.IGNORE_DEBUG_FILES;
import static fine.fractals.machine.ApplicationImpl.iteration;
import static javax.imageio.ImageWriteParam.MODE_EXPLICIT;

public abstract class FractalMachine {

	private static final Logger log = LogManager.getLogger(FractalMachine.class);

	public static void saveImages() {
		log.debug("saveImages()");
		try {
			final String finebrotName = FILE_PATH + NAME + APP_NAME + iteration() + ".jpg";
			log.info("Finebrot image: " + finebrotName);
			saveImage(finebrotName, FinebrotImage);

			if (!IGNORE_DEBUG_FILES) {
				final String mandelbrotName = DEBUG_PATH + NAME + APP_NAME + iteration() + "_mandelbrot.jpg";
				log.info("Mandelbrot image: " + mandelbrotName);
				saveImage(mandelbrotName, MandelbrotMaskImage);
			}

			log.info("saved.");
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
