package fine.fractals;

import fine.fractals.color.PalettePurpleWhite;
import fine.fractals.data.ResolutionMultiplier;
import fine.fractals.data.annotation.EditMe;
import fine.fractals.fractal.Finebrot_Top;
import fine.fractals.fractal.abst.Fractal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.context.ApplicationImpl.Application;
import static fine.fractals.context.ApplicationImpl.USER_HOME;
import static fine.fractals.data.ResolutionMultiplier.square_alter;

public class Main {

	/**
	 * The Fine Fractal to be calculated
	 */
	@EditMe
	public static Fractal FRACTAL = new Finebrot_Top();
	/**
	 * Image resolution height & width
	 * 800 600
	 * 1080 1920 full HD high
	 * 1920 1080 full HD
	 * 2560 1440 quad HD
	 */
	@EditMe
	public static int RESOLUTION_WIDTH = 1920;
	public static int RESOLUTION_HEIGHT = 1080;
	@EditMe
	public static boolean SAVE_IMAGES = false;

	/**
	 * Sets now many points will be used for calculation per each pixel
	 * value: 1
	 * - calculate only one element per each Mandelbrot pixel, [re,im] in the center of the pixel
	 * value: 2
	 * - calculates two more points per each pixel
	 */
	public static ResolutionMultiplier RESOLUTION_MULTIPLIER = square_alter;

	/**
	 * Image in resolution Application.RESOLUTION_IMAGE_SAVE_FOR = 2000 will be saved to the location below
	 * <p>
	 * Create the folder in your home directory or change the path
	 */
	@EditMe
	public static final String FILE_PATH = USER_HOME + "/Fractals/";
	@EditMe
	public static final String DEBUG_PATH = USER_HOME + "/Fractals-debug/";

	private static final Logger log = LogManager.getLogger(Main.class);

	/*
	 * How many pixels round specific element will be investigated for optimization.
	 * If there is nothing interesting going on around specific pixel, the pixel will be ignored.
	 */
	public static final int neighbours = 4;

	public static int COREs = Runtime.getRuntime().availableProcessors() - 1;

	static {
		log.info("init");
		PalettePurpleWhite.init();

		if (COREs < 1) {
			COREs = 1;
		}
		log.info("cores: " + COREs);
	}

	private Main() {
	}

	public static void main(String[] args) {

		log.info("Application start");
		Application.execute();

		log.info("Application finished");
	}
}
