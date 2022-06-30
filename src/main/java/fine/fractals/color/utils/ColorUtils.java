package fine.fractals.color.utils;

import java.awt.*;
import java.util.List;

import static fine.fractals.context.PaletteImpl.Palette;

public abstract class ColorUtils {

    public static void linearSpectrumToPalet(List<Color> colors) {
        double step = 1;
        Color colorA = colors.get(0);
        Color colorB = colors.get(1);
        fromToLinear(colorA, colorB, step);
        for (int i = 1; i < colors.size(); i++) {
            colorA = colorB;
            colorB = colors.get(i);
            fromToLinear(colorA, colorB, step);
        }
    }

    private static void fromToLinear(Color from, Color to, double step) {
        final int rf = from.getRed();
        final int gf = from.getGreen();
        final int bf = from.getBlue();
        final int r = to.getRed() - rf;
        final int g = to.getGreen() - gf;
        final int b = to.getBlue() - bf;

        final int m = max(r, g, b);

        final double rr = ((double) r / (double) m);
        final double gg = ((double) g / (double) m);
        final double bb = ((double) b / (double) m);

        if (m < 0) {
            step *= (-1);
        }
        for (double i = 0; a(i) < a(m); i = i + step) {

            /*
             * Add colors to Palette
             */
            Palette.spectrum.add(new Color(
                    (int) (rf + (i * rr)),
                    (int) (gf + (i * gg)),
                    (int) (bf + (i * bb))
            ));
        }
    }

    private static Integer max(int r, int g, int b) {
        if (a(r) >= a(g) && a(r) >= a(b)) {
            /* red has the biggest difference */
            return r;
        } else if (a(g) >= a(r) && a(g) >= a(b)) {
            /* green has the biggest difference */
            return g;
        } else if (a(b) >= a(r) && a(b) >= a(g)) {
            /* red has the biggest difference */
            return b;
        }
        throw new RuntimeException("Palette.fromToLinear() Unknown Max; " + r + " | " + g + " | " + b);
    }

    private static double a(double v) {
        return Math.abs(v);
    }


}
