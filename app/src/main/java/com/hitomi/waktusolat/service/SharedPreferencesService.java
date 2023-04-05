package com.hitomi.waktusolat.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class SharedPreferencesService extends Service {

    private final Context context;

    public SharedPreferencesService(Context context) {
        this.context = context;
    }

    public void setZon(int zon) {
        SharedPreferences prefs = context.getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("zon", zon);
        editor.apply();
    }

    public int getZon() {
        SharedPreferences prefs = context.getSharedPreferences("preference", Context.MODE_PRIVATE);
        return prefs.getInt("zon", -1);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}