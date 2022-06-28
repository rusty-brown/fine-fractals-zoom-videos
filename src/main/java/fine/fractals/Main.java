package fine.fractals;

import fine.fractals.color.PalettePlasma;
import fine.fractals.color.things.Palette;
import fine.fractals.fractal.FineMandelbrot_Side_Zoom;
import fine.fractals.fractal.Fractal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	 * TODO 2 for zoom is best
	 * 101
	 */
	private static final int m = 1;

	/**
	 * Image in resolution Application.RESOLUTION_IMAGE_SAVE_FOR = 2000 will be saved to the location below
	 *
	 * Create the folder in your home directory or change the path
	 */
	public static final String FILE_PATH = Application.USER_HOME + "/Fractals/";


	public static Palette colorPalette = new PalettePlasma();

	public static final int neighbours = 4;
	public static final int RESOLUTION_MULTIPLIER = m;
	public static final int RESOLUTION_IMAGE_WIDTH = rw;
	public static final int RESOLUTION_IMAGE_HIGHT = rh;

	public static int COREs = Runtime.getRuntime().availableProcessors() - 1;

	private static final Logger log = LogManager.getLogger(Main.class);

	private Main() {
	}

	public static void main(String[] args) {

		if (COREs < 1) {
			COREs = 1;
		}

		log.info("cores: " + COREs);

		if (RESOLUTION_IMAGE_WIDTH < 1000) {
			Application.RESOLUTION_DOMAIN_WIDTH = RESOLUTION_IMAGE_WIDTH;
		}
		if (RESOLUTION_IMAGE_HIGHT < 1000) {
			Application.RESOLUTION_DOMAIN_HEIGHT = RESOLUTION_IMAGE_HIGHT;
		}

		log.info(Application.RESOLUTION_DOMAIN_WIDTH + " <-> " + RESOLUTION_IMAGE_WIDTH);
		log.info(Application.RESOLUTION_DOMAIN_HEIGHT + " <-> " + RESOLUTION_IMAGE_HIGHT);


		new Application().execute();

		log.info("Application END");
	}

}
