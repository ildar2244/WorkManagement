package ru.javaapp.workmanagement;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import ru.javaapp.workmanagement.workDB.Transmission;

/**
 * Created by ildar2244 on 21.12.2015.
 */
public class TaskPushService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TaskPushService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String sessionKey = intent.getStringExtra("sessionKey");
        while (true) {
            createNotification(sessionKey);
        }

    }

    protected void createNotification(String sessionKey) {

        Transmission response = new Transmission();
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(response.getPushForWorker(sessionKey)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
