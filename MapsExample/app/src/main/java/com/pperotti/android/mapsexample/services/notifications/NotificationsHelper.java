package com.pperotti.android.mapsexample.services.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.pperotti.android.mapsexample.R;

public class NotificationsHelper {

    public static final int NOTIFICATION_DOWNLOAD_ID = 0x00010000;
    public static final String CHANNEL_ID = "com.pperotti.android.mapsexample.channel_id";

    /**
     * Present the notification to use when a download is completed.
     *
     * @param context
     */
    public static final void presentDownloadCompletedNotification(Context context) {

        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel(context);
        }
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.btn_plus)
                        .setContentTitle(context.getString(R.string.notification_download_title))
                        .setContentText(context.getString(R.string.notification_download_text))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setStyle(new NotificationCompat.BigTextStyle());

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_DOWNLOAD_ID, builder.build());
    }

    private static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notification_download_channel_name);
            String description = context.getString(R.string.notification_download_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
