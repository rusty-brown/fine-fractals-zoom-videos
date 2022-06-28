package fine.fractals.color;

import fine.fractals.color.things.ColorTest;
import fine.fractals.color.things.Palette;

import java.awt.*;
import java.util.ArrayList;

public class PalettePlasma2 extends Palette {

    public PalettePlasma2() {
        super("PalettePlasma");

        final ArrayList<Color> colors = new ArrayList<>();
        colors.add(black);
        colors.add(new Color(7, 1, 116));
        colors.add(new Color(37, 1, 133));
        colors.add(new Color(62, 0, 145));
        colors.add(new Color(90, 0, 150));
        colors.add(new Color(131, 1, 142));
        colors.add(new Color(153, 8, 128));
        colors.add(new Color(177, 34, 109));
        colors.add(new Color(213, 72, 81));
        colors.add(new Color(231, 101, 65));
        colors.add(new Color(251, 160, 38));
        colors.add(new Color(247, 211, 30));
        colors.add(new Color(239, 250, 26));
        colors.add(white);

        super.spectrumColorsToLinear(colors);
    }

    public static void main(String[] args) {
        ColorTest.execute(new PalettePlasma2());
    }
}