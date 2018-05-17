package com.dinehawaiipartner.Services;

import android.annotation.SuppressLint;
import android.util.Log;

import com.dinehawaiipartner.Util.AppConstants;
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
            if (remoteMessage.getData().size() > 0) {
                String defaultMessage = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.DEFAULT_MESSAGE);
                String DRIVER_NEW_TRIP = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.DRIVER_NEW_TRIP);
            }
            JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());


        } catch (JSONException e)

        {
            e.printStackTrace();
        }

    }

}
