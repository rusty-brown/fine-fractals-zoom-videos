package fine.fractals.color;

import fine.fractals.color.common.PaletteImpl;
import fine.fractals.color.utils.ColorTest;
import fine.fractals.color.utils.ColorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fine.fractals.color.utils.ColorUtils.Function.circleUp;
import static java.awt.Color.black;
import static java.awt.Color.white;

public class PaletteBlackToWhite extends PaletteImpl {

    private static final Logger log = LogManager.getLogger(PaletteBlackToWhite.class);

    public PaletteBlackToWhite() {
        log.debug("constructor");
        ColorUtils.toPalette(spectrum, circleUp, black, white);
    }

    public static void main(String[] args) {
        Palette = new PaletteBlackToWhite();
        ColorTest.execute();
    }
}
