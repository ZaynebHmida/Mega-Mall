package com.anypli.megamall.models;


public class BoutiqueLocation {


    private double mLongitude ;
    private double mLatitude ;
    private String mName ;

    public BoutiqueLocation(String name ,double latitude, double longitude ) {
        this.mLongitude = longitude;
        this.mLatitude = latitude;
        this.mName = name;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLongitude(float value) {
        this.mLongitude = value;

    }

    public void setLatitude(float value) {
        this.mLatitude = value;
    }

    public String getName() {
        return mName;
    }


    public float getPositionX(){

        return (float) (mLatitude);

    }
    public float getPositionZ(){
        return (float) (mLongitude);
    }

}
