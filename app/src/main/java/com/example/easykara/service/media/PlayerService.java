package com.example.easykara.service.media;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.easykara.common.PlayStatus;
import com.example.easykara.service.media.player.MediaPlayerImpl;
import com.example.easykara.service.media.player.Player;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

public class PlayerService extends Service {
    private static final String TAG = "PlayerService";

    public Player player;
    private final IBinder mBinder = new LocalBinder();

    public PlayerService() {
        this.player = new MediaPlayerImpl();
        Log.i(TAG, "PlayerService: " + player.getClass().getName());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.release();
        return super.onUnbind(intent);
    }

    public void play(Context context, String uri) {
        this.player.play(context, uri);
    }

    public void pause() {
        this.player.pause();
    }

    public void resume() {
        this.player.resume();
    }

    public void seekTo(int pos) {
        this.player.seekTo(pos);
    }

    public void release() {
        this.player.release();
    }

    public LiveData<Integer> getCurrentPosition() {
        return player.getCurrentPos();
    }

    public LiveData<PlayStatus> getPlayStatus() {
        return player.getPlayStatus();
    }

    public class LocalBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }
}
