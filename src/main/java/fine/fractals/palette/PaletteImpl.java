package fine.fractals.palette;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

public class PaletteImpl {

    private static final Logger log = LogManager.getLogger(PaletteImpl.class);
    public final ArrayList<Color> spectrum = new ArrayList<>();

    public static final PaletteImpl Palette;

    static {
        log.info("init");
        Palette = new PaletteImpl();
    }

    private PaletteImpl() {
    }

    public Color getSpectrumValue(int index) {
        return spectrum.get(index);
    }

    public int colorResolution() {
        return spectrum.size();
    }
}
