package com.microntek.f1x.mtcdautovolume.volume;

import android.content.Context;
import android.microntek.CarManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created by COMPUTER on 2017-07-02.
 */


@RunWith(PowerMockRunner.class)
@PrepareForTest( android.provider.Settings.System.class )
public class VolumeLevelManagerTest {
    @Before
    public void init() {
        PowerMockito.mockStatic(android.provider.Settings.System.class);
        initMocks(this);
    }

    @Test
    public void test_setVolume() {
        VolumeLevelManager volumeLevelManager = new VolumeLevelManager(mMockCarManager, mMockContext);

        when(mMockContext.getContentResolver()).thenReturn(null);
        when(android.provider.Settings.System.getInt(null, "av_volume=", 0)).thenReturn(1);
        when(mMockCarManager.getParameters("av_mute=")).thenReturn("false");
        volumeLevelManager.setVolumeLevel(30);

        verify(mMockCarManager).setParameters("av_volume=40");
        verifyStatic();
    }

    @Test
    public void test_setVolumeWhenMute() {
        VolumeLevelManager volumeLevelManager = new VolumeLevelManager(mMockCarManager, mMockContext);

        when(mMockContext.getContentResolver()).thenReturn(null);
        when(android.provider.Settings.System.getInt(null, "av_volume=", 0)).thenReturn(1);
        when(mMockCarManager.getParameters("av_mute=")).thenReturn("true");
        volumeLevelManager.setVolumeLevel(30);

        verify(mMockCarManager, times(0)).setParameters(any(String.class));
        verifyStatic();
    }

    @Test
    public void test_setVolumeWhenSystemVolumeIsGreater() {
        VolumeLevelManager volumeLevelManager = new VolumeLevelManager(mMockCarManager, mMockContext);

        when(mMockContext.getContentResolver()).thenReturn(null);
        when(android.provider.Settings.System.getInt(null, "av_volume=", 0)).thenReturn(31);
        when(mMockCarManager.getParameters("av_mute=")).thenReturn("false");
        volumeLevelManager.setVolumeLevel(30);

        verify(mMockCarManager, times(0)).setParameters(any(String.class));
        verifyStatic();
    }

    @Mock
    CarManager mMockCarManager;

    @Mock
    Context mMockContext;
}
