package fine.fractals.color.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

public abstract class PaletteEulerImpl {

    private static final Logger log = LogManager.getLogger(PaletteEulerImpl.class);

    public static PaletteEulerImpl PaletteEuler3;
    public ArrayList<Color> spectrumRed = new ArrayList<>();
    public ArrayList<Color> spectrumGreen = new ArrayList<>();
    public ArrayList<Color> spectrumBlue = new ArrayList<>();

    public PaletteEulerImpl() {
        log.debug("constructor");
    }

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
