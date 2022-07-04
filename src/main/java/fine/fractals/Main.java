package fine.fractals;

import fine.fractals.color.PaletteBlueWhite;
import fine.fractals.data.ResolutionMultiplier;
import fine.fractals.fractal.Finebrot_Side;
import fine.fractals.fractal.Fractal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.context.ApplicationImpl.Application;
import static fine.fractals.context.ApplicationImpl.USER_HOME;
import static fine.fractals.data.ResolutionMultiplier.one_square;

public class Main {

	/**
	 * The Fine Fractal to be calculated
	 */
	public static final Fractal FRACTAL = new Finebrot_Side();
	/**
	 * Image resolution height & width
	 * 800 600
	 * 1080 1920 full HD high
	 * 1920 1080 full HD
	 * 2560 1440 quad HD
	 */
	public static final int RESOLUTION_WIDTH = 1920;
	public static final int RESOLUTION_HEIGHT = 1080;
	public static final boolean SAVE_IMAGES = true;

	/**
	 * Sets now many points will be used for calculation per each pixel
	 * value: 1
	 * - calculate only one element per each Mandelbrot pixel, [re,im] in the center of the pixel
	 * value: 2
	 * - calculates two more points per each pixel
	 */
	public static final ResolutionMultiplier RESOLUTION_MULTIPLIER = one_square;

	/**
	 * Image in resolution Application.RESOLUTION_IMAGE_SAVE_FOR = 2000 will be saved to the location below
	 * <p>
	 * Create the folder in your home directory or change the path
	 */
	public static final String FILE_PATH = USER_HOME + "/Fractals/";

	private static final Logger log = LogManager.getLogger(Main.class);

	/*
	 * How many pixels round specific element will be investigated for optimization.
	 * If there is nothing interesting going on around specific pixel, the pixel will be ignored.
	 */
	public static final int neighbours = 4;

	public static int COREs = Runtime.getRuntime().availableProcessors() - 1;

	static {
		log.info("init");
		PaletteBlueWhite.init();
	}

	private Main() {
	}

	public static void main(String[] args) {

		if (COREs < 1) {
			COREs = 1;
		}

		log.info("cores: " + COREs);

		log.info("Application start");
		Application.execute();

		log.info("Application finished");
	}
}
