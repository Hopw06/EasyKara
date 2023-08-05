package com.example.easykara.view.fragments.medialistscreen.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.easykara.common.MediaListTab;
import com.example.easykara.view.fragments.medialistscreen.MediaListFragment;

public class MediaScreenPagerAdapter extends FragmentStateAdapter {

    private MediaListTab[] mTabs;

    public MediaScreenPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public void setTabs(MediaListTab[] tabs) {
        this.mTabs = tabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new MediaListFragment(mTabs[position]);
    }

    @Override
    public int getItemCount() {
        return mTabs.length;
    }
}
