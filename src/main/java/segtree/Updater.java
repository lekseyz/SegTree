package segtree;

public interface Updater<T, U> {
    T apply(T value, U update, int length);
    U compose(U oldUpdate, U newUpdate);
}
