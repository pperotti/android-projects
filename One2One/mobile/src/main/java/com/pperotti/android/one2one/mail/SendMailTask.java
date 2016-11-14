package com.pperotti.android.one2one.mail;

import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pperotti.android.one2one.R;
import com.pperotti.android.one2one.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SendMailTask extends AsyncTask {

    public static final String TAG = SendMailTask.class.getSimpleName();
    HashMap<String, String> destinataries = new HashMap<>();
    private Task task;
    private Context context;
    private int notificationId;
    private String notificationTitle;

    private static final boolean USEMOCK = false;

    public SendMailTask(Context context, Task task) {
        this.task = task;
        this.context = context;

        Log.d(TAG, "Body=>" + task.getContent());
    }

    @Override
    protected Object doInBackground(Object... args) {

        Random r = new Random();
        notificationId = r.nextInt();

        notificationTitle = context.getString(R.string.notification_title, getDate());

        filterContactsByGroup();

        //TODO: Remove OR comment out
        if (USEMOCK) {
            addMockData();
        }

        sendEmails();

        return null;
    }

    private void addMockData() {
        for (int i=0;i<50;i++) {
            destinataries.put(String.valueOf(i), String.valueOf(i));
        }
    }

    private void filterContactsByGroup() {
        Cursor cursor = null;
        try {

            String[] projection = {
                    "_id",
                    "display_name",
                    "contact_id",
                    "lookup"
            };

            //This query will let me check which contacts belong to the selected group
            Uri uri = ContactsContract.Data.CONTENT_URI;
            cursor = context.getContentResolver().query(uri, projection,
                    ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " = ?",
                    new String[]{String.valueOf(task.getGroupId())},
                    null);

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String displayName = cursor.getString(1);
                String contactId = cursor.getString(2);
                String lookupKey = cursor.getString(3);

                Log.d(TAG, " displayName=" + displayName + " contactId=" + contactId + " lookupKey=" + lookupKey);

                processContact(contactId, lookupKey, displayName);

                cursor.moveToNext();
            }
            cursor.close();


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void processContact(String id, String lookupKey, String name) {
        Log.d(TAG, "NAME=" + name + " ID=" + id + " LOOKUPKEY=" + lookupKey);

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    new String[]{
                            ContactsContract.CommonDataKinds.Email.ADDRESS
                    },
                    ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                            + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'",
                    new String[]{String.valueOf(id)}, null);

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                String email = cursor.getString(0);
                Log.d(TAG, "--> EMAIL=" + email);

                //Add email to destinatary list
                destinataries.put(name, email);

                cursor.moveToNext();
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void sendEmail(String name, String email) {
        try {
            Log.i(TAG, "About to instantiate Gmail...");

            //String from = "paul.perotti.dev@gmail.com";
            //String pass = "TheOneAndOnlyPerotti";
            String from = "desdeiglesia@gmail.com";
            String pass = "p4r4n01c02";
            List<String> to = new ArrayList<>();
            to.add(email);
            String subject = task.getSubject();
            String body = task.getContent();

            Gmail androidEmail = new Gmail(
                    from,
                    pass,
                    to,
                    subject,
                    body);
            androidEmail.createEmailMessage();
            //TODO: Uncomment
            if (!USEMOCK) {
                androidEmail.sendEmail();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void sendEmails() {
        Set<String> names = destinataries.keySet();
        int i = 1, total = names.size();
        for (String name : names) {
            String email = destinataries.get(name);

            String text = String.format("Sending email %d/%d to: %s (%s)", i++, total, email, name);
            Log.d(TAG, text);
            sendEmail(name, email);
            publishProgress(text);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private String getDate() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        int minute = c.get(Calendar.MINUTE);

        return c.get(Calendar.HOUR_OF_DAY) + ":" + ((minute < 10 ) ? "0" + minute : minute);
    }

    @Override
    public void onProgressUpdate(Object... values) {
        Log.d(TAG, "onProgressUpdate: ");

        String text = (String) values[0];

        //Update Notification
        updateNotification(false, text);
    }

    @Override
    public void onPostExecute(Object result) {
        updateNotification(true, context.getString(R.string.notification_completion));
    }

    private void updateNotification(boolean isCompleted, String text) {

        int icon = android.R.drawable.stat_sys_upload;
        if (isCompleted) {
            icon = android.R.drawable.stat_sys_upload_done;
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(icon)
                        .setContentTitle(notificationTitle)
                        .setContentText(text);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

}
