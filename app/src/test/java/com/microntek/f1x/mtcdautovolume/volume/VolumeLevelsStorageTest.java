package com.microntek.f1x.mtcdautovolume.volume;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-07-02.
 */

public class VolumeLevelsStorageTest {
    @Before
    public void init() throws JSONException {
        initMocks(this);
        when(mMockSharedPreferences.edit()).thenReturn(mMockSharedPreferencesEditor);

        mVolumeLevelsArray = new JSONArray();
        mVolumeLevelsArray.put(10);
        mVolumeLevelsArray.put(20);
        mVolumeLevelsArray.put(30);
        mVolumeLevelsArray.put(40);

        mVolumeLevelsJson = new JSONObject();
        mVolumeLevelsJson.put(VolumeLevelsStorage.VOLUME_LEVELS_ARRAY_NAME, mVolumeLevelsArray);

        when(mMockSharedPreferences.getString(eq(VolumeLevelsStorage.VOLUME_LEVELS_PREFERENCE_NAME), any(String.class))).thenReturn(mVolumeLevelsJson.toString());
    }

    @Test
    public void test_ReadVolumeLevels() throws JSONException {
        VolumeLevelsStorage volumeLevelsStorage = new VolumeLevelsStorage(mMockSharedPreferences);
        volumeLevelsStorage.readVolumeLevels();

        assertEquals(mVolumeLevelsArray.getInt(0), volumeLevelsStorage.getLevel(0));
        assertEquals(mVolumeLevelsArray.getInt(1), volumeLevelsStorage.getLevel(1));
        assertEquals(mVolumeLevelsArray.getInt(2), volumeLevelsStorage.getLevel(2));
        assertEquals(mVolumeLevelsArray.getInt(3), volumeLevelsStorage.getLevel(3));
    }

    @Test
    public void test_StoreVolumeLevels() throws JSONException {
        List<Integer> volumeLevels = new ArrayList();
        for(int i = 0; i < mVolumeLevelsArray.length(); ++i) {
            volumeLevels.add(mVolumeLevelsArray.getInt(i));
        }

        VolumeLevelsStorage volumeLevelsStorage = new VolumeLevelsStorage(mMockSharedPreferences);
        volumeLevelsStorage.storeVolumeLevels(volumeLevels);

        verify(mMockSharedPreferencesEditor).putString(VolumeLevelsStorage.VOLUME_LEVELS_PREFERENCE_NAME, mVolumeLevelsJson.toString());
        verify(mMockSharedPreferencesEditor, times(1)).commit();
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void test_IndexOutOfBound() throws JSONException {
        VolumeLevelsStorage volumeLevelsStorage = new VolumeLevelsStorage(mMockSharedPreferences);
        volumeLevelsStorage.readVolumeLevels();
        volumeLevelsStorage.getLevel(5);
    }

    @Mock
    SharedPreferences mMockSharedPreferences;

    @Mock
    SharedPreferences.Editor mMockSharedPreferencesEditor;

    private JSONObject mVolumeLevelsJson;
    private JSONArray mVolumeLevelsArray;
}
