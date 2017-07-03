package com.microntek.f1x.mtcdautovolume;

import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.microntek.f1x.mtcdautovolume.speed.SpeedRange;
import com.microntek.f1x.mtcdautovolume.speed.SpeedUnit;
import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelManager;

/**
 * Created by COMPUTER on 2017-07-01.
 */

public class EqualizerBar implements SeekBar.OnSeekBarChangeListener {
    public EqualizerBar(LinearLayout layout, SpeedRange speedRange, SpeedUnit currentSpeedUnit, VolumeLevelManager volumeLevelManager) {
        mVolumeLevelSeekBar = (SeekBar) layout.findViewById(R.id.seekBarVolumeLevel);
        mVolumeLevelSeekBar.setOnSeekBarChangeListener(this);
        mSpeedRangeTextView = (TextView)layout.findViewById(R.id.textViewSpeedRange);
        mVolumeLevelTextView = (TextView)layout.findViewById(R.id.textViewVolumeLevel);
        mSpeedRange = speedRange;
        mVolumeLevelManager = volumeLevelManager;
        setSpeedUnit(currentSpeedUnit);

        setVolumeLevel(0);
        mVolumeLevelSeekBar.setMax(100);
    }

    public void toggleSpeedUnit() {
        setSpeedUnit((mCurrentSpeedUnit == SpeedUnit.KPH) ? SpeedUnit.MPH : SpeedUnit.KPH);
    }

    public int getVolumeLevel() {
        return mVolumeLevelSeekBar.getProgress();
    }

    public void setVolumeLevel(int level) {
        mVolumeLevelSeekBar.setProgress(level);
        mVolumeLevelTextView.setText(level + "%");
    }

    private void setSpeedUnit(SpeedUnit currentSpeedUnit) {
        mCurrentSpeedUnit = currentSpeedUnit;

        int minSpeedRange = (mCurrentSpeedUnit == SpeedUnit.KPH) ? mSpeedRange.getMinKph() : mSpeedRange.getMinMph();
        int maxSpeedRange = (mCurrentSpeedUnit == SpeedUnit.KPH) ? mSpeedRange.getMaxKph() : mSpeedRange.getMaxMph();
        String speedUnit = (mCurrentSpeedUnit == SpeedUnit.KPH)? "kph" : "mph";

        mSpeedRangeTextView.setText("<" + minSpeedRange + "-" + maxSpeedRange + "> " + speedUnit);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mVolumeLevelTextView.setText(progress + "%");

        if(fromUser) {
            mVolumeLevelManager.setVolumeLevel(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private final TextView mSpeedRangeTextView;
    private final SeekBar mVolumeLevelSeekBar;
    private final TextView mVolumeLevelTextView;
    private final SpeedRange mSpeedRange;
    private final VolumeLevelManager mVolumeLevelManager;
    private SpeedUnit mCurrentSpeedUnit;
}
