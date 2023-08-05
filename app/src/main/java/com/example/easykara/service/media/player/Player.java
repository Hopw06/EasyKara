package com.example.easykara.service.media.player;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.easykara.common.PlayStatus;

import javax.inject.Inject;

// in case need to switch to another player.
public interface Player {

    void play(Context context, String uri);

    void pause();

    void resume();

    void release();

    LiveData<Integer> getCurrentPos();

    LiveData<PlayStatus> getPlayStatus();

    void setPos(Integer pos);

    void seekTo(int pos);
}
