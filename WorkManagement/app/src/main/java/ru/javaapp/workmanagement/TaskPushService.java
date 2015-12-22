package ru.javaapp.workmanagement;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.javaapp.workmanagement.activities.worker.TaskListActivity;
import ru.javaapp.workmanagement.workDB.Transmission;

/**
 * Created by ildar2244 on 21.12.2015.
 */
public class TaskPushService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public TaskPushService() {
        super("TaskPushService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String sessionKey = intent.getStringExtra("sessionKey");
        while (true) {
            createNotification(sessionKey);
            try {
                // Через каждые 10 минут
                Thread.sleep(10 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    protected void createNotification(String sessionKey) {

        Transmission response = new Transmission();
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(response.getPushForWorker(sessionKey)));
            Log.d("service", "" + jsonObject);
            JSONArray jsonArray = jsonObject.getJSONArray("pushNewTasks_by_worker");
            Log.d("service", "" + jsonArray);

            String alert = jsonArray.getJSONObject(0).getString("name");

            int icon = R.mipmap.ic_launcher;
            CharSequence tickerText = this.getString(R.string.new_notification);

            Intent i2 = new Intent(this, TaskListActivity.class);
            i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), i2, 0);

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Notification notification = mBuilder.setSmallIcon(icon).setTicker(tickerText).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(tickerText)
                    .setStyle(inboxStyle)
                    .setContentIntent(pIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSubText(alert)
                    .setContentText(alert)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("service", "ServicePush.Exception: " + e);
        }
    }
}
