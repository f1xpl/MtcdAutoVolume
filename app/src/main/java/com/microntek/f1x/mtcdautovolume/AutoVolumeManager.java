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
    AutoVolumeManager(VolumeLevelManager volumeLevelManager, VolumeLevelsStorage volumeLevelsStorage, int speedRangesCount) {
        mVolumeLevelManager = volumeLevelManager;
        mVolumeLevelsStorage = volumeLevelsStorage;
        mSpeedRanges = SpeedRange.calculateSpeedRanges(speedRangesCount);
        mActive = true;
        mCurrentSpeedRange = null;
    }

    void initialize() throws JSONException {
        mVolumeLevelsStorage.readVolumeLevels();
    }

    void destroy() {
        mVolumeLevelsStorage.destroy();
    }

    void adjustVolumeForSpeed(int speedKph) throws IndexOutOfBoundsException {
        if(mActive) {
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

    private final List<SpeedRange> mSpeedRanges;
    private final VolumeLevelsStorage mVolumeLevelsStorage;
    private final VolumeLevelManager mVolumeLevelManager;
    private boolean mActive;
    private SpeedRange mCurrentSpeedRange;
}
