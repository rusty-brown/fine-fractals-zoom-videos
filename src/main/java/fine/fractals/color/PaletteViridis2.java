package fine.fractals.color;

import fine.fractals.color.things.ColorTest;
import fine.fractals.color.things.Palette;

import java.awt.*;
import java.util.ArrayList;

public class PaletteViridis2 extends Palette {

    @Deprecated
    public PaletteViridis2() {
        super("PaletteViridis");

        final ArrayList<Color> colors = new ArrayList<>();
        colors.add(black);
        colors.add(new Color(48, 3, 67));
        colors.add(new Color(54, 31, 106));
        colors.add(new Color(45, 66, 121));
        colors.add(new Color(34, 97, 122));
        colors.add(new Color(31, 124, 124));
        colors.add(new Color(36, 159, 110));
        colors.add(new Color(99, 202, 71));
        colors.add(new Color(160, 218, 36));
        colors.add(new Color(209, 225, 21));
        colors.add(new Color(254, 229, 19));
        colors.add(white);

        super.spectrumColorsToLinear(colors);
    }

    public static void main(String[] args) {
        ColorTest.execute(new PaletteViridis2());
    }
}