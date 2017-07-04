package com.microntek.f1x.mtcdautovolume;

import com.microntek.f1x.mtcdautovolume.speed.SpeedRange;
import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelManager;
import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelsStorage;

import org.json.JSONException;

import java.util.List;

/**
 * Created by COMPUTER on 2017-07-02.
 */

class AutoVolumeManager {
    AutoVolumeManager(VolumeLevelManager volumeLevelManager, VolumeLevelsStorage volumeLevelsStorage) {
        mVolumeLevelManager = volumeLevelManager;
        mVolumeLevelsStorage = volumeLevelsStorage;
        mActive = true;
        mCurrentSpeedRange = null;
    }

    void initialize() throws JSONException {
        mVolumeLevelsStorage.readVolumeLevels();
        mSpeedRanges = SpeedRange.calculateSpeedRanges(mVolumeLevelsStorage.getVolumeLevelsCount());
    }

    void destroy() {
        mVolumeLevelsStorage.destroy();
    }

    void adjustVolumeForSpeed(int speedKph) throws IndexOutOfBoundsException {
        if(mActive && mSpeedRanges != null) {
            for (int i = 0; i < mSpeedRanges.size(); ++i) {
                SpeedRange speedRange = mSpeedRanges.get(i);
                if (speedRange.isInRange(speedKph) && speedRange != mCurrentSpeedRange) {
                    mVolumeLevelManager.setVolumeLevel(mVolumeLevelsStorage.getLevel(i));
                    mCurrentSpeedRange = speedRange;
                    break;
                }
            }
        }
    }

    void toggleActive() {
        mActive = !mActive;
        mCurrentSpeedRange = null;
    }

    boolean isActive() {
        return mActive;
    }

    void enable() {
        mActive = true;
    }

    void disable() {
        mActive = false;
    }

    private final VolumeLevelsStorage mVolumeLevelsStorage;
    private final VolumeLevelManager mVolumeLevelManager;
    private List<SpeedRange> mSpeedRanges;
    private boolean mActive;
    private SpeedRange mCurrentSpeedRange;
}
