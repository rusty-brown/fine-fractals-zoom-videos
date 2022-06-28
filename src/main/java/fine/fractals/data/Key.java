package fine.fractals.data;

import fine.fractals.data.objects.Data;

import java.util.LinkedList;

public class Key {

    public static final String A_ITERATION_MAX = "A_ITERATION_MAX";
    public static final String A_ITERATION_MIN = "A_ITERATION_MIN";
    public static final String T_ROTATION = "T_ROTATION";
    public static LinkedList<String> keys = new LinkedList<>();

    static {
        /* Order for painting data on screen */
        keys.add(A_ITERATION_MAX);
        keys.add(A_ITERATION_MIN);
        keys.add(T_ROTATION);
        Data.set("", "");
    }

}
