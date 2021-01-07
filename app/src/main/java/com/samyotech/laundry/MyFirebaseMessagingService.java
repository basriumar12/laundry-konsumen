package com.samyotech.laundry;
/**
 * Created by VARUN on 01/01/19.
 */

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.preferences.SharedPrefrence;
import com.samyotech.laundry.ui.activity.Dashboard;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String MyPREFERENCES = "MyPrefs";
    private static final String TAG = "MyFirebaseMsgService";
    SharedPrefrence prefrence;
    int i = 0;
    SharedPreferences sharedpreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        prefrence = SharedPrefrence.getInstance(this);

        Log.e("Aashu", "From: " + remoteMessage.getData());
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        }

        sendNotification(remoteMessage.getNotification().getBody(), "title", remoteMessage.getNotification().getTitle());
/*
        if (remoteMessage.getData() != null) {
            if (remoteMessage.getData().containsKey("title")) {
                if (remoteMessage.getData().get("title").equalsIgnoreCase("Job")) {
                    sendNotification(getValue(remoteMessage.getData(), "body"), 1);
                } else if (remoteMessage.getData().get("title").equalsIgnoreCase("Chat")) {
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(Consts.BROADCAST);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                    i = prefrence.getIntValue("Value");
                    i++;
                    prefrence.setIntValue("Value", i);

                    sendNotification(getValue(remoteMessage.getData(), "body"), 1);
                }else {
                    sendNotification(getValue(remoteMessage.getData(), "body"), 0);
                }
            }

        }
*/

        if (remoteMessage.getData() != null) {
            if (remoteMessage.getData().containsKey(Consts.TYPE)) {
                if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.CHATNOTIFICATION)) {

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(Consts.BROADCAST);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.CHATNOTIFICATION, getValue(remoteMessage.getData(), "title"));
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.BROADCASTNOTIFICATION)) {

                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.BROADCASTNOTIFICATION, getValue(remoteMessage.getData(), "title"));
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.ORDERNOTIFICATION)) {

                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.ORDERNOTIFICATION, getValue(remoteMessage.getData(), "title"));
                } else if (remoteMessage.getData().get(Consts.TYPE).equalsIgnoreCase(Consts.TICKETNOTIFICATION)) {

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(Consts.BROADCAST);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

                    sendNotification(getValue(remoteMessage.getData(), "body"), Consts.TICKETNOTIFICATION, getValue(remoteMessage.getData(), "title"));
                } else {
                    sendNotification(getValue(remoteMessage.getData(), "body"), "", "");
                }
            }

        }/*else {
            sendNotification(getValue(remoteMessage.getData(), "body"), "");
        }*/

    }

    public String getValue(Map<String, String> data, String key) {
        try {
            if (data.containsKey(key))
                return data.get(key);
            else
                return getString(R.string.app_name);
        } catch (Exception ex) {
            ex.printStackTrace();
            return getString(R.string.app_name);
        }
    }

    @Override
    public void onNewToken(String token) {
//        token= FirebaseInstanceId.getInstance().getToken();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Consts.DEVICE_TOKEN, token);
        editor.apply();
        SharedPreferences userDetails = MyFirebaseMessagingService.this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPrefrence prefrence = SharedPrefrence.getInstance(getApplicationContext());
        prefrence.setValue(Consts.DEVICE_TOKEN, token);
        Log.d(TAG, "Refreshed token: " + token);

    }

    private void sendNotification(final String messageBody, String tag, String title) {

        Intent intent = new Intent(this, Dashboard.class);
        intent.putExtra(Consts.SCREEN_TAG, tag);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);


        }
        manager.notify(0, builder.build());

    }


}

