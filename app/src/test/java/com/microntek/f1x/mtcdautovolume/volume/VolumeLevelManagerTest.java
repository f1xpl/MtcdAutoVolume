package com.microntek.f1x.mtcdautovolume.volume;

import android.microntek.CarManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-07-02.
 */

public class VolumeLevelManagerTest {
    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void test_setVolume() {
        VolumeLevelManager volumeLevelManager = new VolumeLevelManager(mMockCarManager);

        volumeLevelManager.setVolumeLevel(30);
        verify(mMockCarManager).setParameters("av_volume=40");
    }

    @Mock
    CarManager mMockCarManager;
}
