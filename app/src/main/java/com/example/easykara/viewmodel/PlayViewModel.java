package com.example.easykara.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.easykara.config.application.EasyKaraApplication;
import com.example.easykara.entity.Media;
import com.example.easykara.service.realm.MediaRealmService;
import com.example.easykara.utils.CollectionUtils;
import com.example.easykara.utils.ConvertorUtils;

import java.io.File;
import java.util.List;

import linc.com.amplituda.Amplituda;
import linc.com.amplituda.AmplitudaResult;
import linc.com.amplituda.exceptions.io.AmplitudaIOException;

public class PlayViewModel extends AndroidViewModel {
    private static final String TAG = "PlayViewModel";

    // service
    private Amplituda mAmplituda;
    public MediaRealmService mMediaRealmService;

    // data
    private MutableLiveData<Long> mDuration;
    private MutableLiveData<int[]> mAmplites;
    private MutableLiveData<Media> mCurrentPlayingMedia;

    public PlayViewModel(@NonNull Application application) {
        super(application);
        EasyKaraApplication easyKaraApplication = (EasyKaraApplication) application;
        mAmplituda = easyKaraApplication.createAmplituda();

        mDuration = new MutableLiveData<>();
        mAmplites = new MutableLiveData<>();
        mCurrentPlayingMedia = new MutableLiveData<>();

        mMediaRealmService = easyKaraApplication.createMediaRealmService();
        Media initMedia = mMediaRealmService.initAudio(application, mAmplituda);
        mCurrentPlayingMedia.setValue(initMedia);
    }

    public LiveData<int[]> getAmplites() {
        return mAmplites;
    }

    public MutableLiveData<Long> getDuration() {
        return mDuration;
    }

    public MutableLiveData<Media> getCurrentPlayingMedia() {
        return mCurrentPlayingMedia;
    }

    public void getLastPlayedMedia() {
        new Thread(() -> {
            Media media = mMediaRealmService.getLastPlayedMedia();
            Media currentPlayingMedia = mCurrentPlayingMedia.getValue();

            if (currentPlayingMedia == null || (media != null && !media.getUri().equalsIgnoreCase(currentPlayingMedia.getUri()))) {
                mCurrentPlayingMedia.postValue(media);
            }
        }).start();
    }

    public void loadWaveForm(Media media) {
        if (media == null || TextUtils.isEmpty(media.getUri())) return;
        if (CollectionUtils.isEmpty(media.getAmplitudesData())) {
            File file = new File(media.getUri());
            loadWaveForm(file);
        } else {
            mDuration.setValue(media.getDuration());
            mAmplites.setValue(ConvertorUtils.toIntArray(media.getAmplitudesData()));
        }
    }

    public void loadWaveForm(File file) {
        new Thread(() -> mAmplituda.processAudio(file)
                .get(result -> {
                    Log.i(TAG, "loadWaveForm: " + Thread.currentThread().getName());
                    List<Integer> amplitudesData = result.amplitudesAsList();
                    long duration = result.getAudioDuration(AmplitudaResult.DurationUnit.SECONDS);

                    mDuration.postValue(duration);
                    mAmplites.postValue(ConvertorUtils.toIntArray(amplitudesData));
                }, exception -> {
                    exception.printStackTrace();
                    if(exception instanceof AmplitudaIOException) {
                        Log.i(TAG, "loadWaveForm: " + exception.getMessage());
                    }
                })).start();
    }
}
