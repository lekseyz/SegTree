package segtree.testUtils;

import segtree.Aggregator;
import segtree.Updater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlainArrSeg<T, U> {
    private final int size;
    private final List<T> array;
    private final Aggregator<T> aggregator;
    private final Updater<T, U> updater;
    private final T identityT;
    private final U identityU;

    public PlainArrSeg(T[] data, Aggregator<T> aggregator, Updater<T, U> updater, T identityT, U identityU) {
        this.size = data.length;
        this.array = new ArrayList<>(Arrays.stream(data).toList());
        this.aggregator = aggregator;
        this.updater = updater;
        this.identityT = identityT;
        this.identityU = identityU;
    }

    public void update(int l, int r, U value) {
        for (int i = l; i <= r; i++) {
            array.set(i, updater.apply(array.get(i), value, 1));
        }
    }

    public T query(int l, int r) {
        T result = aggregator.aggregate(identityT, array.get(l));
        for (int i = l + 1; i <= r; i++) {
            result = aggregator.aggregate(result, array.get(i));
        }

        return result;
    }

}
