package fine.fractals.data.misc;

/* Boolean to be passed as reference parameter */
public class Bool {

    private boolean value;

    public Bool() {
        this.value = false;
    }

    public boolean is() {
        return this.value;
    }

    public void setTrue() {
        this.value = true;
    }

    public void setFalse() {
        this.value = false;
    }
}
