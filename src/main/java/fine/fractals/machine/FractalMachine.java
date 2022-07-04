package fine.fractals.machine;

import fine.fractals.data.MandelbrotElement;
import fine.fractals.data.misc.Bool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static fine.fractals.Main.*;
import static fine.fractals.context.ApplicationImpl.APP_NAME;
import static fine.fractals.context.ApplicationImpl.iteration;
import static fine.fractals.fractal.Fractal.NAME;
import static fine.fractals.images.FractalImage.FinebrotImage;
import static javax.imageio.ImageWriteParam.MODE_EXPLICIT;

public abstract class FractalMachine {

	private static final Logger log = LogManager.getLogger(FractalMachine.class);

	public static void saveImage() {
		try {
			JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
			jpegParams.setCompressionMode(MODE_EXPLICIT);
			jpegParams.setCompressionQuality(0.98f);

			String fileName = FILE_PATH
					+ NAME + APP_NAME
					+ iteration()
					+ ".jpg";

			File outputFile = new File(fileName);
			log.info("Image: " + fileName);
			final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();

			try (FileImageOutputStream fis = new FileImageOutputStream(outputFile)) {
				writer.setOutput(fis);
				writer.write(null, new IIOImage(FinebrotImage, null, null), jpegParams);
				fis.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private static String iteration() {
		return "_" + String.format("%06d", iteration);
	}

	public static boolean isVeryDeepBlack(int xx, int yy, MandelbrotElement[][] elements) {
		if (elements[xx][yy].getValue() > 0) {
			return false;
		}
		Integer value;
		/* r is a radius of a circle to verify non-zero values around */
		final int r = neighbours + 1;
		for (int x = -r; x < r; x++) {
			for (int y = -r; y < r; y++) {
				if ((x * x) + (y * y) < (r * r)) {
					/* Find value to be carried by HH.calculation */
					int x2 = xx + x;
					int y2 = yy + y;
					value = valueAt(x2, y2, elements);
					if (value == null || value > 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean someNeighboursFinishedLong(int xx, int yy, MandelbrotElement[][] elements) {
		/* r is a radius of a square to verify if any neighbor px finished long (red pixel) */
		MandelbrotElement el;
		for (int x = -neighbours; x < neighbours; x++) {
			for (int y = -neighbours; y < neighbours; y++) {
				if ((x * x) + (y * y) <= (neighbours * neighbours)) {
					int x2 = xx + x;
					int y2 = yy + y;
					if (checkDomain(x2, y2)) {
						el = elements[x2][y2];
						if (el != null && el.isHibernatedFinishedLong()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean checkDomain(int x, int y) {
		return x >= 0 && x < RESOLUTION_WIDTH
				&& y >= 0 && y < RESOLUTION_HEIGHT;
	}

	private static Integer valueAt(int x, int y, MandelbrotElement[][] mandelbrot) {
		if (checkDomain(x, y)) {
			MandelbrotElement element = mandelbrot[x][y];
			return element.getValue();
		}
		return null;
	}

	public static void setAsDeepBlack(int x, int y, MandelbrotElement[][] elements) {
		if (checkDomain(x, y)) {
			/* Most probably Element of Mandelbrot set, don't recalculate */
			elements[x][y].setHibernatedBlackNeighbour();
		}
	}

	public static void testOptimizationBreakElement(int x, int y, MandelbrotElement element, ArrayList<Integer> failedNumbersRe, ArrayList<Integer> failedNumbersIm, Bool lastIsWhite, Bool lastIsBlack) {
		if (element.isHibernatedBlack_Neighbour() || element.isHibernatedBlack()) {
			if (lastIsWhite.is()) {
				failedNumbersRe.add(x);
				failedNumbersIm.add(y);
			}
			lastIsBlack.setTrue();
			lastIsWhite.setFalse();

		} else if (element.isHibernatedFinished()) {

			if (lastIsBlack.is()) {
				failedNumbersRe.add(x);
				failedNumbersIm.add(y);
			}
			lastIsBlack.setFalse();
			lastIsWhite.setTrue();
		} else {
			lastIsBlack.setFalse();
			lastIsWhite.setFalse();
		}
	}

	// not correct;
	public static void setActiveMovedIfBlack(int x, int y, MandelbrotElement[][] mandelbrot) {
		MandelbrotElement element;
		if (checkDomain(x, y)) {
			element = mandelbrot[x][y];
			if (element.isHibernatedBlack() || element.isHibernatedBlack_Neighbour()) {
				element.setActiveFixed();
			}
		}
	}

	public static void setActiveToAddToCalculation(int x, int y, MandelbrotElement[][] mandelbrot) {
		MandelbrotElement element;
		if (checkDomain(x, y)) {
			element = mandelbrot[x][y];
			if (element != null && (element.isActiveAny() || element.isHibernatedFinished())) {
				element.setActiveRecalculate();
			}
		}
	}
}
