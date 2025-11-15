package com.emillics.lemon.base.listener;

import com.emillics.lemon.model.Video;

public interface SpiderEventListener {
    void onWebLog(String message);

    void onVideoSelected(Video video);
}
