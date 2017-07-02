package com.microntek.f1x.mtcdautovolume.speed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-07-01.
 */

public class SpeedRange {
    SpeedRange(int minKph, int maxKph) {
        mMinKph = minKph;
        mMaxKph = maxKph;
    }

    public boolean isInRange(int speedKph) {
        return speedKph >= mMinKph && speedKph <= mMaxKph;
    }

    public int getMinKph() {
        return mMinKph;
    }

    public int getMaxKph() {
        return mMaxKph;
    }

    public int getMinMph() {
        return (int)(mMinKph / 1.6);
    }

    public int getMaxMph() {
        return (int)(mMaxKph / 1.6);
    }

    public static List<SpeedRange> calculateSpeedRanges(int count) {
        List<SpeedRange> list = new ArrayList<>();

        int currentMinSpeedKph = SpeedRange.MIN_SPEED;
        final int speedStep = (SpeedRange.MAX_SPEED - SpeedRange.MIN_SPEED) / count;
        int currentMaxSpeedKph = SpeedRange.MIN_SPEED + speedStep;

        for(int i = 0; i < count; ++i) {
            list.add( new SpeedRange(currentMinSpeedKph, currentMaxSpeedKph - 1));
            currentMinSpeedKph += speedStep;
            currentMaxSpeedKph += speedStep;
        }

        return list;
    }

    private final int mMinKph;
    private final int mMaxKph;

    private final static int MIN_SPEED = 0;
    private final static int MAX_SPEED = 140;
}