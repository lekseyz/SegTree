package segtree;

import org.junit.jupiter.api.Test;
import java.util.Random;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SegmentTree test
 * Апдейтер - присвоение
 * Агрегатор - минимум
 */
public class MinPlusAssignTest {

    /// Реализация Aggregator для нахождения минимума на отрезке
    final private Aggregator<Long> MinAgg = Long::min;

    /// Реализация Updater для присвоения числа всем эелементам на отрезке
    final private Updater<Long, Long> AsgUpd = new Updater<>() {
        @Override
        public Long apply(Long value, Long update, int length) {
            return update;
        }

        @Override
        public Long compose(Long oldUpdate, Long newUpdate) {
            return newUpdate;
        }
    };

    @Test
    void testPointRangeAssigns() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(rd::nextLong).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);

        for (int i = 0; i < 100; i++) {
            int idx = rd.nextInt(1000);
            long delta = rd.nextLong() % 100;
            st.update(idx, idx, delta);
            plainSt.update(idx, idx, delta);
            assertEquals(plainSt.query(idx, idx), st.query(idx, idx));
        }

        for (int i = 0; i < 50; i++) {
            int idx = rd.nextInt(1000);
            assertEquals(plainSt.query(idx, idx), st.query(idx, idx));
        }
    }

    @Test
    void testFullRangeAssigns() {
        Long[] arr = new Long[1000];
        for (int i = 0; i < arr.length; i++) arr[i] = (long) i;

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);

        // Multiple full-range assignments
        st.update(0, arr.length - 1, 7L);
        plainSt.update(0, arr.length - 1, 7L);
        assertEquals(plainSt.query(0, arr.length - 1), st.query(0, arr.length - 1));
        assertEquals(plainSt.query(500, 500), st.query(500, 500));

        st.update(0, arr.length - 1, 3L);
        plainSt.update(0, arr.length - 1, 3L);
        assertEquals(plainSt.query(0, 0), st.query(0, 0));
        assertEquals(plainSt.query(999, 999), st.query(999, 999));
        assertEquals(plainSt.query(0, 999), st.query(0, 999));

        st.update(0, arr.length - 1, Long.MAX_VALUE);
        plainSt.update(0, arr.length - 1, Long.MAX_VALUE);
        assertEquals(plainSt.query(100, 200), st.query(100, 200));
        assertEquals(plainSt.query(50, 950), st.query(50, 950));
    }

    @Test
    void testIntersectingRangesAssigns() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(rd::nextLong).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);

        for (int i = 0; i < 100; i++) {
            int l = rd.nextInt(1000);
            int r = rd.nextInt(1000);
            if (l > r) { int tmp = l; l = r; r = tmp; }
            long delta = rd.nextLong() % 50;
            st.update(l, r, delta);
            plainSt.update(l, r, delta);
        }

        for (int i = 0; i < 50; i++) {
            int l = rd.nextInt(1000);
            int r = rd.nextInt(1000);
            if (l > r) { int tmp = l; l = r; r = tmp; }
            assertEquals(plainSt.query(l, r), st.query(l, r));
        }
    }

    @Test
    void testEdgeRangesAssigns() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(rd::nextLong).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);

        st.update(0, 0, 1L);
        plainSt.update(0, 0, 1L);
        st.update(999, 999, 2L);
        plainSt.update(999, 999, 2L);
        st.update(0, 1, 3L);
        plainSt.update(0, 1, 3L);
        st.update(998, 999, 4L);
        plainSt.update(998, 999, 4L);
        st.update(0, 999, 5L);
        plainSt.update(0, 999, 5L);

        assertEquals(plainSt.query(0, 0), st.query(0, 0));
        assertEquals(plainSt.query(999, 999), st.query(999, 999));
        assertEquals(plainSt.query(1, 1), st.query(1, 1));
        assertEquals(plainSt.query(998, 998), st.query(998, 998));
        assertEquals(plainSt.query(100, 900), st.query(100, 900));
    }

    @Test
    void testConsecutiveAssigns() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(rd::nextLong).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, null);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, null);

        st.update(0, 2, 15L);
        plainSt.update(0, 2, 15L);
        st.update(0, 2, 5L);
        plainSt.update(0, 2, 5L);
        st.update(0, 2, 20L);
        plainSt.update(0, 2, 20L);
        st.update(0, 2, 0L);
        plainSt.update(0, 2, 0L);
        st.update(1, 2, 7L);
        plainSt.update(1, 2, 7L);

        assertEquals(plainSt.query(0, 0), st.query(0, 0));
        assertEquals(plainSt.query(1, 1), st.query(1, 1));
        assertEquals(plainSt.query(2, 2), st.query(2, 2));
        assertEquals(plainSt.query(0, 2), st.query(0, 2));
        assertEquals(plainSt.query(1, 2), st.query(1, 2));
    }

    @Test
    void testMixedAssigns() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(rd::nextLong).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, null);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, null);

        for (int i = 0; i < 50; i++) {
            int idx = rd.nextInt(1000);
            long delta = rd.nextLong() % 20;
            st.update(idx, idx, delta);
            plainSt.update(idx, idx, delta);
        }
        for (int i = 0; i < 50; i++) {
            int l = rd.nextInt(1000);
            int r = rd.nextInt(1000);
            if (l > r) { int tmp = l; l = r; r = tmp; }
            long delta = rd.nextLong() % 20;
            st.update(l, r, delta);
            plainSt.update(l, r, delta);
        }

        for (int i = 0; i < 100; i++) {
            int l = rd.nextInt(1000);
            int r = rd.nextInt(1000);
            if (l > r) { int tmp = l; l = r; r = tmp; }
            assertEquals(plainSt.query(l, r), st.query(l, r));
        }
        assertEquals(plainSt.query(0, 999), st.query(0, 999));
    }

    @Test
    void testEmptyAndFullAssigns() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(rd::nextLong).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, null);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, null);

        assertEquals(plainSt.query(0, 1000-1), st.query(0, 1000-1));
        assertEquals(plainSt.query(100, 100), st.query(100, 100));

        st.update(0, 1000-1, 1L);
        plainSt.update(0, 1000-1, 1L);
        assertEquals(plainSt.query(0, 1000-1), st.query(0, 1000-1));
        assertEquals(plainSt.query(500, 500), st.query(500, 500));
    }
}
