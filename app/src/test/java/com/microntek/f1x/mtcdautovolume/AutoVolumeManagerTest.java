package com.microntek.f1x.mtcdautovolume;

import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelManager;
import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelsStorage;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-07-02.
 */

public class AutoVolumeManagerTest {
    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void test_AdjustVolumeForSpeed() throws JSONException {
        AutoVolumeManager autoVolumeManager = new AutoVolumeManager(mMockVolumeLevelManager, mMockVolumeLevelsStorage);
        when(mMockVolumeLevelsStorage.getVolumeLevelsCount()).thenReturn(6);

        autoVolumeManager.initialize();
        final int speedKph = 64;
        final int volumeLevel = 49;

        when(mMockVolumeLevelsStorage.getLevel(2)).thenReturn(volumeLevel);
        autoVolumeManager.adjustVolumeForSpeed(speedKph);
        verify(mMockVolumeLevelManager).setVolumeLevel(volumeLevel);

        autoVolumeManager.destroy();
    }

    @Test
    public void test_VolumeNotAdjustedTwiceForTheSameSpeed() throws JSONException {
        AutoVolumeManager autoVolumeManager = new AutoVolumeManager(mMockVolumeLevelManager, mMockVolumeLevelsStorage);
        when(mMockVolumeLevelsStorage.getVolumeLevelsCount()).thenReturn(6);

        autoVolumeManager.initialize();
        final int speedKph = 64;
        final int volumeLevel = 49;

        when(mMockVolumeLevelsStorage.getLevel(2)).thenReturn(volumeLevel);
        autoVolumeManager.adjustVolumeForSpeed(speedKph);
        autoVolumeManager.adjustVolumeForSpeed(speedKph);
        verify(mMockVolumeLevelManager, times(1)).setVolumeLevel(volumeLevel);

        autoVolumeManager.destroy();
    }

    @Test
    public void test_Toggle() throws JSONException {
        AutoVolumeManager autoVolumeManager = new AutoVolumeManager(mMockVolumeLevelManager, mMockVolumeLevelsStorage);
        when(mMockVolumeLevelsStorage.getVolumeLevelsCount()).thenReturn(6);

        autoVolumeManager.initialize();
        final int speedKph = 64;
        final int volumeLevel = 49;

        when(mMockVolumeLevelsStorage.getLevel(2)).thenReturn(volumeLevel);
        autoVolumeManager.adjustVolumeForSpeed(speedKph);

        autoVolumeManager.toggleActive();
        assertFalse(autoVolumeManager.isActive());

        autoVolumeManager.adjustVolumeForSpeed(speedKph);

        autoVolumeManager.toggleActive();
        assertTrue(autoVolumeManager.isActive());

        autoVolumeManager.adjustVolumeForSpeed(speedKph);
        verify(mMockVolumeLevelManager, times(2)).setVolumeLevel(volumeLevel);

        autoVolumeManager.destroy();
    }

    @Mock
    VolumeLevelsStorage mMockVolumeLevelsStorage;

    @Mock
    VolumeLevelManager mMockVolumeLevelManager;
}
