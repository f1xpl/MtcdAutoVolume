package com.microntek.f1x.mtcdautovolume;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.microntek.CarManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.microntek.f1x.mtcdautovolume.activities.MainActivity;
import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelManager;
import com.microntek.f1x.mtcdautovolume.volume.VolumeLevelsStorage;

import org.json.JSONException;

/**
 * Created by COMPUTER on 2017-07-01.
 */

public class MtcdAutoVolumeService extends Service {
    public static final String SHARED_PREFERENCES_NAME = "MtcdAutoVolumePreferences";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mServiceInitialized = false;
        mAutoVolumeManager = new AutoVolumeManager(new VolumeLevelManager(new CarManager()),
                                                   new VolumeLevelsStorage(getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)),
                                                   MainActivity.EQUALIZER_BAR_IDS.length);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AUTO_VOLUME_TOGGLE_ACTION);
        intentFilter.addAction(AUTO_VOLUME_DISABLE_ACTION);
        intentFilter.addAction(AUTO_VOLUME_ENABLE_ACTION);
        registerReceiver(mAutoVolumeToggleListener, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mServiceInitialized && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(mVehicleSpeedListener);
        }

        mAutoVolumeManager.destroy();
        mServiceInitialized = false;
        unregisterReceiver(mAutoVolumeToggleListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (!mServiceInitialized) {
            try {
                mAutoVolumeManager.initialize();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 20, mVehicleSpeedListener);
                }

                mServiceInitialized = true;
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, this.getString(R.string.UnableToReadVolumeLevelSettings), Toast.LENGTH_LONG).show();
            }
        }

        return START_STICKY;
    }

    private boolean mServiceInitialized;
    private AutoVolumeManager mAutoVolumeManager;
    private LocationManager mLocationManager;

    private LocationListener mVehicleSpeedListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location.hasSpeed()) {
                try {
                    final int speedKph = (int) ((location.getSpeed() * 3600) / 1000);
                    mAutoVolumeManager.adjustVolumeForSpeed(speedKph);
                } catch(IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private BroadcastReceiver mAutoVolumeToggleListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final boolean currentState = mAutoVolumeManager.isActive();

            if(AUTO_VOLUME_TOGGLE_ACTION.equals(intent.getAction())) {
                mAutoVolumeManager.toggleActive();
            } else if(AUTO_VOLUME_DISABLE_ACTION.equals(intent.getAction())) {
                mAutoVolumeManager.disable();
            } else if(AUTO_VOLUME_ENABLE_ACTION.equals(intent.getAction())) {
                mAutoVolumeManager.enable();
            }

            if(currentState != mAutoVolumeManager.isActive()) {
                final int textResourceId = mAutoVolumeManager.isActive() ? R.string.AutoVolumeEnabled : R.string.AutoVolumeDisabled;
                Toast.makeText(MtcdAutoVolumeService.this, MtcdAutoVolumeService.this.getString(textResourceId), Toast.LENGTH_LONG).show();
            }
        }
    };

    private static final String AUTO_VOLUME_TOGGLE_ACTION = "com.microntek.f1x.mtcdautovolume.toggle";
    private static final String AUTO_VOLUME_DISABLE_ACTION = "com.microntek.f1x.mtcdautovolume.disable";
    private static final String AUTO_VOLUME_ENABLE_ACTION = "com.microntek.f1x.mtcdautovolume.enable";
}
