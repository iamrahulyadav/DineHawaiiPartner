package com.dinehawaiipartner.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dinehawaiipartner.Activity.Driver.NewDeliveryActivity;
import com.dinehawaiipartner.Activity.Manager.MCompletedOrderActivity;
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
            String DRIVER_NEW_DELIVERY = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.DRIVER_NEW_DELIVERY);
            String MANAGER_NEW_DELIVERY = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.MANAGER_NEW_DELIVERY);
            String MANAGER_DELIVERY_COMPLETED = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.MANAGER_DELIVERY_COMPLETED);
            String MANAGER_DELIVERY_PICKEDUP = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.MANAGER_DELIVERY_PICKEDUP);

            if (!DRIVER_NEW_DELIVERY.equalsIgnoreCase("null")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(DRIVER_NEW_DELIVERY);
                    Intent intent = new Intent(this, NewDeliveryActivity.class);
                    sendNewTripNotification(intent, jsonObject.getString("order_id"), jsonObject.getString("msg"), jsonObject.getString("delivery_adddress"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (!MANAGER_NEW_DELIVERY.equalsIgnoreCase("null")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(MANAGER_NEW_DELIVERY);
                    Intent intent = new Intent(this, VendorHomeActivity.class);
                    sendNewTripNotification(intent, jsonObject.getString("order_id"), jsonObject.getString("msg"), jsonObject.getString("delivery_adddress"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (!MANAGER_DELIVERY_COMPLETED.equalsIgnoreCase("null")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(MANAGER_DELIVERY_COMPLETED);
                    Intent intent = new Intent(this, MCompletedOrderActivity.class);
                    sendNotification(intent, jsonObject.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (!MANAGER_DELIVERY_PICKEDUP.equalsIgnoreCase("null")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(MANAGER_DELIVERY_PICKEDUP);
                    Intent intent = new Intent(this, VendorHomeActivity.class);
                    sendNotification(intent, jsonObject.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendNewTripNotification(Intent intent, String delivery_id, String msg, String address) {
//        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.car_alarm);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Delivery Id: #" + delivery_id + "\nAddress: " + address);
        bigText.setBigContentTitle(msg);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(msg)
                .setContentIntent(notificationPendingIntent)
                .setContentText("Delivery Id: " + delivery_id)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.DEFAULT_VIBRATE)
                .setSound(defaultSoundUri)
                .setStyle(bigText)
                .setAutoCancel(true)
                .setFullScreenIntent(notificationPendingIntent, true)
                .setPriority(NotificationManager.IMPORTANCE_HIGH);


        final Notification notification = builder.build();

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(1, notification);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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

    private void sendNotification(Intent intent, String message) {
        try {
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(getResources().getString(R.string.app_name))
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
