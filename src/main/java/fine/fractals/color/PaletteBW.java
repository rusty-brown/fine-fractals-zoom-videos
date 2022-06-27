package fine.fractals.color;

import fine.fractals.color.things.ColorTest;
import fine.fractals.color.things.Function;
import fine.fractals.color.things.Palette;

import java.util.ArrayList;

public class PaletteBW extends Palette {

    public PaletteBW() {
        super("PaletteBW3");

        fromTo(black, white, Function.circleUp);
    }

    public static void main(String[] args) {
        ColorTest.execute(new PaletteBW());
    }

}
