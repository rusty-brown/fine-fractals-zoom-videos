package fine.fractals.color;

import fine.fractals.color.utils.ColorTest;
import fine.fractals.color.utils.ColorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static fine.fractals.context.PaletteImpl.Palette;
import static java.awt.Color.black;
import static java.awt.Color.white;

public abstract class PaletteBWBW {

    private static final Logger log = LogManager.getLogger(PaletteBWBW.class);

    public static void init() {
        log.info("init");
        Palette.name("Black White Black White");
        ColorUtils.toPaletteLinearSpectrum(List.of(
                black, white,
                white, black,
                black, white
        ));
    }

    public static void main(String[] args) {
        PaletteBWBW.init();
        ColorTest.execute();
    }
}
