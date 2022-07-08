package fine.fractals.color;

import fine.fractals.color.common.PaletteImpl;
import fine.fractals.color.utils.ColorTest;
import fine.fractals.color.utils.ColorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class PaletteViridis extends PaletteImpl {

    private static final Logger log = LogManager.getLogger(PaletteViridis.class);

    public PaletteViridis() {
        log.debug("constructor");
        ColorUtils.toPaletteLinearSpectrum(spectrum,
                new Color(48, 3, 67),
                new Color(54, 31, 106),
                new Color(45, 66, 121),
                new Color(34, 97, 122),
                new Color(31, 124, 124),
                new Color(36, 159, 110),
                new Color(99, 202, 71),
                new Color(160, 218, 36),
                new Color(209, 225, 21),
                new Color(254, 229, 19)
        );
    }

    public static void main(String[] args) {
        Palette = new PaletteViridis();
        ColorTest.execute();
    }
}