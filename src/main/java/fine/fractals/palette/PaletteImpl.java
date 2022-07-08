package fine.fractals.palette;

import java.awt.*;
import java.util.ArrayList;

public class PaletteImpl {

    public final ArrayList<Color> spectrum = new ArrayList<>();

    public static final PaletteImpl Palette = new PaletteImpl();

    private PaletteImpl() {
    }

    public Color getSpectrumValue(int index) {
        return spectrum.get(index);
    }

    public int colorResolution() {
        return spectrum.size();
    }
}
