package segtree;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class SumPlusAddTest {
    final private Aggregator<Long> SumAgg = Long::sum;

    final private Updater<Long, Long> AddUpd = new Updater<>() {
        @Override
        public Long apply(Long value, Long update, int length) {
            return value + update * length;
        }

        @Override
        public Long compose(Long oldUpdate, Long newUpdate) {
            return oldUpdate + newUpdate;
        }
    };

    @Test
    void testPointRange() {
        Long[] arr = {10L, 20L, 30L, 40L};
        SegmentTree<Long, Long> st = new SegmentTree<>(arr, SumAgg, AddUpd, 0L, 0L);
        assertEquals(30L, st.query(2, 2));
        st.update(2, 2, 10L);
        assertEquals(40L, st.query(2, 2));
    }

    @Test
    void testFullRangeUpdateAndQuery() {
        Long[] arr = new Long[100];
        for (int i = 0; i < arr.length; i++) arr[i] = 1L;
        SegmentTree<Long, Long> st = new SegmentTree<>(arr, SumAgg, AddUpd, 0L, 0L);
        st.update(0, arr.length - 1, 2L);
        assertEquals(300L, st.query(0, arr.length - 1));
    }

    @Test
    void testIntersectingRanges() {
        Long[] arr = {1L, 2L, 3L, 4L, 5L};
        SegmentTree<Long, Long> st = new SegmentTree<>(arr, SumAgg, AddUpd, 0L, 0L);
        st.update(1, 3, 1L);
        st.update(2, 4, 2L);
        assertEquals(1L + 3L + 6L + 7L + 7L, st.query(0, 4));
    }

    @Test
    void testBoundaryAccess() {
        Long[] arr = {5L, 10L, 15L};
        SegmentTree<Long, Long> st = new SegmentTree<>(arr, SumAgg, AddUpd, 0L, 0L);
        st.update(0, 0, 5L);
        st.update(2, 2, 5L);
        assertEquals(10L, st.query(0, 0));
        assertEquals(20L, st.query(2, 2));
    }

    @Test
    void testMultipleConsecutiveUpdates() {
        Long[] arr = {1L, 1L, 1L};
        SegmentTree<Long, Long> st = new SegmentTree<>(arr, SumAgg, AddUpd, 0L, 0L);
        st.update(0, 2, 2L);
        st.update(0, 2, 3L);
        assertEquals(18L, st.query(0, 2)); // 3 + 6 + 9
    }
}
