package segtree;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Random;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SegmentTree test
 * Апдейтер и агрегатор - сумма
 */
public class SumPlusAddTest {

    /// Реализация Aggregator для нахождения суммы на отрезке
    final private Aggregator<Long> SumAgg = Long::sum;

    /// Реализация Updater для прибавления числа ко всем элементам на отрезке
    final private Updater<Long, Long> AddUpd = new Updater<>() {
        @Override
        public Long apply(Long value, Long update, int length) {
            return value + update * length;
        }

        @Override
        public Long compose(Long oldUpdate, Long newUpdate) {
            return Objects.isNull(oldUpdate) ? newUpdate : oldUpdate + newUpdate; // баг -3 часа жизни
        }
    };

    @Test
    void testPointRangesAdditions() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(() -> rd.nextLong(-1000, 1000)).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, SumAgg, AddUpd, 0L, 0L);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, SumAgg, AddUpd, 0L, 0L);

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
    void testFullRangesAdditions() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(() -> rd.nextLong(-1000, 1000)).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, SumAgg, AddUpd, 0L, 0L);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, SumAgg, AddUpd, 0L, 0L);

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
    void testIntersectingRangesAdditions() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(() -> rd.nextLong(-1000, 1000)).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, SumAgg, AddUpd, 0L, 0L);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, SumAgg, AddUpd, 0L, 0L);

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
    void testEdgeRangesAdditions() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(() -> rd.nextLong(-1000, 1000)).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, SumAgg, AddUpd, 0L, 0L);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, SumAgg, AddUpd, 0L, 0L);

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
    void testConsecutiveAdditions() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(() -> rd.nextLong(-1000, 1000)).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, SumAgg, AddUpd, 0L, 0L);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, SumAgg, AddUpd, 0L, 0L);

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
    void testMixedAdditions() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(() -> rd.nextLong(-1000, 1000)).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, SumAgg, AddUpd, 0L, 0L);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, SumAgg, AddUpd, 0L, 0L);

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
    void testEmptyAndFullAdditions() {
        Random rd = new Random(42);
        Long[] arr = LongStream.generate(() -> rd.nextLong(-1000, 1000)).limit(1000).boxed().toArray(Long[]::new);

        SegmentTree<Long, Long> st = new SegmentTree<>(arr, SumAgg, AddUpd, 0L, 0L);
        segtree.testUtils.PlainArrSeg<Long, Long> plainSt = new segtree.testUtils.PlainArrSeg<>(arr, SumAgg, AddUpd, 0L, 0L);

        assertEquals(plainSt.query(0, 1000-1), st.query(0, 1000-1));
        assertEquals(plainSt.query(100, 100), st.query(100, 100));

        st.update(0, 1000-1, 1L);
        plainSt.update(0, 1000-1, 1L);
        assertEquals(plainSt.query(0, 1000-1), st.query(0, 1000-1));
        assertEquals(plainSt.query(500, 500), st.query(500, 500));
    }
}
