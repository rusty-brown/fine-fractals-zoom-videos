package fine.fractals.context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

public class PaletteImpl {

    private static final Logger log = LogManager.getLogger(PaletteImpl.class);
    private String name;

    public final ArrayList<Color> spectrum = new ArrayList<>();

    public static final PaletteImpl Palette;

    static {
        log.info("init");
        Palette = new PaletteImpl();
    }

    private PaletteImpl() {
    }

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public Color getSpectrumValue(int index) {
        return spectrum.get(index);
    }

    public int colorResolution() {
        return spectrum.size();
    }
}
