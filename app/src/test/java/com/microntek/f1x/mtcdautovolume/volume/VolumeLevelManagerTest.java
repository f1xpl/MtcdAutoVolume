package com.microntek.f1x.mtcdautovolume.volume;

import android.content.ContentResolver;
import android.content.Context;
import android.microntek.CarManager;
import android.provider.Settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-07-02.
 */

@RunWith(PowerMockRunner.class)
public class VolumeLevelManagerTest {
    @Before
    public void init() {
        PowerMockito.mockStatic(android.provider.Settings.System.class);
        initMocks(this);
    }

    @Test
    public void test_setVolume() {
        VolumeLevelManager volumeLevelManager = new VolumeLevelManager(mMockCarManager);

        when(mMockCarManager.getParameters("av_mute=")).thenReturn("false");
        volumeLevelManager.setVolumeLevel(30);

        verify(mMockCarManager).setParameters("av_volume=40");
    }

    @Test
    public void test_setVolumeWhenMute() {
        VolumeLevelManager volumeLevelManager = new VolumeLevelManager(mMockCarManager);

        when(mMockCarManager.getParameters("av_mute=")).thenReturn("true");
        volumeLevelManager.setVolumeLevel(30);

        verify(mMockCarManager, times(0)).setParameters(any(String.class));
    }

    @Mock
    CarManager mMockCarManager;
}
