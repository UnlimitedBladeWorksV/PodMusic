package com.example.ag.podmusic.base;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.ag.podmusic.service.MusicService;

/**
 * Created by Ag on 2018/3/23.
 */

public class RairApp extends Application {

    private static RairApp app;
    private MusicService mService;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((MusicService.LocalBinder) service).getService();
            mService.setContext(getApplicationContext());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    public static RairApp getApp() {
        return app;
    }

    public MusicService getService() {
        return mService;
    }
}
