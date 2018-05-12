package com.dinehawaiipartner.Services;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String messageBody = "", order_id = "", reserv_id = "";

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.e(TAG, "Notification Data >> " + remoteMessage.getData());
            JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("notification_data");

        } catch (JSONException e)

        {
            e.printStackTrace();
        }

    }

}
