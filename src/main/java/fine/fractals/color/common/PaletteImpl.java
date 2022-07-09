package fine.fractals.color.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

public abstract class PaletteImpl {

    private static final Logger log = LogManager.getLogger(PaletteImpl.class);

    public static PaletteImpl Palette;

    public final ArrayList<Color> spectrum = new ArrayList<>();

    protected PaletteImpl() {
        log.debug("constructor");
    }

    public Color getSpectrumValue(int index) {
        return spectrum.get(index);
    }

    public int colorResolution() {
        return spectrum.size();
    }
}
