package com.example.easykara.service.realm;

import android.content.Context;

import com.example.easykara.entity.Media;

import java.util.List;

import linc.com.amplituda.Amplituda;

public interface MediaRealmService {

    Media initAudio(Context context, Amplituda amplituda);

    Media getLastPlayedMedia();

    void insert(Media media);

    List<Media> listAll();
}
