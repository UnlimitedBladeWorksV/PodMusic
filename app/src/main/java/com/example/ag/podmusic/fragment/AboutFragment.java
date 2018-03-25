package com.example.ag.podmusic.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ag.podmusic.MainActivity;
import com.example.ag.podmusic.R;
import com.example.ag.podmusic.base.RairApp;
import com.example.ag.podmusic.constant.Constants;
import com.example.ag.podmusic.service.MusicService;

public class AboutFragment extends Fragment {

    private AboutReceiver receiver;
    private LocalBroadcastManager broadcastManager;
    private Handler handler;
    private MusicService musicService;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    private void initView() {
        register();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        musicService.pausePlay();
                        break;
                    case 1:
                        if (musicService.isPlay())
                            musicService.prevMusic();
                        break;
                    case 2:
                        if (musicService.isPlay())
                            musicService.nextMusic();
                        break;
                    case 3:
                        MainActivity activity = (MainActivity) getActivity();
                        activity.showFragment(1);
                        break;
                    case 4:
                        musicService.pausePlay();
                        break;
                }
            }
        };
    }


    private void register() {
        musicService = RairApp.getApp().getService();
        receiver = new AboutReceiver();
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_SEND);
        broadcastManager = LocalBroadcastManager.getInstance(getContext());
        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    class AboutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra(Constants.ACTION_KEY);
            Message message = Message.obtain(handler);
            switch (key) {
                case Constants.ACTION_OK:
                    message.what = 0;
                    break;
                case Constants.ACTION_PREV:
                    message.what = 1;
                    break;
                case Constants.ACTION_NEXT:
                    message.what = 2;
                    break;
                case Constants.ACTION_MENU:
                    message.what = 3;
                    break;
                case Constants.ACTION_PLAY:
                    message.what = 4;
                    break;
            }
            handler.sendMessage(message);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) broadcastManager.unregisterReceiver(receiver);
        else register();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        handler = null;
        broadcastManager.unregisterReceiver(receiver);
    }

}
