package fine.fractals.color.euler;

import fine.fractals.color.common.PaletteEulerImpl;
import fine.fractals.color.utils.ColorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.color.utils.ColorUtils.Function.circleUp;
import static java.awt.Color.BLUE;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static java.awt.Color.black;

public class Palette3RGB extends PaletteEulerImpl {

    private static final Logger log = LogManager.getLogger(Palette3RGB.class);

    public Palette3RGB() {
        log.debug("constructor");
        ColorUtils.toPalette(spectrumRed, circleUp, black, RED);
        ColorUtils.toPalette(spectrumGreen, circleUp, black, GREEN);
        ColorUtils.toPalette(spectrumBlue, circleUp, black, BLUE);
    }
}
