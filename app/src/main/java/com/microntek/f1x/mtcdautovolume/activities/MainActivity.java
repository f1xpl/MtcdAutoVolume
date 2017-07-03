package com.microntek.f1x.mtcdautovolume.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.microntek.CarManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.microntek.f1x.mtcdautovolume.EqualizerBar;
import com.microntek.f1x.mtcdautovolume.MtcdAutoVolumeService;
import com.microntek.f1x.mtcdautovolume.R;
import com.microntek.f1x.mtcdautovolume.speed.SpeedRange;
import com.microntek.f1x.mtcdautovolume.speed.SpeedUnit;
import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelManager;
import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelsStorage;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationPermissions();

        mVolumeLevelsStorage = new VolumeLevelsStorage(getSharedPreferences(MtcdAutoVolumeService.SHARED_PREFERENCES_NAME, MODE_PRIVATE));
        mEqualizerBars = new ArrayList<>();
        mVolumeLevelManager = new VolumeLevelManager(new CarManager());

        try {
            createEqualizerBars(EQUALIZER_BAR_IDS, SpeedUnit.KPH);
            setVolumeLevels();
        } catch (JSONException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        Button toggleSpeedUnitButton = (Button)this.findViewById(R.id.buttonToggleSpeedUnit);
        toggleSpeedUnitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSpeedUnit();
            }
        });

        Button setLinearButton = (Button)this.findViewById(R.id.buttonSetLinear);
        setLinearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VolumeLevelRangeActivity.class);
                intent.putExtra(VolumeLevelRangeActivity.VOLUME_LEVELS_COUNT_EXTRA, EQUALIZER_BAR_IDS.length);
                MainActivity.this.startActivityForResult(intent, GENERATE_RANGE_REQUEST_ID);
            }
        });

        Button saveButton = (Button)this.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveVolumeLevels();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVolumeLevelsStorage.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == ACCESS_FINE_LOCATION_PERMISSION_REQUEST_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, MtcdAutoVolumeService.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == GENERATE_RANGE_REQUEST_ID) {
            final int[] range = data.getIntArrayExtra(VolumeLevelRangeActivity.GENERATED_RANGE_EXTRA);

            for(int i = 0; i < mEqualizerBars.size(); ++i) {
                mEqualizerBars.get(i).setVolumeLevel(range[i]);
            }
        }
    }

    void toggleSpeedUnit() {
        for(EqualizerBar equalizerBar : mEqualizerBars) {
            equalizerBar.toggleSpeedUnit();
        }
    }

    void saveVolumeLevels() {
        List<Integer> volumeLevels = new ArrayList<>();
        for(EqualizerBar equalizerBar : mEqualizerBars) {
            volumeLevels.add(equalizerBar.getVolumeLevel());
        }

        int textId;

        try {
            textId = mVolumeLevelsStorage.storeVolumeLevels(volumeLevels) ? R.string.VolumeLevelsSaved : R.string.UnableToStoreVolumeLevelSettings;
        } catch (JSONException e) {
            e.printStackTrace();
            textId = R.string.UnableToCreateVolumeLevelSettings;
        }

        Toast.makeText(this, this.getString(textId), Toast.LENGTH_LONG).show();
    }

    void createEqualizerBars(int resIds[], SpeedUnit speedUnit) {
        final List<SpeedRange> speedRanges = SpeedRange.calculateSpeedRanges(resIds.length);
        final ScrollView equalizerLayout = (ScrollView)this.findViewById(R.id.layoutEqualizer);

        for(int i = 0; i < resIds.length; ++i) {
            final SpeedRange speedRange = speedRanges.get(i);
            mEqualizerBars.add(new EqualizerBar((LinearLayout)equalizerLayout.findViewById(resIds[i]), speedRange, speedUnit, mVolumeLevelManager));
        }
    }

    void setVolumeLevels() throws JSONException, IndexOutOfBoundsException {
        mVolumeLevelsStorage.readVolumeLevels();

        for(int i = 0; i < EQUALIZER_BAR_IDS.length; ++i) {
            mEqualizerBars.get(i).setVolumeLevel(mVolumeLevelsStorage.getLevel(i));
        }
    }

    void checkLocationPermissions() {
        if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, MtcdAutoVolumeService.class));
        } else {
            ActivityCompat.requestPermissions( this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, ACCESS_FINE_LOCATION_PERMISSION_REQUEST_ID);
        }
    }

    private VolumeLevelsStorage mVolumeLevelsStorage;
    private List<EqualizerBar> mEqualizerBars;
    private VolumeLevelManager mVolumeLevelManager;

    private final int ACCESS_FINE_LOCATION_PERMISSION_REQUEST_ID = 1443;
    private final int GENERATE_RANGE_REQUEST_ID = 443;
    public static final int[] EQUALIZER_BAR_IDS = new int[]{R.id.layoutEqualizerBar0,
                                                                R.id.layoutEqualizerBar1,
                                                                R.id.layoutEqualizerBar2,
                                                                R.id.layoutEqualizerBar3,
                                                                R.id.layoutEqualizerBar4,
                                                                R.id.layoutEqualizerBar5,
                                                                R.id.layoutEqualizerBar6,
                                                                R.id.layoutEqualizerBar7,
                                                                R.id.layoutEqualizerBar8,
                                                                R.id.layoutEqualizerBar9,
                                                                R.id.layoutEqualizerBar10,
                                                                R.id.layoutEqualizerBar11,
                                                                R.id.layoutEqualizerBar12,
                                                                R.id.layoutEqualizerBar13,
                                                                R.id.layoutEqualizerBar14,
                                                                R.id.layoutEqualizerBar15,
                                                                R.id.layoutEqualizerBar16,
                                                                R.id.layoutEqualizerBar17,
                                                                R.id.layoutEqualizerBar18,
                                                                R.id.layoutEqualizerBar19,
                                                                R.id.layoutEqualizerBar20,
                                                                R.id.layoutEqualizerBar21,
                                                                R.id.layoutEqualizerBar22,
                                                                R.id.layoutEqualizerBar23,
                                                                R.id.layoutEqualizerBar24,
                                                                R.id.layoutEqualizerBar25,
                                                                R.id.layoutEqualizerBar26,
                                                                R.id.layoutEqualizerBar27};
}
