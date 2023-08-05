package com.example.easykara.view.fragments.medialistscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.easykara.R;
import com.example.easykara.common.MediaListTab;
import com.example.easykara.view.fragments.medialistscreen.adapter.MediaScreenPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MediaScreenFragment extends Fragment {

    private static final MediaListTab[] tabs = MediaListTab.values();

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_screen, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view) {
        mViewPager2 = view.findViewById(R.id.pager);
        mTabLayout = view.findViewById(R.id.tab_layout);

        MediaScreenPagerAdapter adapter = new MediaScreenPagerAdapter(this);
        adapter.setTabs(tabs);
        mViewPager2.setAdapter(adapter);

        new TabLayoutMediator(
                mTabLayout,
                mViewPager2,
                (tab, position) -> {
                    tab.setText(getTabTitle(position));
                }
        ).attach();
    }

    private String getTabTitle(int position) {
        switch (tabs[position]) {
            case ALL:
                return "All";
            case RECENT:
                return "Recent";
            case LOVED:
                return "Loved";
        }
        return "";
    }
}