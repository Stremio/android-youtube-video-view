package com.stremio.youtubevideo;

public interface YouTubeEventListener {

    void onError(String message);

    void onTimeChanged(int time);

    void onEndReached();

    void onPlaying(int duration);

    void onPaused();

    void onSeekPerformed();

    void onSeekRequested(int time);

    void onBuffering(int buffering);

    void onReady();

}
