package com.example.easykara.repository.media;

import com.example.easykara.common.FieldConst;
import com.example.easykara.entity.Media;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MediaRealmRepositoryImpl implements MediaRealmRepository {

    @Override
    public void insert(Media media) {
        new Thread(() -> {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(r -> {
                r.insert(media);
            });
        }).start();
    }

    @Override
    public List<Media> listAll() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Media> rs = realm.where(Media.class)
                .findAll();
        return realm.copyFromRealm(rs);
    }

    @Override
    public Media findByUri(String uri) {
        Realm realm = Realm.getDefaultInstance();
        Media media = realm.where(Media.class)
                .equalTo(FieldConst.URI, uri)
                .findFirst();
        return media != null ? realm.copyFromRealm(media) : null;
    }

    @Override
    public Media getLastPlayedMedia() {
        Realm realm = Realm.getDefaultInstance();
        Media media = realm.where(Media.class)
                .sort(FieldConst.LAST_PLAYED_DATE, Sort.DESCENDING)
                .findFirst();
        return media != null ? realm.copyFromRealm(media) : null;
    }
}
