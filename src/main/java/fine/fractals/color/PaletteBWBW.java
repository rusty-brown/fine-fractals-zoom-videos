package fine.fractals.color;

import fine.fractals.color.things.ColorTest;
import fine.fractals.color.things.Function;
import fine.fractals.color.things.Palette;

public class PaletteBWBW extends Palette {

    public PaletteBWBW() {
        super("PaletteBWBW");
        fromTo(black, white, Function.linear1);
        fromTo(white, black, Function.linear1);
        fromTo(black, white, Function.linear1);
    }

    public static void main(String[] args) {
        ColorTest.execute(new PaletteBWBW());
    }
}
