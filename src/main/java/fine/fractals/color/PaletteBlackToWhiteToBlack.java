package fine.fractals.color;

import fine.fractals.color.common.PaletteImpl;
import fine.fractals.color.utils.ColorTest;
import fine.fractals.color.utils.ColorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.awt.Color.black;
import static java.awt.Color.white;

public class PaletteBlackToWhiteToBlack extends PaletteImpl {

    private static final Logger log = LogManager.getLogger(PaletteBlackToWhiteToBlack.class);

    public PaletteBlackToWhiteToBlack() {
        log.debug("constructor");
        ColorUtils.toPaletteLinearSpectrum(spectrum,
                black, white,
                white, black,
                black, white
        );
    }

    public static void main(String[] args) {
        Palette = new PaletteBlackToWhiteToBlack();
        ColorTest.execute();
    }
}
