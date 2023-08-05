package com.example.easykara.config.application;

import android.app.Application;

import com.example.easykara.service.realm.MediaRealmService;
import com.example.easykara.service.realm.MediaRealmServiceImpl;

import dagger.hilt.android.HiltAndroidApp;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import linc.com.amplituda.Amplituda;

@HiltAndroidApp
public class EasyKaraApplication extends Application {

    private Amplituda mAmplituda;
    private MediaRealmService mMediaRealmService;

    @Override
    public void onCreate() {
        super.onCreate();
        configRealm();
    }

    private void configRealm() {
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("easykara.db")
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(false)
                .compactOnLaunch()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public synchronized Amplituda createAmplituda() {
        if (mAmplituda == null) {
            mAmplituda = new Amplituda(this);
        }
        return mAmplituda;
    }

    public synchronized MediaRealmService createMediaRealmService() {
        if (mMediaRealmService == null) {
            mMediaRealmService = new MediaRealmServiceImpl();
        }
        return mMediaRealmService;
    }
}
