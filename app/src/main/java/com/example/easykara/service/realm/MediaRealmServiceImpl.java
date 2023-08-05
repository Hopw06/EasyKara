package com.example.easykara.service.realm;

import android.content.Context;

import com.example.easykara.common.ContentType;
import com.example.easykara.entity.Media;
import com.example.easykara.repository.media.MediaRealmRepository;
import com.example.easykara.repository.media.MediaRealmRepositoryImpl;
import com.example.easykara.utils.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import linc.com.amplituda.Amplituda;
import linc.com.amplituda.AmplitudaResult;

public class MediaRealmServiceImpl implements MediaRealmService{

    private static final String INIT_AUDIO_FILE = "init_sample_20411228179756956830824711882219.mp3";


    private final MediaRealmRepository mediaRealmRepo;

    public MediaRealmServiceImpl() {
        this.mediaRealmRepo = new MediaRealmRepositoryImpl();
    }

    @Override
    public Media initAudio(Context context, Amplituda amplituda) {
        Media media = null;
        InputStream inputStream = FileUtils.getFileInputStreamFromAsset(context, INIT_AUDIO_FILE);
        if (inputStream != null) {
            File file = FileUtils.createFileFromInputStream(context, inputStream, INIT_AUDIO_FILE);
            if (file != null) {
                media = mediaRealmRepo.findByUri(file.getAbsolutePath());
                if (media == null) {
                    media = new Media();
                    media.setFileName(INIT_AUDIO_FILE);
                    media.setUri(file.getAbsolutePath());
                    Media finalMedia = media;
                    amplituda.processAudio(file).get(result -> {
                        List<Integer> amplitudesData = result.amplitudesAsList();
                        long duration = result.getAudioDuration(AmplitudaResult.DurationUnit.SECONDS);
                        RealmList<Integer> list = new RealmList<Integer>();
                        list.addAll(amplitudesData);
                        finalMedia.setAmplitudesData(list);
                        finalMedia.setDuration(duration);
                    }, Throwable::printStackTrace);
                    finalMedia.setCreatedDate(new Date());
                    finalMedia.setLastPlayedDate(new Date());
                    finalMedia.setContentType(ContentType.AUDIO.name());
                    mediaRealmRepo.insert(finalMedia);
                }
            }
        }
        return media;
    }

    @Override
    public Media getLastPlayedMedia() {
        return mediaRealmRepo.getLastPlayedMedia();
    }

    @Override
    public void insert(Media media) {
        mediaRealmRepo.insert(media);
    }

    @Override
    public List<Media> listAll() {
        return mediaRealmRepo.listAll();
    }
}
