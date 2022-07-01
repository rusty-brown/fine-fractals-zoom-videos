package fine.fractals.machine;

import fine.fractals.Main;
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

	public static boolean isVeryDeepBlack(int tt, int xx, MandelbrotElement[][] elements) {
		if (elements[tt][xx].getValue() > 0) {
			return false;
		}
		Integer value;
		/* r is a radius of a circle to verify non-zero values around */
		final int r = 5;
		for (int t = -r; t < r; t++) {
			for (int x = -r; x < r; x++) {
				if ((t * t) + (x * x) < (r * r)) {
					/* Find value to be carried by HH.calculation */
					int t2 = tt + t;
					int x2 = xx + x;
					value = valueAt(t2, x2, elements);
					if (value == null || value > 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean someNeighboursFinishedInside(int tt, int xx, MandelbrotElement[][] elements) {
		/* r is a radius of a circle to verify finished inside (red) elements*/
		MandelbrotElement el;
		final int r = Main.neighbours;
		for (int t = -r; t < r; t++) {
			for (int x = -r; x < r; x++) {
				if ((t * t) + (x * x) <= (r * r)) {
					int t2 = tt + t;
					int x2 = xx + x;
					if (checkDomain(t2, x2)) {
						el = elements[t2][x2];
						if (el != null && el.isHibernatedFinishedInside()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean checkDomain(int t, int x) {
		return t >= 0 && t < RESOLUTION_WIDTH
				&& x >= 0 && x < RESOLUTION_HEIGHT;
	}

	private static Integer valueAt(int t, int x, MandelbrotElement[][] mandelbrot) {
		if (checkDomain(t, x)) {
			MandelbrotElement element = mandelbrot[t][x];
			return element.getValue();
		}
		return null;
	}

	public static void setAsDeepBlack(int t, int x, MandelbrotElement[][] elements) {
		if (checkDomain(t, x)) {
			/* Most probably Element of Mandelbrot set, don're calculate */
			elements[t][x].setHibernatedBlackNeighbour();
		}
	}

	public static void testOptimizationBreakElement(int t, int x, MandelbrotElement element, ArrayList<Integer> failedNumbersT, ArrayList<Integer> failedNumbersX, Bool lastIsWhite, Bool lastIsBlack) {
		if (element.isHibernatedBlack_Neighbour() || element.isHibernatedBlack()) {
			if (lastIsWhite.is()) {
				failedNumbersT.add(t);
				failedNumbersX.add(x);
			}
			lastIsBlack.setTrue();
			lastIsWhite.setFalse();

		} else if (element.isHibernatedFinished()) {

			if (lastIsBlack.is()) {
				failedNumbersT.add(t);
				failedNumbersX.add(x);
			}
			lastIsBlack.setFalse();
			lastIsWhite.setTrue();
		} else {
			lastIsBlack.setFalse();
			lastIsWhite.setFalse();
		}
	}

	// not correct;
	public static void setActiveMovedIfBlack(int t, int x, MandelbrotElement[][] mandelbrot) {
		MandelbrotElement element;
		if (checkDomain(t, x)) {
			element = mandelbrot[t][x];
			if (element.isHibernatedBlack() || element.isHibernatedBlack_Neighbour()) {
				element.setActiveFixed();
			}
		}
	}

	public static void setActiveToAddToCalculation(int t, int x, MandelbrotElement[][] mandelbrot) {
		MandelbrotElement element;
		if (checkDomain(t, x)) {
			element = mandelbrot[t][x];
			if (element != null && (element.isActiveAny() || element.isHibernatedFinished())) {
				element.setActiveRecalculate();
			}
		}
	}
}
