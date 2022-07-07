package fine.fractals.palette;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

public class PaletteEulerImpl {

    private static final Logger log = LogManager.getLogger(PaletteEulerImpl.class);

    public final ArrayList<Color> spectrumR = new ArrayList<>();
    public final ArrayList<Color> spectrumG = new ArrayList<>();
    public final ArrayList<Color> spectrumB = new ArrayList<>();

    public static final PaletteEulerImpl PaletteEuler3;

    static {
        log.info("init");
        PaletteEuler3 = new PaletteEulerImpl();
    }

    PaletteEulerImpl() {
        super();
    }

    public Color getSpectrumValueR(int index) {
        return spectrumR.get(index);
    }

    public Color getSpectrumValueG(int index) {
        return spectrumG.get(index);
    }

    public Color getSpectrumValueB(int index) {
        return spectrumB.get(index);
    }

    /*
     * RGB spectra should be all the same size
     */
    public int colorResolution() {
        return spectrumB.size();
    }
}
