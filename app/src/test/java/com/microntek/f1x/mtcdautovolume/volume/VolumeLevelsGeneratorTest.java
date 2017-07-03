package com.microntek.f1x.mtcdautovolume.volume;

import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelsGenerator;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by COMPUTER on 2017-07-03.
 */

public class VolumeLevelsGeneratorTest {
    @Test
    public void test_generateRange() {
        final int result[] = VolumeLevelsGenerator.generate(20, 43, 10);

        assertEquals(10, result.length);
        assertEquals(20, result[0]);
        assertEquals(23, result[1]);
        assertEquals(26, result[2]);
        assertEquals(29, result[3]);
        assertEquals(32, result[4]);
        assertEquals(34, result[5]);
        assertEquals(37, result[6]);
        assertEquals(40, result[7]);
        assertEquals(43, result[8]);
        assertEquals(43, result[9]);
    }

    @Test
    public void test_generateRange_InvalidRangeProvided() {
        int result[] = VolumeLevelsGenerator.generate(20, 20, 10);
        assertEquals(null, result);

        result = VolumeLevelsGenerator.generate(20, 19, 10);
        assertEquals(null, result);
    }

    @Test
    public void test_generateRange_ZeroCount() {
        int result[] = VolumeLevelsGenerator.generate(20, 30, 0);
        assertEquals(null, result);
    }
}
