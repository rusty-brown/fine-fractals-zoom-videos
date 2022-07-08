package fine.fractals.color;

import fine.fractals.color.utils.ColorTest;
import fine.fractals.color.utils.ColorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

import static fine.fractals.color.utils.ColorUtils.Function.circleUp;
import static fine.fractals.palette.PaletteImpl.Palette;

public abstract class PalettePurpleToWhite {

    private static final Logger log = LogManager.getLogger(PalettePurpleToWhite.class);

    public static void init() {
        log.info("init");
        ColorUtils.toPalette(
                Palette.spectrum,
                circleUp,
                new Color(28, 7, 44),
                new Color(255, 255, 255)
        );
    }

    public static void main(String[] args) {
        PalettePurpleToWhite.init();
        ColorTest.execute();
    }
}
