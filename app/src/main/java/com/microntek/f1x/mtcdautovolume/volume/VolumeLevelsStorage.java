package com.microntek.f1x.mtcdautovolume.volume;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-07-01.
 */

public class VolumeLevelsStorage implements SharedPreferences.OnSharedPreferenceChangeListener {
    public VolumeLevelsStorage(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mVolumeLevels = new ArrayList<>();
    }

    public void destroy() {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    public boolean storeVolumeLevels(List<Integer> levels) throws JSONException {
        JSONObject volumeLevelsJson = new JSONObject();
        JSONArray volumeLevelsArray = new JSONArray();

        for(int level : levels) {
            volumeLevelsArray.put(level);
        }

        volumeLevelsJson.put(VOLUME_LEVELS_ARRAY_NAME, volumeLevelsArray);
        mSharedPreferencesEditor.putString(VOLUME_LEVELS_PREFERENCE_NAME, volumeLevelsJson.toString());
        return mSharedPreferencesEditor.commit();
    }

    public int getLevel(int index) throws IndexOutOfBoundsException {
        if(index < mVolumeLevels.size()) {
            return mVolumeLevels.get(index);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void readVolumeLevels() throws JSONException {
        String volumeLevelsString = mSharedPreferences.getString(VOLUME_LEVELS_PREFERENCE_NAME, (new JSONObject()).toString());
        JSONObject volumeLevelsJson = new JSONObject(volumeLevelsString);

        if(volumeLevelsJson.has(VOLUME_LEVELS_ARRAY_NAME)) {
            JSONArray volumeLevelsArray = volumeLevelsJson.getJSONArray(VOLUME_LEVELS_ARRAY_NAME);

            mVolumeLevels.clear();
            for (int i = 0; i < volumeLevelsArray.length(); ++i) {
                mVolumeLevels.add(volumeLevelsArray.getInt(i));
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String changedPreference) {
        if(changedPreference.equals(VOLUME_LEVELS_PREFERENCE_NAME)) {
            try {
                readVolumeLevels();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;
    private List<Integer> mVolumeLevels;

    public static final String VOLUME_LEVELS_PREFERENCE_NAME = "volumeLevelsPreference";
    public static final String VOLUME_LEVELS_ARRAY_NAME = "volumeLevels";
}
