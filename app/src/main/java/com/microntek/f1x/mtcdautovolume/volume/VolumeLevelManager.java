package com.microntek.f1x.mtcdautovolume.volume;

import android.microntek.CarManager;

/**
 * Created by COMPUTER on 2017-07-01.
 */

public class VolumeLevelManager {
    public VolumeLevelManager(CarManager carManager) {
        mCarManager = carManager;

        try {
            mMaxVolume = Integer.parseInt(mCarManager.getParameters("cfg_maxvolume"));
        } catch (Exception e) {
            mMaxVolume = 30;
        }
    }

    public void setVolumeLevel(double percent) {
        final int currentVolume = (int)(mMaxVolume * (percent / 100));
        mCarManager.setParameters("av_volume=" + calculateVolume(currentVolume));
    }

    private int calculateVolume(int currentVolume) {
        float volume = (((float) currentVolume) * 100.0f) / ((float) mMaxVolume);
        volume = volume < 20.0f ? (volume * 3.0f) / 2.0f : volume < 50.0f ? volume + 10.0f : ((volume * 4.0f) / 5.0f) + 20.0f;
        return (int) volume;
    }

    private int mMaxVolume;
    private final CarManager mCarManager;
}
