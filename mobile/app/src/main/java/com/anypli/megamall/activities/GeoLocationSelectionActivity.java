package com.anypli.megamall.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anypli.megamall.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.fragment.app.FragmentActivity;

public class GeoLocationSelectionActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private MarkerOptions mSelectedPosition ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_location_selection);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        boolean haveExtra=this.getIntent().getBooleanExtra("extra",false);
        if(haveExtra) {
            mSelectedPosition = new MarkerOptions().title("Your boutique Location ?");
            mSelectedPosition.position(
                    new LatLng(this.getIntent()
                            .getDoubleExtra("lat",0.0),
                            this.getIntent().getDoubleExtra("long",0.0)));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(mSelectedPosition!=null){
            mMap.addMarker(mSelectedPosition);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mSelectedPosition.getPosition()));
        }
        mMap.setOnMapClickListener(this);
        findViewById(R.id.locationConfirmButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedPosition==null){
                    Toast.makeText(v.getContext(),"no location Seletected",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent= new Intent();
                    intent.putExtra("long",mSelectedPosition.getPosition().longitude);
                    intent.putExtra("lat",mSelectedPosition.getPosition().latitude);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(mSelectedPosition==null){
            mSelectedPosition= new MarkerOptions().title("Your boutique Location ?");
        }
       mMap.clear();
        mSelectedPosition.position(latLng);
       mMap.addMarker(mSelectedPosition);
    }

}
