package fine.fractals;

import fine.fractals.color.PalettePlasma;
import fine.fractals.fractal.FineMandelbrot_Side_Zoom;
import fine.fractals.fractal.Fractal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.context.ApplicationImpl.Application;
import static fine.fractals.context.ApplicationImpl.USER_HOME;

public class Main {

	/**
	 * The Fine Fractal to be calculated
	 */
	public static Fractal FRACTAL = new FineMandelbrot_Side_Zoom();
	/**
	 * Image resolution height & width
	 * 1080 1920 full HD
	 * 1920 1080 full HD high
	 * 2560 1440 quad HD
	 */
	public static final int RESOLUTION_WIDTH = 700;
	public static final int RESOLUTION_HEIGHT = 900;

	/**
	 * Calculation points per pixel
	 * m x m
	 * Keep it odd, so that the center point is in the center of a pixel
	 * values: 1 or 2
	 */
	public static final int RESOLUTION_MULTIPLIER = 2;

	/**
	 * Image in resolution Application.RESOLUTION_IMAGE_SAVE_FOR = 2000 will be saved to the location below
	 * <p>
	 * Create the folder in your home directory or change the path
	 */
	public static final String FILE_PATH = USER_HOME + "/Fractals/";

	private static final Logger log = LogManager.getLogger(Main.class);

	public static final int neighbours = 4;

	public static int COREs = Runtime.getRuntime().availableProcessors() - 1;

	static {
		log.info("init");
		PalettePlasma.init();
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
