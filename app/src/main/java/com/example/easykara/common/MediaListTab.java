package com.example.easykara.common;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MediaListTab {
    ALL("All"),
    RECENT("RECENT"),
    LOVED("LOVED");

    private String mTab;

    MediaListTab(String tab) {
        this.mTab = tab;
    }

    public String getTab() {
        return mTab;
    }

    public List<String> getTabs() {
        return Arrays.stream(values())
                .map(MediaListTab::getTab).collect(Collectors.toList());
    }
}
