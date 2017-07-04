package com.microntek.f1x.mtcdautovolume.volume;

import android.content.Context;
import android.microntek.CarManager;
import android.provider.Settings.System;

/**
 * Created by COMPUTER on 2017-07-01.
 */

public class VolumeLevelManager {
    public VolumeLevelManager(CarManager carManager, Context context) {
        mCarManager = carManager;
        mContext = context;

        try {
            mMaxVolume = Integer.parseInt(mCarManager.getParameters(MAX_VOLUME_PARAMETER_NAME));
        } catch (Exception e) {
            mMaxVolume = 30;
        }
    }

    public void setVolumeLevel(double percent) {
        if(mCarManager.getParameters(MUTE_PARAMETER_NAME).equals("false")) {
            final int currentVolume = calculateVolume((int) (mMaxVolume * (percent / 100)));
            final int systemVolume = calculateVolume(System.getInt(mContext.getContentResolver(), VOLUME_PARAMETER_NAME, 0));

            if(currentVolume > systemVolume) {
                mCarManager.setParameters(VOLUME_PARAMETER_NAME + currentVolume);
            }
        }
    }

    private int calculateVolume(int inputVolume) {
        float volume = (((float) inputVolume) * 100.0f) / ((float) mMaxVolume);
        volume = volume < 20.0f ? (volume * 3.0f) / 2.0f : volume < 50.0f ? volume + 10.0f : ((volume * 4.0f) / 5.0f) + 20.0f;
        return (int) volume;
    }

    private int mMaxVolume;
    private final CarManager mCarManager;
    private final Context mContext;

    private static final String VOLUME_PARAMETER_NAME = "av_volume=";
    private static final String MUTE_PARAMETER_NAME = "av_mute=";
    private static final String MAX_VOLUME_PARAMETER_NAME = "cfg_maxvolume";
}
