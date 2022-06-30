package fine.fractals.perfect.coloring;

public class ScreenPixel {

    final private int pixelValue;
    final private int px;
    final private int py;

    public ScreenPixel(int pixelValue, int px, int py) {
        this.pixelValue = pixelValue;
        this.px = px;
        this.py = py;
    }

    public int pixelValue() {
        return pixelValue;
    }

    public int px() {
        return px;
    }

    public int py() {
        return py;
    }
}
