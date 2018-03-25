package com.example.ag.podmusic.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.ag.podmusic.bean.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {


    private final IBinder mBinder = new LocalBinder();
    private Context context;

    private MediaPlayer mMediaPlayer;
    private int currentTime = 0;    //当前时间
    private int currentItem = -1;   //当前歌曲
    private ArrayList<Music> songs;

    @Override
    public void onCreate() {
        super.onCreate();
        if(mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void playMusic(String path){
        try{
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    nextMusic();
                }
            });
        }catch (IOException e){

        }
    }

    //下一首
    public void nextMusic(){
        if(++currentItem >= songs.size()){
            currentItem = 0;
        }
        playMusic(songs.get(currentItem).getPath());
    }

    //上一首
    public void prevMusic(){
        if (--currentItem < 0) {
            currentItem = songs.size() - 1;
        }
        playMusic(songs.get(currentItem).getPath());
    }

    //当前播放进度
    public int getCurrent(){
        if(mMediaPlayer.isPlaying()){
            return mMediaPlayer.getCurrentPosition();
        }else{
            return currentTime;
        }
    }

    //跳到某某进度
    public void movePlay(int progress){
        mMediaPlayer.seekTo(progress);
        currentTime = progress;
    }

    //歌曲是否播放
    public boolean isPlay(){
        return mMediaPlayer.isPlaying();
    }

    //暂停或播放歌曲
    public void pausePlay(){
        if (mMediaPlayer.isPlaying()){
            currentTime = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
        }else{
            mMediaPlayer.start();
        }
    }

    public String getSongName(){
        return songs.get(currentItem).getName();
    }

    public String getAlbum(){
        return songs.get(currentItem).getAlbum();
    }

    public String getAlbumPic(){
        return songs.get(currentItem).getPic();
    }

    public String getSingerName(){
        return songs.get(currentItem).getSinger();
    }

    public void setContext(Context context){
        this.context = context;
    }

    public MediaPlayer getmMediaPlayer(){
        return mMediaPlayer;
    }

    public void setmMediaPlayer(MediaPlayer mMediaPlayer){
        this.mMediaPlayer = mMediaPlayer;
    }

    public int getCurrentItem(){
        return currentItem;
    }

    public void setCurrentItem(int currentListItme) {
        this.currentItem = currentListItme;
    }

    public int getDuration(){
        return mMediaPlayer.getDuration();
    }

    public List<Music> getSongs(){
        return songs;
    }

    public void setSongs(ArrayList<Music> songs){
        this.songs = songs;
    }

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
