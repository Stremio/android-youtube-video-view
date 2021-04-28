package com.stremio.youtubevideo;

import android.webkit.JavascriptInterface;

final class PublicJavascriptInterface {

    private final YouTubeEventListener mEventListener;

    PublicJavascriptInterface(final YouTubeEventListener eventListener) {
        mEventListener = eventListener;
    }

    @JavascriptInterface
    public void onError(final String message) {
        mEventListener.onError(message);
    }

    @JavascriptInterface
    public void onTimeChanged(final int time) {
        mEventListener.onTimeChanged(time);
    }

    @JavascriptInterface
    public void onEndReached() {
        mEventListener.onEndReached();
    }

    @JavascriptInterface
    public void onPlaying(final int duration) {
        mEventListener.onPlaying(duration);
    }

    @JavascriptInterface
    public void onPaused() {
        mEventListener.onPaused();
    }

    @JavascriptInterface
    public void onSeekPerformed() {
        mEventListener.onSeekPerformed();
    }

    @JavascriptInterface
    public void onSeekRequested(final int time) {
        mEventListener.onSeekRequested(time);
    }

    @JavascriptInterface
    public void onBuffering(final int buffering) {
        mEventListener.onBuffering(buffering);
    }

    @JavascriptInterface
    public void onReady() {
        mEventListener.onReady();
    }

}
