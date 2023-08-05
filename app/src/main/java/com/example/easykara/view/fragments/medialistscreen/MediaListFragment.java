package com.example.easykara.view.fragments.medialistscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.easykara.R;
import com.example.easykara.common.MediaListTab;
import com.example.easykara.viewmodel.MediaViewModel;

public class MediaListFragment extends Fragment {

    private MediaListTab mTab;
    private MediaViewModel mMediaViewModel;


    public MediaListFragment() {
    }

    public MediaListFragment(MediaListTab tab) {
        mTab = tab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_list, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMediaViewModel = new ViewModelProvider(this).get(MediaViewModel.class);
        initObserver();
        initData();
    }

    private void initView(View view) {

    }

    private void initObserver() {
        switch (mTab) {
            case ALL:
                mMediaViewModel.getMedias().observe(getViewLifecycleOwner(), medias -> {
                    System.out.println(medias);
                });
            case RECENT:
                break;
            case LOVED:
                break;
        }
    }

    private void initData() {
        switch (mTab) {
            case ALL:
                mMediaViewModel.getAllMedia();
                break;
            case RECENT:
                break;
            case LOVED:
                break;
        }
    }
}