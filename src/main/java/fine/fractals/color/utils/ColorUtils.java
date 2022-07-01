package fine.fractals.color.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.List;

import static fine.fractals.context.PaletteImpl.Palette;

public abstract class ColorUtils {

    private static final Logger log = LogManager.getLogger(ColorUtils.class);

    public enum Function {linear1, linear3, linear7, quadratic, q3, q4, q5, exp, exp2, circleDown, circleUp}

    public static void toPaletteLinearSpectrum(List<Color> colors) {
        double step = 1;
        Color colorA = colors.get(0);
        Color colorB = colors.get(1);
        toPaletteLinear(colorA, colorB, step);
        for (int i = 1; i < colors.size(); i++) {
            colorA = colorB;
            colorB = colors.get(i);
            toPaletteLinear(colorA, colorB, step);
        }
    }

    public static void toPaletteLinear(Color from, Color to, double step) {
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

    public static void toPalette(Color from, Color to, Function function) {
        final int rFrom = from.getRed();
        final int gFrom = from.getGreen();
        final int bFrom = from.getBlue();

        final int rDif = to.getRed() - rFrom;
        final int gDif = to.getGreen() - gFrom;
        final int bDif = to.getBlue() - bFrom;

        final double maxDif = a(max(rDif, gDif, bDif));

        final double rStep = ((double) rDif / maxDif);
        final double gStep = ((double) gDif / maxDif);
        final double bStep = ((double) bDif / maxDif);

        double d = 0;
        double v = 0;
        double value = 0;
        int i = 0;

        int rr = 0;
        int gg = 0;
        int bb = 0;

        boolean stop = false;

        final int tr = to.getRed();
        final int tg = to.getGreen();
        final int tb = to.getBlue();

        final int rgb255 = 255;

        try {
            for (i = 0; i < a(maxDif); i++) {
                d = i / maxDif;
                v = function(d, function);
                value = v * maxDif;
                // dots((int) value);

                rr = (int) (rFrom + (value * rStep));
                gg = (int) (gFrom + (value * gStep));
                bb = (int) (bFrom + (value * bStep));

                if (rr > rgb255) {
                    rr = rgb255;
                    stop = true;
                }
                if (gg > rgb255) {
                    gg = rgb255;
                    stop = true;
                }
                if (bb > rgb255) {
                    bb = rgb255;
                    stop = true;
                }

                if (rr < 0) {
                    rr = 0;
                    stop = true;
                }
                if (gg < 0) {
                    gg = 0;
                    stop = true;
                }
                if (bb < 0) {
                    bb = 0;
                    stop = true;
                }

                final boolean rStop = rDif > 0 ? tr < rr : tr > rr;
                final boolean gStop = gDif > 0 ? tg < gg : tg > gg;
                final boolean bStop = bDif > 0 ? tb < bb : tb > bb;

                if (rStop) {
                    rr = tr;
                    stop = true;
                }
                if (gStop) {
                    gg = tg;
                    stop = true;
                }
                if (bStop) {
                    bb = tb;
                    stop = true;
                }

                /*
                 * Add colors to Palette
                 */
                Palette.spectrum.add(new Color(rr, gg, bb));

                if (stop) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("i: " + i);
            log.error("d: " + d);
            log.error("v: " + v);
            log.error("val: " + value);
            log.error("rr: " + rr);
            log.error("gg: " + gg);
            log.error("bb: " + bb);
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


    /**
     * 0 <= d <= 1
     */
    private static double function(double d, Function function) {
        return switch (function) {
            case linear1 -> d;
            case linear3 -> d * 3;
            case linear7 -> d * 7;
            case quadratic -> d * d;
            case q3 -> d * d * d;
            case q4 -> d * d * d * d;
            case q5 -> d * d * d * d * d;
            case exp -> Math.exp(d) - 1;
            case exp2 -> Math.exp(d * d) - 1;
            case circleDown -> Math.sqrt(1 - (d * d));
            case circleUp -> 1 - Math.sqrt(1 - (d * d));
        };
    }
}
