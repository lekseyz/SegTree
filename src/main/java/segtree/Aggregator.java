package segtree;

public interface Aggregator<T> {
    T aggregate(T left, T right);
}