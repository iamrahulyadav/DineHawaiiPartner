package com.dinehawaiipartner.Activity.Driver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dinehawaiipartner.Activity.LoginActivity;
import com.dinehawaiipartner.CustomViews.CustomTextView;
import com.dinehawaiipartner.Model.DeliveryModel;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Retrofit.ApiClient;
import com.dinehawaiipartner.Retrofit.MyApiEndpointInterface;
import com.dinehawaiipartner.Util.AppConstants;
import com.dinehawaiipartner.Util.AppPreference;
import com.dinehawaiipartner.Util.DirectionsJSONParser;
import com.dinehawaiipartner.Util.Functions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    private static final String TAG = "DriverHomeActivity";
    private static final long INTERVAL = 1000 * 1;
    private static final long FASTEST_INTERVAL = 1000 * 1;
    TextView tvDeliveryId, tvName, tvPhoneNo, tvAddress;
    List<String> waypoints = new ArrayList<>();
    List<String> waypoints1 = new ArrayList<>();
    private GoogleMap map;
    private Marker markerCurrent;
    private View headerView;
    private CustomTextView userName;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Context context;
    private CardView delivery_view;
    private TextView tvHideShow;
    private LinearLayout llCustDetails;
    private DeliveryModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.customerName);
        userName.setText(AppPreference.getUsername(context));
        checkLocationPermission();
        // new LocationService(context);

        setUpMap();
        setUpFused();

        if (getIntent().getAction().equalsIgnoreCase("Delivery")) {
            initDeliveryView();
            data = (DeliveryModel) getIntent().getSerializableExtra("data");
            Log.e(TAG, "onCreate: data " + data);
            tvDeliveryId.setText("#" + data.getOrderUniqueId());
            tvName.setText(data.getCustName());
            tvPhoneNo.setText(data.getCustPhone());
            tvAddress.setText(data.getCustDeliveryAddress());

            //route();
            LatLng restaurant, source, customer;
            if (!data.getBusLatitude().equalsIgnoreCase("0") && !data.getBusLatitude().equalsIgnoreCase("")) {
                restaurant = new LatLng(new Double(data.getBusLatitude()).doubleValue(), new Double(data.getBusLongitude()).doubleValue());
            } else {
                Toast.makeText(context, "Can't fetch route, restaurant address not found", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!AppPreference.getCurLat(context).equalsIgnoreCase("0") && !AppPreference.getCurLat(context).equalsIgnoreCase("")) {
                source = new LatLng(new Double(AppPreference.getCurLat(context)).doubleValue(), new Double(new Double(AppPreference.getCurLong(context)).doubleValue()));
            } else {
                Toast.makeText(context, "Can't fetch route, current location not found", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!data.getCustLatitude().equalsIgnoreCase("0") && !data.getCustLatitude().equalsIgnoreCase("")) {
                customer = new LatLng(new Double(data.getCustLatitude()).doubleValue(), new Double(data.getCustLongitude()).doubleValue());
            } else {
                Toast.makeText(context, "Can't fetch route, customer address not found", Toast.LENGTH_SHORT).show();
                return;
            }

            //driver to restaurant
            waypoints.add(restaurant.latitude + "," + restaurant.longitude);
            String url = Functions.getDirectionsUrlWaypont(source, restaurant, waypoints);
            RoutesDownloadTask downloadTask = new RoutesDownloadTask(DriverHomeActivity.this);
            downloadTask.execute(url);


            //restaurant to customer
            waypoints1.add(customer.latitude + "," + customer.longitude);
            String url1 = Functions.getDirectionsUrlWaypont(restaurant, customer, waypoints);
            RoutesDownloadTask1 downloadTask1 = new RoutesDownloadTask1(DriverHomeActivity.this);
            downloadTask1.execute(url1);


        }
    }

    private void initDeliveryView() {
        delivery_view = findViewById(R.id.delivery_view);
        llCustDetails = findViewById(R.id.llCustDetails);
        tvHideShow = findViewById(R.id.tvHideShow);
        tvDeliveryId = findViewById(R.id.tvDeliveryId);
        tvName = findViewById(R.id.tvName);
        tvPhoneNo = findViewById(R.id.tvPhoneNo);
        tvAddress = findViewById(R.id.tvAddress);
        delivery_view.setVisibility(View.VISIBLE);
        delivery_view.setOnClickListener(this);
    }


    private void setUpFused() {
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        MapsInitializer.initialize(this);
    }

    @SuppressLint("RestrictedApi")
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(10);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DriverHomeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            showExitAlert();
        }
    }

    private void showExitAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_menus, menu);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to logout?").setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        if (Functions.isNetworkAvailable(context))
                            logoutDriverApi();
                        else
                            Toast.makeText(context, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();

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
            case R.id.nav_driver_deliveries:
                startActivity(new Intent(context, NewDeliveryActivity.class));
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(final GoogleMap maps) {
        map = maps;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        setCurrentLocationMarker();
      /*  map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.e("checkSelfPermission", false + "");
                    return;
                }
                map.setMyLocationEnabled(true);

                setCurrentLocationMarker();

                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            }
        });*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.e(TAG, "startLocationUpdates: Started");
    }

    private void setCurrentLocationMarker() {
        LatLng currentLatLng = new LatLng(new Double(AppPreference.getCurLat(context)).doubleValue(), new Double(AppPreference.getCurLong(context)).doubleValue());
        Log.e(TAG, "setCurrentLocationMarker >> " + currentLatLng.latitude + "::" + currentLatLng.longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.custom_map_scooter_icon)));
        markerOptions.title("Current Location");
        markerOptions.snippet("This is your current location");
        markerCurrent = map.addMarker(markerOptions);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: " + connectionResult.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        Log.e(TAG, "stopLocationUpdates: Stopped");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        AppPreference.setCurLat(this, String.valueOf(location.getLatitude()));
        AppPreference.setCurLong(this, String.valueOf(location.getLongitude()));
        try {
            Log.e(TAG, "onLocationChanged : " + location.getLatitude() + " : " + location.getLongitude());
            if (markerCurrent != null) {
                animateMarker(markerCurrent, new LatLng(location.getLatitude(), location.getLongitude()), false);
            }
            /*if (markerCurrent == null) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
                markerOptions.title("I am here");
                markerCurrent = map.addMarker(markerOptions);
            } else {
                animateMarker(markerCurrent, new LatLng(location.getLatitude(), location.getLongitude()), false);
            }*/
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = map.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1000;

        final LinearInterpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.marker_icon);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delivery_view:
                hideShowDeliveryView();
                break;

        }
    }

    private void hideShowDeliveryView() {
        if (llCustDetails.getVisibility() == View.VISIBLE) {
            llCustDetails.setVisibility(View.GONE);
            tvHideShow.setText("Show Details");
        } else {
            llCustDetails.setVisibility(View.VISIBLE);
            tvHideShow.setText("Hide Details");
        }

    }

    public class RoutesDownloadTask extends AsyncTask<String, Void, String> {

        Context context;
        String distanceTime;

        public RoutesDownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);
        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.connect();

                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            } catch (Exception e) {
                //Log.d("Exception while downloading url", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

            // Parsing the data in non-ui thread
            @Override
            protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;
                Log.d("routes", "111");

                try {

                    jObject = new JSONObject(jsonData[0]);
                    DirectionsJSONParser parser = new DirectionsJSONParser();

                    // Starts parsing data
                    routes = parser.parse(jObject);
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                return routes;
            }

            // Executes in UI thread, after the parsing process
            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> result) {
                PolylineOptions lineOptions = null;
                ArrayList<LatLng> points = null;
                String distance = "";
                String duration = "1";
                String durationValue = "1";
                String distanceValue = "0";

                try {
                    if (result.size() < 1) {
                        //Toast.makeText(ParserTask.this, "No Points", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NullPointerException e) {
                    return;
                }
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    if (i == 1) {
                        lineOptions = new PolylineOptions();
                        points = new ArrayList<LatLng>();
                        // Fetching i-th route
                        List<HashMap<String, String>> path = result.get(i);
                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            if (j == 0) {    // Get distance from the list
                                distance = point.get("distance");
                                distanceValue = point.get("distance");
                                continue;
                            } else if (j == 1) { // Get duration from the list
                                duration = point.get("duration");
                                durationValue = point.get("value");
                                Log.d("duration ss", duration + "::" + durationValue);
                                continue;
                            } else if (j == 2) { // Get duration from the list

                                continue;
                            }

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            //Log.d("routes",position.toString());
                            points.add(position);
                            com.google.android.gms.maps.model.LatLng mapPoint =
                                    new com.google.android.gms.maps.model.LatLng(lat, lng);
                            builder.include(mapPoint);

                        }
                        lineOptions.addAll(points);

                        lineOptions.width(6);

                        lineOptions.color(Color.RED);
                        lineOptions.geodesic(true);

                        // if(polylineFinal.isGeodesic())
                        //  polylineFinal.remove();
                        map.addPolyline(lineOptions);
                        //mMap.setPadding(0, measuredHeight/2, 0, 0);

                        map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));

                        // mMap.setPadding(0, cardView.getHeight(), 0, 0);
                    }
                }

               /* // Start marker
                MarkerOptions options = new MarkerOptions();
                options.position(resturantLatlong);
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_m));
                map.addMarker(options);*/

                //        // End marker

                /* MarkerOptions options2 = new MarkerOptions();
                options2.position(points.get(points.size() - 1));
                options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_c));
                map.addMarker(options2);*/

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(points.get(points.size() - 1));
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.custom_map_rest_icon)));
                markerOptions.title("Restaurant's Location");
                markerOptions.snippet("This is restaurant's Location");
                map.addMarker(markerOptions);

            }
        }

    }

    public class RoutesDownloadTask1 extends AsyncTask<String, Void, String> {

        Context context;
        String distanceTime;

        public RoutesDownloadTask1(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);
        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.connect();

                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            } catch (Exception e) {
                //Log.d("Exception while downloading url", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

            // Parsing the data in non-ui thread
            @Override
            protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;
                Log.d("routes", "111");

                try {

                    jObject = new JSONObject(jsonData[0]);
                    DirectionsJSONParser parser = new DirectionsJSONParser();

                    // Starts parsing data
                    routes = parser.parse(jObject);
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
                return routes;
            }

            // Executes in UI thread, after the parsing process
            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> result) {
                PolylineOptions lineOptions = null;
                ArrayList<LatLng> points = null;
                String distance = "";
                String duration = "1";
                String durationValue = "1";
                String distanceValue = "0";

                try {
                    if (result.size() < 1) {
                        //Toast.makeText(ParserTask.this, "No Points", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NullPointerException e) {
                    return;
                }
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    if (i == 1) {
                        lineOptions = new PolylineOptions();
                        points = new ArrayList<LatLng>();
                        // Fetching i-th route
                        List<HashMap<String, String>> path = result.get(i);
                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            if (j == 0) {    // Get distance from the list
                                distance = point.get("distance");
                                distanceValue = point.get("distance");
                                continue;
                            } else if (j == 1) { // Get duration from the list
                                duration = point.get("duration");
                                durationValue = point.get("value");
                                Log.d("duration ss", duration + "::" + durationValue);
                                continue;
                            } else if (j == 2) { // Get duration from the list

                                continue;
                            }

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            //Log.d("routes",position.toString());
                            points.add(position);
                            com.google.android.gms.maps.model.LatLng mapPoint =
                                    new com.google.android.gms.maps.model.LatLng(lat, lng);
                            builder.include(mapPoint);

                        }
                        lineOptions.addAll(points);

                        lineOptions.width(6);

                        lineOptions.color(Color.BLUE);
                        lineOptions.geodesic(true);

                        // if(polylineFinal.isGeodesic())
                        //  polylineFinal.remove();
                        map.addPolyline(lineOptions);
                        //mMap.setPadding(0, measuredHeight/2, 0, 0);

                        map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));

                        // mMap.setPadding(0, cardView.getHeight(), 0, 0);
                    }
                }

               /* // Start marker
                MarkerOptions options = new MarkerOptions();
                options.position(resturantLatlong);
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_m));
                map.addMarker(options);*/

                //        // End marker

                /* MarkerOptions options2 = new MarkerOptions();
                options2.position(points.get(points.size() - 1));
                options2.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_c));
                map.addMarker(options2);*/

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(points.get(points.size() - 1));
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.custom_map_cust_icon)));
                markerOptions.title("Customer's Location");
                markerOptions.snippet("This is customer's location");
                map.addMarker(markerOptions);

            }
        }
    }


    class updateDriverLogTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.DRIVER_METHODS.UPDATELOG);
            jsonObject.addProperty("driver_id", FirebaseInstanceId.getInstance().getToken());
            jsonObject.addProperty("fcm_id", FirebaseInstanceId.getInstance().getToken());
            jsonObject.addProperty("driver_lat", "");
            jsonObject.addProperty("driver_long", "");

            Log.e(TAG, "updateDriverLogTask request >>>>>" + jsonObject.toString());

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);

            Call<JsonObject> call = apiService.drivers_url(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "updateDriverLogTask reponse>>>>>> " + response.body().toString());
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsString().equals("200")) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                    } else {
                        JsonObject result = jsonObject.getAsJsonObject("result");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("Response: onFailure", t.toString());
                }
            });
            return null;
        }

    }
    private void logoutDriverApi() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.DRIVER_METHODS.LOGOUTDRIVER);
        jsonObject.addProperty(AppConstants.KEY_USER_ID, AppPreference.getUserid(context));
        Log.e(TAG, "logoutDriverApi: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.login_url(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "logoutDriverApi: Response >> " + resp);
                try {

                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        AppPreference.clearPreference(context);
                        startActivity(new Intent(context, LoginActivity.class));
                        finish();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "logoutDriverApi error :- " + Log.getStackTraceString(t));
                Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
