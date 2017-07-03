package com.microntek.f1x.mtcdautovolume.volume;

/**
 * Created by COMPUTER on 2017-07-03.
 */

public class VolumeLevelsGenerator {
    public static int[] generate(int startRange, int endRange, int count) {
        if(startRange >= endRange || count < 1) {
            return null;
        }

        final int range = endRange - startRange;
        final int generationsCount = count - 2;
        final double step = range / (double)generationsCount;

        final int[] volumeLevels = new int[count];
        volumeLevels[0] = startRange;
        volumeLevels[count - 1] = endRange;

        double progress = startRange + step;
        for(int i = 1; i <= generationsCount; ++i) {
            volumeLevels[i] = Math.min((int)Math.round(progress), endRange);
            progress += step;
        }

        return volumeLevels;
    }
}
