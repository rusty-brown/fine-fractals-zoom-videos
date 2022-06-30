package fine.fractals;

import fine.fractals.color.PalettePlasma2;
import fine.fractals.context.ApplicationImpl;
import fine.fractals.fractal.FineMandelbrot_Side_Zoom;
import fine.fractals.fractal.Fractal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.context.ApplicationImpl.Application;

public class Main {

	/**
	 * The Fine Fractal to be calculated
	 */
	public static Fractal FRACTAL = new FineMandelbrot_Side_Zoom();

	/**
	 * Image resolution height & width
	 */
	private static final int rh = 800;
	private static final int rw = 800;

	/**
	 * Calculation points per pixel
	 * m x m
	 * Keep it odd, so that the center point is in the center of a pixel
	 * values: 1 or 2
	 */
	private static final int m = 1;

	/**
	 * Image in resolution Application.RESOLUTION_IMAGE_SAVE_FOR = 2000 will be saved to the location below
	 * <p>
	 * Create the folder in your home directory or change the path
	 */
	public static final String FILE_PATH = ApplicationImpl.USER_HOME + "/Fractals/";

	private static final Logger log = LogManager.getLogger(Main.class);

	public static final int neighbours = 4;
	public static final int RESOLUTION_MULTIPLIER = m;
	public static final int RESOLUTION_WIDTH = rw;
	public static final int RESOLUTION_HEIGHT = rh;

	public static int COREs = Runtime.getRuntime().availableProcessors() - 1;

	static {
		log.info("init");
		PalettePlasma2.init();
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
