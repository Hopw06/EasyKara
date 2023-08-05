package com.example.easykara.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.easykara.config.application.EasyKaraApplication;
import com.example.easykara.entity.Media;
import com.example.easykara.service.realm.MediaRealmService;

import java.util.List;

import linc.com.amplituda.Amplituda;

public class MediaViewModel extends AndroidViewModel {
    private static final String TAG = "MediaViewModel";

    private Amplituda mAmplituda;

    private MediaRealmService mediaRealmSv;

    private MediatorLiveData<List<Media>> medias;

    public MediaViewModel(@NonNull Application application) {
        super(application);

        medias = new MediatorLiveData<>();

        EasyKaraApplication easyKaraApplication = (EasyKaraApplication) application;
        mediaRealmSv = easyKaraApplication.createMediaRealmService();
        mAmplituda = easyKaraApplication.createAmplituda();
    }

    public void insertMedia(Media media) {
        mediaRealmSv.insert(media);
    }

    public void getAllMedia() {
        medias.setValue(mediaRealmSv.listAll());
    }

    public LiveData<List<Media>> getMedias() {
        return medias;
    }
}
