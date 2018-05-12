package com.dinehawaiipartner.Services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by hvantage3 on 6/30/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";


    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
       // AppPreferences.setTOKENID(this, token);
       // sendTokenOnServer(token);
    }

  /*  private void sendTokenOnServer(String s) {
        if (Util.isNetworkAvailable(this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.FCMUPDATE.SENFCMTOKEN);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(this));
            jsonObject.addProperty("fcm_id", s);
            Log.e(TAG, jsonObject.toString());
            JsonCallMethod(jsonObject);
        }
    }

    @SuppressLint("LongLogTag")
    private void JsonCallMethod(JsonObject jsonObject) {
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUrl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "<<<FCM RESPONSE>>>", response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
*/
}

