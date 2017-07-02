package com.microntek.f1x.mtcdautovolume;

import com.microntek.f1x.mtcdautovolume.speed.SpeedRange;
import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelManager;
import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelsStorage;

import org.json.JSONException;

import java.util.List;

/**
 * Created by COMPUTER on 2017-07-02.
 */

public class AutoVolumeManager {
    public AutoVolumeManager(VolumeLevelManager volumeLevelManager, VolumeLevelsStorage volumeLevelsStorage, int speedRangesCount) {
        mVolumeLevelManager = volumeLevelManager;
        mVolumeLevelsStorage = volumeLevelsStorage;
        mSpeedRanges = SpeedRange.calculateSpeedRanges(speedRangesCount);
        mActive = true;
        mCurrentSpeedRange = null;
    }

    public void initialize() throws JSONException {
        mVolumeLevelsStorage.readVolumeLevels();
    }

    public void destroy() {
        mVolumeLevelsStorage.destroy();
    }

    public void adjustVolumeForSpeed(int speedKph) throws IndexOutOfBoundsException {
        for (int i = 0; i < mSpeedRanges.size(); ++i) {
            SpeedRange speedRange = mSpeedRanges.get(i);
            if (speedRange.isInRange(speedKph) && speedRange != mCurrentSpeedRange) {
                mVolumeLevelManager.setVolumeLevel(mVolumeLevelsStorage.getLevel(i));
                mCurrentSpeedRange = speedRange;
                break;
            }
        }
    }

    public void toggleActive() {
        mActive = !mActive;
        mCurrentSpeedRange = null;
    }

    public boolean isActive() {
        return mActive;
    }

    private final List<SpeedRange> mSpeedRanges;
    private final VolumeLevelsStorage mVolumeLevelsStorage;
    private final VolumeLevelManager mVolumeLevelManager;
    private boolean mActive;
    private SpeedRange mCurrentSpeedRange;
}
