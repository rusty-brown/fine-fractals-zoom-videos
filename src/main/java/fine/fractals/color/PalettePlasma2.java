package fine.fractals.color;

import fine.fractals.color.utils.ColorTest;
import fine.fractals.color.utils.ColorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.List;

import static fine.fractals.context.PaletteImpl.Palette;
import static java.awt.Color.black;
import static java.awt.Color.white;

public abstract class PalettePlasma2 {

    private static final Logger log = LogManager.getLogger(PalettePlasma2.class);

    public static void init() {
        log.info("init");
        Palette.name("Plasma 2");
        ColorUtils.linearSpectrumToPalet(List.of(
                black,
                new Color(7, 1, 116),
                new Color(37, 1, 133),
                new Color(62, 0, 145),
                new Color(90, 0, 150),
                new Color(131, 1, 142),
                new Color(153, 8, 128),
                new Color(177, 34, 109),
                new Color(213, 72, 81),
                new Color(231, 101, 65),
                new Color(251, 160, 38),
                new Color(247, 211, 30),
                new Color(239, 250, 26),
                white
        ));
    }

    public static void main(String[] args) {
        PalettePlasma2.init();
        ColorTest.execute();
    }
}