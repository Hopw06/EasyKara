package com.example.easykara.service.media.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.easykara.common.PlayStatus;

import java.io.IOException;

import javax.inject.Inject;

public class MediaPlayerImpl implements Player {

    private final String TAG = getClass().getName();

    private final MediaPlayer mp;

    private Thread mProgressMonitor;
    private MediatorLiveData<Integer> mProgress;
    private MediatorLiveData<PlayStatus> mPlayStatus;


    @Inject
    public MediaPlayerImpl() {
        mp = new MediaPlayer();
        mProgress = new MediatorLiveData<>();
        mPlayStatus = new MediatorLiveData<>();
        mPlayStatus.setValue(PlayStatus.IDLE);
    }

    @Override
    public void play(Context context, String uri) {
        mp.reset();
        try {
            mp.setDataSource(context, Uri.parse(uri));
            mp.prepareAsync();
            mp.setOnPreparedListener(mp -> {
                Log.i(TAG, "Duration: " +  mp.getDuration());
                initProgressMonitor();
                mPlayStatus.setValue(PlayStatus.PLAYING);
                mp.setVolume(100f, 100f);
                mp.setLooping(false);
                mp.start();
            });

            mp.setOnCompletionListener(mp -> {
                mPlayStatus.setValue(PlayStatus.PLAYED);
                Log.i(TAG, "onCompletion: " + uri);
                if (mp != null) {
                    mp.reset();
                    destroyProgressMonitor();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void release() {
        mp.reset();
        mp.release();
        destroyProgressMonitor();
    }

    @Override
    public void seekTo(int pos) {
        if (mp.isPlaying()) {
            mp.seekTo(pos);
        }
    }


    @Override
    public LiveData<Integer> getCurrentPos() {
        return mProgress;
    }

    @Override
    public LiveData<PlayStatus> getPlayStatus() {
        return mPlayStatus;
    }

    @Override
    public void setPos(Integer pos) {
        this.mProgress.setValue(pos);
    }

    private void initProgressMonitor() {
        mProgressMonitor = new Thread(() -> {
            Log.i(TAG, "initProgressMonitor: " +"begin while loop: " + mp.isPlaying());
            while (mp.isPlaying()) {
                try {
                    Integer currentPos = mp.getCurrentPosition() / 1000;
                    Log.i(TAG, "initProgressMonitor: " + currentPos);
                    mProgress.postValue(currentPos);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        mProgressMonitor.start();
    }

    private void destroyProgressMonitor() {
        if (mProgressMonitor != null && mProgressMonitor.isAlive()) {
            mProgressMonitor.interrupt();
            mProgressMonitor = null;
        }
    }
}
