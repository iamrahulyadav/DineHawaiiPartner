package com.dinehawaiipartner.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dinehawaiipartner.CustomViews.CustomTextView;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Util.AppPreference;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DriverHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private static final String TAG = "DriverHomeActivity";
    private GoogleMap map;
    private Marker markerCurrent;
    private double cur_lat = 22.718358, cur_long = 75.875529;
    private View headerView;
    private CustomTextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        userName = (CustomTextView) headerView.findViewById(R.id.customerName);
        userName.setText(AppPreference.getUsername(DriverHomeActivity.this));
        checkLocationPermission();

        setUpMap();

    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(DriverHomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DriverHomeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
        }
        if (ContextCompat.checkSelfPermission(DriverHomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DriverHomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }
    }

    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(DriverHomeActivity.this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DriverHomeActivity.this);
            alertDialog.setTitle("Dine Hawaii Partner");
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.setMessage("Do you want to exit?");
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finishAffinity();

                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DriverHomeActivity.this);
        builder.setTitle("Dine Hawaii Partner");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to logout?").setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        AppPreference.clearPreference(DriverHomeActivity.this);
                        startActivity(new Intent(DriverHomeActivity.this, LoginActivity.class));
                        finish();

                    }
                })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                            }
                        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_driver_logout:
                showLogoutAlert();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(final GoogleMap maps) {
        map = maps;
        zoomIn(new LatLng(cur_lat, cur_long));
        Log.d(TAG, "onMapReady");

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (ActivityCompat.checkSelfPermission(DriverHomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DriverHomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.d("checkSelfPermission", false + "");
                    return;
                }
                map.setMyLocationEnabled(true);
                updateUserMapLocation();

                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            }
        });
    }

    private void updateUserMapLocation() {
        LatLng latLng = new LatLng(cur_lat, cur_long);
        zoomIn(latLng);
        initializeLocationMarker(latLng);
    }

    private void initializeLocationMarker(LatLng latLngMarker) {
        MarkerOptions options = new MarkerOptions()
                .flat(true)
                .position(latLngMarker)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.navigation))
                .anchor(0.5f, 0.5f);
        markerCurrent = map.addMarker(options);
    }

    private void zoomIn(LatLng latLngZoom) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngZoom, 18));
    }

}
