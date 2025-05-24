package segtree;

public class SegmentTree<T, U> {
    private final int size;
    private final T[] tree;
    private final U[] lazy;
    private final Aggregator<T> aggregator;
    private final Updater<T, U> updater;
    private final T identityT;
    private final U identityU;

    public SegmentTree(T[] data, Aggregator<T> aggregator, Updater<T, U> updater, T identityT, U identityU) {
        this.size = data.length;
        this.tree = (T[]) new Object[4 * size];
        this.lazy = (U[]) new Object[4 * size];
        this.aggregator = aggregator;
        this.updater = updater;
        this.identityT = identityT;
        this.identityU = identityU;
        build(1, 0, size - 1, data);
    }

    private void build(int node, int left, int right, T[] data) {
        lazy[node] = identityU;
        if (left == right) {
            tree[node] = data[left];
        } else {
            int mid = (left + right) / 2;
            build(2 * node, left, mid, data);
            build(2 * node + 1, mid + 1, right, data);
            tree[node] = aggregator.aggregate(tree[2 * node], tree[2 * node + 1]);
        }
    }

    public void update(int l, int r, U value) {
        update(1, 0, size - 1, l, r, value);
    }

    private void push(int node, int left, int right) {
        if (!lazy[node].equals(identityU)) {
            tree[node] = updater.apply(tree[node], lazy[node], right - left + 1);
            if (left != right) {
                lazy[2 * node] = updater.compose(lazy[2 * node], lazy[node]);
                lazy[2 * node + 1] = updater.compose(lazy[2 * node + 1], lazy[node]);
            }
            lazy[node] = identityU;
        }
    }

    private void update(int node, int left, int right, int l, int r, U value) {
        push(node, left, right);
        if (r < left || right < l) return;
        if (l <= left && right <= r) {
            lazy[node] = updater.compose(lazy[node], value);
            push(node, left, right);
            return;
        }
        int mid = (left + right) / 2;
        update(2 * node, left, mid, l, r, value);
        update(2 * node + 1, mid + 1, right, l, r, value);
        tree[node] = aggregator.aggregate(tree[2 * node], tree[2 * node + 1]);
    }

    public T query(int l, int r) {
        return query(1, 0, size - 1, l, r);
    }

    private T query(int node, int left, int right, int l, int r) {
        push(node, left, right);
        if (r < left || right < l) return identityT;
        if (l <= left && right <= r) return tree[node];
        int mid = (left + right) / 2;
        T leftResult = query(2 * node, left, mid, l, r);
        T rightResult = query(2 * node + 1, mid + 1, right, l, r);
        return aggregator.aggregate(leftResult, rightResult);
    }
}

