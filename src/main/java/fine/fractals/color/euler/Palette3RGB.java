package fine.fractals.color.euler;

import fine.fractals.color.utils.ColorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.color.utils.ColorUtils.Function.circleUp;
import static fine.fractals.palette.PaletteEulerImpl.PaletteEuler3;
import static java.awt.Color.*;

public abstract class Palette3RGB {

    private static final Logger log = LogManager.getLogger(Palette3RGB.class);

    public static void init() {
        log.info("init");

        ColorUtils.toPalette(PaletteEuler3.spectrumRed,
                circleUp,
                black, RED
        );
        ColorUtils.toPalette(PaletteEuler3.spectrumGreen,
                circleUp,
                black, GREEN
        );
        ColorUtils.toPalette(PaletteEuler3.spectrumBlue,
                circleUp,
                black, BLUE
        );
    }
}
