package fine.fractals.color;

import fine.fractals.color.things.ColorTest;
import fine.fractals.color.things.Function;
import fine.fractals.color.things.Palette;

import java.awt.*;
import java.util.ArrayList;

public class Palette3_RGB extends Palette {

    public Palette3_RGB() {
        super("Palette3_RGB");
        /* Color for Highest values is first */

        spectrumR = new ArrayList<>();
        spectrumG = new ArrayList<>();
        spectrumB = new ArrayList<>();

        fromTo(spectrumR, black, Color.red, Function.circleUp);
        fromTo(spectrumR, Color.red, white, Function.linear7);

        fromTo(spectrumG, black, Color.green, Function.circleUp);
        fromTo(spectrumG, Color.green, white, Function.linear7);

        fromTo(spectrumB, black, Color.blue, Function.circleUp);
        fromTo(spectrumB, Color.blue, white, Function.linear7);
    }

    public static void main(String[] args) {
        ColorTest.execute(new Palette3_RGB());
    }
}
