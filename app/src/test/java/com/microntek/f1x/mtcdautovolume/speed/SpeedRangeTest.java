package com.microntek.f1x.mtcdautovolume.speed;

import org.junit.Test;

import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by COMPUTER on 2017-07-02.
 */

public class SpeedRangeTest {
    @Test
    public void test_generateSpeedRanges() {
        final int speedRangesCount = 4;
        List<SpeedRange> speedRanges = SpeedRange.calculateSpeedRanges(speedRangesCount);
        assertEquals(speedRangesCount, speedRanges.size());

        assertEquals(0, speedRanges.get(0).getMinKph());
        assertEquals(34, speedRanges.get(0).getMaxKph());
        assertEquals(0, speedRanges.get(0).getMinMph());
        assertEquals(21, speedRanges.get(0).getMaxMph());

        assertEquals(35, speedRanges.get(1).getMinKph());
        assertEquals(69, speedRanges.get(1).getMaxKph());
        assertEquals(21, speedRanges.get(1).getMinMph());
        assertEquals(43, speedRanges.get(1).getMaxMph());

        assertEquals(70, speedRanges.get(2).getMinKph());
        assertEquals(104, speedRanges.get(2).getMaxKph());
        assertEquals(43, speedRanges.get(2).getMinMph());
        assertEquals(65, speedRanges.get(2).getMaxMph());

        assertEquals(105, speedRanges.get(3).getMinKph());
        assertEquals(139, speedRanges.get(3).getMaxKph());
        assertEquals(65, speedRanges.get(3).getMinMph());
        assertEquals(86, speedRanges.get(3).getMaxMph());
    }

    @Test
    public void test_SpeedInRange() {
        SpeedRange speedRange = new SpeedRange(20, 25);

        assertFalse(speedRange.isInRange(19));
        assertTrue(speedRange.isInRange(20));
        assertTrue(speedRange.isInRange(21));
        assertTrue(speedRange.isInRange(22));
        assertTrue(speedRange.isInRange(23));
        assertTrue(speedRange.isInRange(24));
        assertTrue(speedRange.isInRange(25));
        assertFalse(speedRange.isInRange(26));
    }

    @Test
    public void test_generateSpeedRanges_zeroCount() {
        List<SpeedRange> speedRanges = SpeedRange.calculateSpeedRanges(0);
        assertEquals(null, speedRanges);
    }
}
