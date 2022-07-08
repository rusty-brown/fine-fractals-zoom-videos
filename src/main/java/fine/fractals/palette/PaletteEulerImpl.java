package fine.fractals.palette;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

import static java.awt.Color.BLACK;

public class PaletteEulerImpl {

    private static final Logger log = LogManager.getLogger(PaletteEulerImpl.class);

    public final ArrayList<Color> spectrumRed = new ArrayList<>();
    public final ArrayList<Color> spectrumGreen = new ArrayList<>();
    public final ArrayList<Color> spectrumBlue = new ArrayList<>();

    public static final PaletteEulerImpl PaletteEuler3;

    static {
        log.info("init");
        PaletteEuler3 = new PaletteEulerImpl();
    }

    PaletteEulerImpl() {
        super();
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
