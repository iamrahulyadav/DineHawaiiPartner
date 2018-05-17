package com.dinehawaiipartner.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dinehawaiipartner.Activity.Driver.NewDeliveryActivity;
import com.dinehawaiipartner.Activity.Manager.VendorHomeActivity;
import com.dinehawaiipartner.R;
import com.dinehawaiipartner.Util.AppConstants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FbMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "Notification Data >> " + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0) {
            String DEFAULT_MESSAGE = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.DEFAULT_MESSAGE);
            String DRIVER_NEW_DELIVERY = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.DRIVER_NEW_DELIVERY);
            String MANAGER_NEW_DELIVERY = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.MANAGER_NEW_DELIVERY);
            String MANAGER_DELIVERY_COMPLETED = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.MANAGER_DELIVERY_COMPLETED);
            String MANAGER_DELIVERY_PICKEDUP = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.MANAGER_DELIVERY_PICKEDUP);

            if (!DEFAULT_MESSAGE.equalsIgnoreCase("null")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(DEFAULT_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (!DRIVER_NEW_DELIVERY.equalsIgnoreCase("null")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(DRIVER_NEW_DELIVERY);
                    Intent intent = new Intent(this, NewDeliveryActivity.class);
                    setNewTripNotification(intent, "New Delivery");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (!MANAGER_NEW_DELIVERY.equalsIgnoreCase("null")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(MANAGER_NEW_DELIVERY);
                    Intent intent = new Intent(this, VendorHomeActivity.class);
                    setNewTripNotification(intent, "New Delivery");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendDefaultNotification(Intent intent, String message) {
        try {
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("BINITALL")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo))
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(AppConstants.NOTIFICATION_ID.DEFAULT, notificationBuilder.build());
        } catch (Exception e) {
            Log.e("Notification Ex", e.getMessage());
        }
    }

    private void setNewTripNotification(Intent intent, String message) {
        try {
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("BINITALL")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo))
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(AppConstants.NOTIFICATION_ID.DRIVER_NEW_DELIVERY, notificationBuilder.build());
        } catch (Exception e) {
            Log.e("Notification Ex", e.getMessage());
        }
    }

}
