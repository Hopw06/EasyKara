package com.example.easykara.view.fragments.playscreen;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.easykara.MainActivity;
import com.example.easykara.R;
import com.example.easykara.common.PlayStatus;
import com.example.easykara.entity.Media;
import com.example.easykara.service.media.PlayerService;
import com.example.easykara.viewmodel.PlayViewModel;
import com.masoudss.lib.WaveformSeekBar;

public class PlayScreenFragment extends Fragment {

    private final String TAG = getClass().getName();

    private MainActivity mActivity;
    private PlayerService mPlayer;
    private PlayViewModel mPlayViewModel;

    // view
    private Button mPlayBtn;
    private WaveformSeekBar mWaveformSeekBar;

    // state
    private float mCurrentWaveProgress;
    private PlayStatus mPlayStatus = PlayStatus.IDLE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPlayViewModel = new ViewModelProvider(this).get(PlayViewModel.class);

        initObserver();
    }

    private void initObserver() {
        mPlayViewModel.getAmplites().observe(getViewLifecycleOwner(), samples -> {
            if (mWaveformSeekBar != null) {
                mWaveformSeekBar.setSample(samples);
            }
        });

        mPlayViewModel.getDuration().observe(getViewLifecycleOwner(), duration -> {
            if (mWaveformSeekBar != null) {
                mWaveformSeekBar.setMaxProgress(duration);
            }
        });

        mPlayViewModel.getCurrentPlayingMedia().observe(getViewLifecycleOwner(), media -> {
            mPlayViewModel.loadWaveForm(media);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initPlayerMonitor();
        mPlayViewModel.getLastPlayedMedia();
    }

    private void initView(View view) {
        mWaveformSeekBar = view.findViewById(R.id.waveForm);
        mWaveformSeekBar.setProgress(mCurrentWaveProgress);
        mWaveformSeekBar.setOnClickListener(v -> {
            int progress = (int) mWaveformSeekBar.getProgress() * 1000;
            mPlayer.seekTo(progress);
        });
        mPlayBtn = view.findViewById(R.id.playSample);
        setPlayBtnState(mPlayStatus);
        mPlayBtn.setOnClickListener(v -> {
            play();
        });
    }

    private void initPlayer() {
        if (mActivity == null || mPlayer == null) {
            mActivity = (MainActivity) getActivity();
            if (mActivity != null) {
                Log.i(TAG, "playSampleMp3: " + mActivity.getClass().getName());
                mPlayer = mActivity.getPlayer();
                if (mPlayer == null) {
                    Log.i(TAG, "playSampleMp3: " + "player null");
                    return;
                }
                Log.i(TAG, "playSampleMp3: player = " + mPlayer.getClass().getName());
            }
        }
    }

    private void initPlayerMonitor() {
        if (mPlayer != null) {
            mPlayer.getCurrentPosition().observe(getViewLifecycleOwner(), pos -> {
                Log.i(TAG, "initProgressMonitor: progress = " + pos);
                if (mWaveformSeekBar != null) {
                    mWaveformSeekBar.setProgress(pos);
                    mCurrentWaveProgress = pos;
                }
            });

            mPlayer.getPlayStatus().observe(getViewLifecycleOwner(), playStatus -> {
                mPlayStatus = playStatus;
                setPlayBtnState(playStatus);
            });
        }
    }

    private void setPlayBtnState(PlayStatus playStatus) {
        if (mPlayBtn == null) return;
        switch (playStatus) {
            case IDLE:
            case PLAYED:
                mPlayBtn.setEnabled(true);
                break;
            case PLAYING:
                mPlayBtn.setEnabled(false);
                break;
            default:
                break;
        }
    }

    private void play() {
        initPlayer();
        Media media = mPlayViewModel.getCurrentPlayingMedia().getValue();
        if (media != null && !TextUtils.isEmpty(media.getUri())) {
            initPlayerMonitor();
            mPlayer.play(requireContext().getApplicationContext(), media.getUri());
            mPlayBtn.setEnabled(false);
        }
    }
}