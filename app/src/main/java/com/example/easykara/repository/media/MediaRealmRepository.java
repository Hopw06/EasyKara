package com.example.easykara.repository.media;

import com.example.easykara.entity.Media;

import java.util.List;

public interface MediaRealmRepository {

    void insert(Media media);

    List<Media> listAll();

    Media findByUri(String uri);

    Media getLastPlayedMedia();
}
