package fine.fractals.color;

import fine.fractals.color.things.ColorTest;
import fine.fractals.color.things.Palette;

import java.awt.*;
import java.util.LinkedList;

public class PaletteViridis extends Palette {

    @Deprecated
    public PaletteViridis() {
        super("PaletteViridis");

        final LinkedList<Color> colors = new LinkedList<>();
        colors.add(new Color(254, 229, 19));
        colors.add(new Color(209, 225, 21));
        colors.add(new Color(160, 218, 36));
        colors.add(new Color(99, 202, 71));
        colors.add(new Color(36, 159, 110));
        colors.add(new Color(31, 124, 124));
        colors.add(new Color(34, 97, 122));
        colors.add(new Color(45, 66, 121));
        colors.add(new Color(54, 31, 106));
        colors.add(new Color(48, 3, 67));

        // TODO
        // super.spectrumColorsToLinear(colors);
    }

    public static void main(String[] args) {
        ColorTest.execute(new PaletteViridis());
    }
}