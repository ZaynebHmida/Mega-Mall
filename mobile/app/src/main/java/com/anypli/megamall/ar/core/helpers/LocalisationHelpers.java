package com.anypli.megamall.ar.core.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

public final class LocalisationHelpers implements LocationListener , SensorEventListener {
    private Activity    mCurrentActivity;
    private LocationManager mLocationManager;
    private Location    mLastKnownLocation;
    private float[]     mMagnaticFieldValues ;
    private float[]     mAccelerometerValues ;
    private float[]     mRotation ;
    private float[]     mInclination ;
    private boolean     mDeviceLocationChanged ;
    public static SensorManager mSensorManager ;

    public LocalisationHelpers(@NonNull Activity currentActivity) {
        this.mCurrentActivity = currentActivity;
        mLocationManager = (LocationManager) mCurrentActivity.getSystemService(mCurrentActivity.LOCATION_SERVICE);
        mSensorManager= (SensorManager)mCurrentActivity.getSystemService(Context.SENSOR_SERVICE);
        mMagnaticFieldValues=new float[3];
        mAccelerometerValues=new float[3] ;
        mRotation=new float[16] ;
        mInclination=new float[16] ;
        mDeviceLocationChanged=true ;

    }

    @SuppressLint("MissingPermission")
    public boolean LocalisationUpdateConfig( long minTime, float distance) {
       if(LocalisationPermissionHelper.hasLocalisationPermission(mCurrentActivity)){
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,distance,this);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, distance, this);
            return true ;
       }else{
           return false ;
       }
    }

    public boolean onResume(){

        return mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL)
        && mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void onPause(){
       onDestroy();
    }

    public void onDestroy(){
        mSensorManager.unregisterListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
        mSensorManager.unregisterListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    //Localisation related methods
    public Location getLastKnownLocation() {
        return mLastKnownLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("Location", "location updated " );
        mLastKnownLocation=location ;
        mDeviceLocationChanged=true ;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(mCurrentActivity,provider+" is Enabled",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(mCurrentActivity,provider+" is Disabled",Toast.LENGTH_SHORT).show();
    }


    //Sensors Events related methods

    public float[] getMagnaticFieldValues() {
        return mMagnaticFieldValues;
    }

    public float[] getRotation() {
        return mRotation;
    }

    public float[] getInclination() {
        return mInclination;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
            mMagnaticFieldValues[0]=event.values[0];
            mMagnaticFieldValues[1]=event.values[1];
            mMagnaticFieldValues[2]=event.values[2];
            mDeviceLocationChanged=true;

        }else if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            mAccelerometerValues[0]=event.values[0];
            mAccelerometerValues[1]=event.values[1];
            mAccelerometerValues[2]=event.values[2];
            mDeviceLocationChanged=true ;
        }
        if(!SensorManager.getRotationMatrix(mRotation,mInclination,mAccelerometerValues,mMagnaticFieldValues)){
            Log.e("Location: ", "cannot determine device orientation");
        }else {
            Log.e("Location: ", "been able to determine device orientation");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public boolean isLocationChanged() {
        boolean res =mDeviceLocationChanged;
        mDeviceLocationChanged=false ;
        return res ;
    }
}
