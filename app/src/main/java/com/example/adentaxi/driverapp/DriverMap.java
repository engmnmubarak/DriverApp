package com.example.adentaxi.driverapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adentaxi.driverapp.Interface.DriverInfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DriverMap extends FragmentActivity implements LocationListener,
        OnMapReadyCallback {

    LocationRequest LocationRequestDriver;
    private FusedLocationProviderClient mFusedLocationPC;

    private FirebaseDatabase database;
    private FirebaseAuth Auth;
    Double driverLatitude;
    Double driverLongitude;
    private Button btn_active,btn_disactive;
    ImageView btn_logout;
    private String userkey;

    private GoogleMap mGoogleMap;
    Marker currentMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mFusedLocationPC = LocationServices.getFusedLocationProviderClient(DriverMap.this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        database = FirebaseDatabase.getInstance();
        Auth = FirebaseAuth.getInstance();

        btn_active = findViewById(R.id.btn_active);
        btn_disactive = findViewById(R.id.btn_disactive);
        btn_logout = findViewById(R.id.logout);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DriverMap.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        btn_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getLocation();

            }
        });

        btn_disactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    disactiveDriver();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        //Set Message and Title
                    AlertDialog.Builder builder = new AlertDialog.Builder(DriverMap.this);
                    builder.setMessage("هل تريد تسجيل الخروج من تطبيق عدن تاكسي؟")
                            .setTitle("تاكيد تسجيل الخروج                     ");

                    //Set When SEND Button Click
                    builder.setPositiveButton("خروج", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Auth.signOut();
                            Toast.makeText(DriverMap.this, "تم تسجيل الخروج بنجاج ..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DriverMap.this,SplashActivity.class));
                            finish();
                        }
                    });

                    //Set When Cancel Button Click
                    builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //Dismissing the alertDialog
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                        dialog.show();
            }
        });
    }



    void getLocation() {
        try {
            checkLocationPermission();
            mFusedLocationPC.requestLocationUpdates(LocationRequestDriver, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);

            userkey = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference reference = database.getReference("Drivers").child(userkey);
            reference.child("driver_active").setValue("1");
            btn_active.setVisibility(View.GONE);
            btn_disactive.setVisibility(View.VISIBLE);

        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    void disactiveDriver() {
        try {
            btn_active.setVisibility(View.VISIBLE);
            btn_disactive.setVisibility(View.GONE);
            currentMarker.remove();
            mGoogleMap.clear();

            userkey = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = database.getReference("Drivers").child(userkey);
            databaseReference.child("driver_active").setValue("0");
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LocationRequestDriver = new LocationRequest();
        LocationRequestDriver.setInterval(1000); //Refresh map since 1 second
        LocationRequestDriver.setFastestInterval(1000);
        LocationRequestDriver.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            }else{
                checkLocationPermission();
            }
        }
    }


    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for(Location location : locationResult.getLocations()){
                if(getApplicationContext()!=null){

                    mGoogleMap.clear();
                    driverLatitude = location.getLatitude();
                    driverLongitude = location.getLongitude();
                    LatLng driver_LatLng = new LatLng(driverLatitude,driverLongitude);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(driver_LatLng));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

                    userkey = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference databaseReference = database.getReference("Drivers");
                    databaseReference.child(userkey)
                            .child("driverLatitude").setValue(driverLatitude);
                    databaseReference.child(userkey)
                            .child("driverLongitude")
                            .setValue(driverLongitude);


                        if (currentMarker!=null) {
                            currentMarker.remove();
                            currentMarker=null;
                        }

                        if (currentMarker==null) {
                            currentMarker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(driver_LatLng)
                                    .title("أنا")
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_drloc)));
                        }

                    }
                }
        }
    };

    // check Location Permission to use and display the map
    private void checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(DriverMap.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            }
            else{
                ActivityCompat.requestPermissions(DriverMap.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        mFusedLocationPC.requestLocationUpdates(LocationRequestDriver, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else{
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        Toast.makeText(DriverMap.this, "Please Enable GPS and Internet", Toast.LENGTH_LONG).show();

    }
}