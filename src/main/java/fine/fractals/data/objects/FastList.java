package fine.fractals.data.objects;

import fine.fractals.fractal.Fractal;

import java.util.*;

public class FastList implements Iterable<Double>, Iterator<Double> {

    /* ArrayList is simply faster */
    private ArrayList<Double> list = new ArrayList<>();

    private int count = 0;

    public FastList() {
    }

    public int size() {
        return list.size();
    }


    public void add(Double d) {
        list.add(d);
    }

    public Double get(Integer i) {
        return list.get(i);
    }

    @Override
    public Iterator<Double> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return count < list.size();
    }

    @Override
    public Double next() {
        if (count == list.size()) {
            throw new NoSuchElementException();
        }
        count++;
        return list.get(count - 1);
    }

    public ArrayList<Double> list() {
        return list;
    }
}
