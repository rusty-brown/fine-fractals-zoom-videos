package fine.fractals.color;

import fine.fractals.color.common.PaletteImpl;
import fine.fractals.color.utils.ColorTest;
import fine.fractals.color.utils.ColorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

import static fine.fractals.color.utils.ColorUtils.Function.circleUp;

public class PaletteBlueToWhite extends PaletteImpl {

    private static final Logger log = LogManager.getLogger(PaletteBlueToWhite.class);

    public PaletteBlueToWhite() {
        log.debug("constructor");
        ColorUtils.toPalette(spectrum, circleUp,
                new Color(4, 23, 53),
                new Color(255, 255, 255)
        );
    }

    public static void main(String[] args) {
        Palette = new PaletteBlueToWhite();
        ColorTest.execute();
    }
}
