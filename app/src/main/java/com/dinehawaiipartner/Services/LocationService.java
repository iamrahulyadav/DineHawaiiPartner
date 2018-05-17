package com.dinehawaiipartner.Services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.dinehawaiipartner.Retrofit.ApiClient;
import com.dinehawaiipartner.Retrofit.MyApiEndpointInterface;
import com.dinehawaiipartner.Util.AppConstants;
import com.dinehawaiipartner.Util.AppPreference;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LocationService extends Service implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationService";
    private static final long INTERVAL = 3 * 60 * 1000;
    private static final long FASTEST_INTERVAL = 3 * 60 * 1000;
    public static Location mCurrentLocation;
    Context mContext;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    String mLastUpdateTime;
    private double latitude;
    private double longitude;

    public LocationService() {
    }

    public LocationService(Context mContext) {
        this.mContext = mContext;

        if (mGoogleApiClient == null) {
            createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

        }
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        Log.e(TAG, "Location start: ");
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(TAG, "Location Permission: ");
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.e(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (mCurrentLocation != null) {
            Log.e(TAG, "onChange >> Lat: " + location.getLatitude() + ", Long: " + location.getLongitude());
            AppPreference.setCurLat(mContext, String.valueOf(location.getLatitude()));
            AppPreference.setCurLong(mContext, String.valueOf(location.getLongitude()));
            new updateDriverLog().execute();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class updateDriverLog extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.DRIVER_METHODS.UPDATELOG);
            jsonObject.addProperty("driver_id", AppPreference.getUserid(mContext));
            jsonObject.addProperty("fcm_id", FirebaseInstanceId.getInstance().getToken());
            jsonObject.addProperty("driver_lat", AppPreference.getCurLat(mContext));
            jsonObject.addProperty("driver_long", AppPreference.getCurLong(mContext));

            Log.e(TAG, "updateDriverLog: Request >>" + jsonObject.toString());

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);

            Call<JsonObject> call = apiService.drivers_url(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "updateDriverLog: Response >> " + response.body().toString());
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsString().equals("200")) {
                    } else {
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


//    private void sendLocation() {
//        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        new AsyncTask<String, Void, String>() {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//                String result = "";
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    JSONObject data = new JSONObject();
//                    data.put("method", "update_GeoLocation_FCM");
//                    data.put("driver_id", AppPreferences.getUserId(mContext));
//                    data.put("order_id", "");
//                    data.put("customer_id", "");
//                    data.put("fcm_token_id", FirebaseInstanceId.getInstance().getToken());
//                    data.put("current_latitude", String.valueOf(mCurrentLocation.getLatitude()));
//                    data.put("current_longitude", String.valueOf(mCurrentLocation.getLongitude()));
//                    data.put("gps", Function.isGPSEnabled(mContext));
//                    data.put("batterystatus", Function.getBatteryLevel(mContext));
//
//                    Log.e("Request sendLocation", data.toString());
//                    //json.put("notification", dataJson);
//                    RequestBody body = RequestBody.create(JSON, data.toString());
//                    Request request = new Request.Builder()
//                            .url(AppConstant.BASEURL.URL + AppConstant.ENDPOINT.UPDATE_FCM)
//                            .post(body)
//                            .build();
//                    okhttp3.Response response = client.newCall(request).execute();
//                    result = response.body().string();
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    FirebaseCrash.report(e);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    FirebaseCrash.report(e);
//                }
//                return result;
//            }
//
//            @Override
//            protected void onPostExecute(String finalResponse) {
//                super.onPostExecute(finalResponse);
//                Log.e("Response sendLocation", finalResponse);
//                String s = finalResponse;
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//
//                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
//
//                    } else if (jsonObject.get("status").equals("400")) {
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    FirebaseCrash.report(e);
//                }
//            }
//        }.execute("", "");
//       /* JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty(AppConstant.KEY_METHOD, AppConstant.METHOD.SIGNUP);
//        jsonObject.addProperty(AppConstant.KEY_USERNAME, editTextName.getText().toString());
//        jsonObject.addProperty(AppConstant.KEY_MOBILE_NO, editTextMobileNo.getText().toString());
//        jsonObject.addProperty(AppConstant.KEY_PASSWORD, editTextConfirmPassword.getText().toString());
//        jsonObject.addProperty(AppConstant.KEY_FCM_ID, fcm_id);
//
//        Log.v(TAG, "Json Request signup :- " + jsonObject);
//        signUptask(jsonObject);*/
//    }

}
