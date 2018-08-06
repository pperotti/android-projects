package com.pperotti.android.mapsexample.services.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.pperotti.android.mapsexample.R;
import com.pperotti.android.mapsexample.domain.routes.Route;
import com.pperotti.android.mapsexample.domain.routes.RouteState;

public class NotificationsHelper {

    public static final String CHANNEL_ID = "com.pperotti.android.mapsexample.channel_id";

    /**
     * Present or update the notification depending on the state.
     *
     * @param context Any context object
     * @param route The Route object used.
     */
    public static void presentNotification(Context context, Route route) {

        String title = null;
        String text= null;

        RouteState state = route.getState();

        if (RouteState.NOT_DOWNLOADED.equals(state)) {
            title = context.getString(R.string.notification_download_failed_title, route.getName());
            text = context.getString(R.string.notification_download_failed_text);
        } else if (RouteState.PARSING.equals(state)) {
            title = context.getString(R.string.notification_parsing_title, route.getName());
            text = context.getString(R.string.notification_parsing_text);
        } else if (RouteState.PARSING_FAILED.equals(state)) {
            title = context.getString(R.string.notification_parsing_failed_title, route.getName());
            text = context.getString(R.string.notification_parsing_failed_text);
        } else if (RouteState.PARSED.equals(state)) {
            title = context.getString(R.string.notification_parsed_title, route.getName());
            text = context.getString(R.string.notification_parsed_text);
        } else if (RouteState.PROCESSING.equals(state)) {
            title = context.getString(R.string.notification_processing_title, route.getName());
            text = context.getString(R.string.notification_processing_text);
        } else if (RouteState.PROCESSED.equals(state)) {
            title = context.getString(R.string.notification_processed_title);
            text = context.getString(R.string.notification_processed_text);
        } else if (RouteState.PROCESSING_FAILED.equals(state)) {
            title = context.getString(R.string.notification_processing_title, route.getName());
            text = context.getString(R.string.notification_processing_text);
        } else if (RouteState.ENQUEUED.equals(state)) {
            //Nothing to display here since this notification is handled by Android.
        }

        //Display the notification
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)) {
            Notification notification = createNotification(context, title, text)
                    .setProgress(0,0,false).build();
            presentNotification(context, route.getRouteId(), notification);
        }
    }

    /**
     * Update the notification with the proper progress for the route specified.
     *
     * @param context Context object
     * @param route Route object whose notification is intended to be updated.
     * @param progress Current progress to update.
     * @param maxValue Max value to use in the notification.
     */
    public static void updateNotification(Context context, Route route, int progress, int maxValue) {
        String title = null;
        String text= null;

        RouteState state = route.getState();

        if (RouteState.PARSING.equals(state)) {
            title = context.getString(R.string.notification_parsing_title, route.getName());
            text = context.getString(R.string.notification_parsing_text);
        } else if (RouteState.PROCESSING.equals(state)) {
            title = context.getString(R.string.notification_processing_title, route.getName());
            text = context.getString(R.string.notification_processing_text);
        } else {
            //Nothing to display here it is not relevant to other states
        }

        //Display the notification
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)) {
            Notification notification = createInProgressNotification(
                    context,
                    title,
                    text,
                    progress,
                    maxValue).build();
            presentNotification(context, route.getRouteId(), notification);
        }
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

    private static final NotificationCompat.Builder createNotification(Context context,
                                                                       String title,
                                                                       String text) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel(context);
        }
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.btn_plus)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setStyle(new NotificationCompat.BigTextStyle());

        return builder;
    }

    private static final NotificationCompat.Builder createInProgressNotification(Context context,
                                                                                 String title,
                                                                                 String text,
                                                                                 int progress,
                                                                                 int maxValue) {
        NotificationCompat.Builder builder = createNotification(context, title, text);
        builder.setProgress(maxValue, progress, false);
        return builder;
    }

    private static void presentNotification(Context context, long routeId, Notification notification) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //The (int) casting is irrelevant because it is used only for the notification identifier.
        notificationManager.notify((int) routeId, notification);
    }

}
