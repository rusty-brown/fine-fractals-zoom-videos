package fine.fractals.gpgpu;

public abstract class GPU {

    static final int GPU_COUNT = 1;

    public static GPUHigh rebuild() {
        return new GPUHigh();
    }
}
