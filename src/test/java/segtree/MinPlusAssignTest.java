package segtree;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class MinPlusAssignTest {
    final private Aggregator<Long> MinAgg = Long::min;

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
    void testPointRangeAssign() {
        Long[] arr = {8L, 6L, 4L};
        SegmentTree<Long, Long> st = new SegmentTree<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);
        st.update(1, 1, 2L);
        assertEquals(2L, st.query(1, 1));
    }

    @Test
    void testFullRangeAssign() {
        Long[] arr = new Long[500];
        for (int i = 0; i < arr.length; i++) arr[i] = (long) i;
        SegmentTree<Long, Long> st = new SegmentTree<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);
        st.update(0, arr.length - 1, 7L);
        assertEquals(7L, st.query(0, arr.length - 1));
    }

    @Test
    void testIntersectingAssignRanges() {
        Long[] arr = {9L, 8L, 7L, 6L};
        SegmentTree<Long, Long> st = new SegmentTree<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);
        st.update(1, 3, 10L);
        st.update(2, 3, 2L);
        assertEquals(2L, st.query(0, 3));
    }

    @Test
    void testEdgeUpdates() {
        Long[] arr = {100L, 200L, 300L};
        SegmentTree<Long, Long> st = new SegmentTree<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);
        st.update(0, 0, 1L);
        st.update(2, 2, 2L);
        assertEquals(1L, st.query(0, 0));
        assertEquals(2L, st.query(2, 2));
    }

    @Test
    void testConsecutiveAssigns() {
        Long[] arr = {10L, 20L, 30L};
        SegmentTree<Long, Long> st = new SegmentTree<>(arr, MinAgg, AsgUpd, Long.MAX_VALUE, 0L);
        st.update(0, 2, 15L);
        st.update(0, 2, 5L);
        assertEquals(5L, st.query(0, 2));
    }
}

