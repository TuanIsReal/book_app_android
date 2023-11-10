package com.anhtuan.bookapp.firebase_cloud_mess;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.anhtuan.bookapp.R;
import com.anhtuan.bookapp.config.BookApplication;
import com.anhtuan.bookapp.fragment.NotificationFragment;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        RemoteMessage.Notification notification = message.getNotification();

        if (notification == null){
            return;
        }

        String title = notification.getTitle();
        String bodyMess = notification.getBody();


        sendNotification(title, bodyMess);
    }


    private void sendNotification(String title, String bodyMess) {
        Intent intent = new Intent(this, NotificationFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, BookApplication.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(bodyMess)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent);

        Notification notification = notificationBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null){
            notificationManager.notify(1, notification);
        }
    }
}
