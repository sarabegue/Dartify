package com.mentit.dartify.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mentit.dartify.Tasks.Chat.GetMessagesTask;
import com.mentit.dartify.Tasks.Notification.GetNotificationsTask;

public class GetMessagesService extends IntentService {
    private static Context context;
    private boolean busy = false;

    public GetMessagesService() {
        super("");
    }

    public GetMessagesService(String name) {
        super(GetMessagesService.class.getName());
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("Get Messages Service", "On Handle Intent");

        new GetMessagesTask(context).execute();
        new GetNotificationsTask(context).execute();
    }

    public void onDestroy() {
        Log.d("Get Messages Service", "On Destroy");
        if (busy) {
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Get Messages Service", "On Bind");
        return null;
    }
}
