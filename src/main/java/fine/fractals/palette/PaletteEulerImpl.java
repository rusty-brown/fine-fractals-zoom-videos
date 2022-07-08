package fine.fractals.palette;

import java.awt.*;
import java.util.ArrayList;

public class PaletteEulerImpl {

    public final ArrayList<Color> spectrumRed = new ArrayList<>();
    public final ArrayList<Color> spectrumGreen = new ArrayList<>();
    public final ArrayList<Color> spectrumBlue = new ArrayList<>();

    public static final PaletteEulerImpl PaletteEuler3 = new PaletteEulerImpl();

    public Color getSpectrumValueRed(int index) {
        return spectrumRed.get(index);
    }

    public Color getSpectrumValueGreen(int index) {
        return spectrumGreen.get(index);
    }

    public Color getSpectrumValueBlue(int index) {
        return spectrumBlue.get(index);
    }

    /*
     * RGB spectra should be all the same size
     */
    public int colorResolution() {
        return spectrumBlue.size();
    }
}
