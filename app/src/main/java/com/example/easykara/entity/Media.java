package com.example.easykara.entity;

import com.example.easykara.common.FieldConst;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;

public class Media extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    @RealmField(FieldConst.FILE_NAME)
    private String fileName;

    @RealmField(FieldConst.URI)
    private String uri;

    @RealmField(FieldConst.THUMBNAIL_URL)
    private String thumbnailUrl;

    @RealmField(FieldConst.DURATION)
    private Long duration;

    @RealmField(FieldConst.AMPLITUDES_DATA)
    private RealmList<Integer> amplitudesData;

    @RealmField(FieldConst.LOVED)
    private Boolean loved;

    @RealmField(FieldConst.CONTENT_TYPE)
    private String contentType; // AUDIO, VIDEO

    @RealmField(FieldConst.CREATED_DATE)
    private Date createdDate;

    @RealmField(FieldConst.LAST_PLAYED_DATE)
    private Date lastPlayedDate;

    @RealmField(FieldConst.LAST_MODIFIED_DATE)
    private Date lastModifiedDate;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public RealmList<Integer> getAmplitudesData() {
        return amplitudesData;
    }

    public void setAmplitudesData(RealmList<Integer> amplitudesData) {
        this.amplitudesData = amplitudesData;
    }

    public Boolean getLoved() {
        return loved;
    }

    public void setLoved(Boolean loved) {
        this.loved = loved;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastPlayedDate() {
        return lastPlayedDate;
    }

    public void setLastPlayedDate(Date lastPlayedDate) {
        this.lastPlayedDate = lastPlayedDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Media() {
    }

    public Media(String fileName, String uri,
                 String thumbnailUrl, Long duration,
                 RealmList<Integer> amplitudesData, Boolean loved,
                 String contentType, Date createdDate, Date lastPlayedDate,
                 Date lastModifiedDate) {
        this.fileName = fileName;
        this.uri = uri;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
        this.amplitudesData = amplitudesData;
        this.loved = loved;
        this.contentType = contentType;
        this.createdDate = createdDate;
        this.lastPlayedDate = lastPlayedDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
