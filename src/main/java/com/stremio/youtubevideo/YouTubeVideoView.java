package com.stremio.youtubevideo;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import java.io.InputStream;
import java.text.MessageFormat;

public final class YouTubeVideoView extends FrameLayout {

    private static final String PUBLIC_JAVASCRIPT_INTERFACE_NAME = "PublicAndroidBridge";
    private static final String INTERNAL_JAVASCRIPT_INTERFACE_NAME = "InternalAndroidBridge";
    private static final String HTML_MIME_TYPE = "text/html; charset=utf-8";
    private static final String HTML_ENCODING = "UTF-8";
    private static final String LOAD_MEDIA_COMMAND_FORMAT = "loadMedia(''{0}'', {1}, {2})";
    private static final String SEEK_COMMAND_FORMAT = "seek({0})";
    private static final String PAUSE_COMMAND = "pause()";
    private static final String PLAY_COMMAND = "play()";

    private boolean mReady;
    private String mPendingMediaCommand;
    private final WebView mWebView;
    private final Object mInternalJavascriptInterface = new Object() {

        @JavascriptInterface
        public void onReady() {
            mReady = true;
            if (mPendingMediaCommand != null) {
                YouTubeVideoView.this.evaluateJavascript(mPendingMediaCommand);
                mPendingMediaCommand = null;
            }
        }

    };

    public YouTubeVideoView(final Context context, final YouTubeEventListener eventListener) {
        super(context);

        mWebView = new WebView(context);
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mWebView, layoutParams);

        mWebView.addJavascriptInterface(mInternalJavascriptInterface, INTERNAL_JAVASCRIPT_INTERFACE_NAME);

        final PublicJavascriptInterface publicJavascriptInterface = new PublicJavascriptInterface(eventListener);
        mWebView.addJavascriptInterface(publicJavascriptInterface, PUBLIC_JAVASCRIPT_INTERFACE_NAME);

        final WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(false);
        settings.setAllowFileAccess(false);
        settings.setAppCacheEnabled(false);
        settings.setGeolocationEnabled(false);
        settings.setDisplayZoomControls(false);
        settings.setDatabaseEnabled(false);
        settings.setSupportMultipleWindows(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }

        final YouTubeWebViewClient webViewClient = new YouTubeWebViewClient();
        mWebView.setWebViewClient(webViewClient);

        final InputStream iFrameHTMLRaw = getResources().openRawResource(R.raw.youtube_iframe);
        final String iFrameHTML = Utils.streamToString(iFrameHTMLRaw);
        mWebView.loadData(iFrameHTML, HTML_MIME_TYPE, HTML_ENCODING);
    }

    public void loadMedia(final String videoId, final int startTime, final boolean autoplay) {
        final String loadMediaCommand = MessageFormat.format(LOAD_MEDIA_COMMAND_FORMAT, videoId, String.valueOf(startTime), autoplay);
        if (mReady) {
            evaluateJavascript(loadMediaCommand);
        } else {
            mPendingMediaCommand = loadMediaCommand;
        }
    }

    public void play() {
        if (!mReady) {
            return;
        }

        evaluateJavascript(PLAY_COMMAND);
    }

    public void pause() {
        if (!mReady) {
            return;
        }

        evaluateJavascript(PAUSE_COMMAND);
    }

    public void seek(final int time) {
        if (!mReady) {
            return;
        }

        final String seekCommand = MessageFormat.format(SEEK_COMMAND_FORMAT, String.valueOf(time));
        evaluateJavascript(seekCommand);
    }

    private void evaluateJavascript(final String javascriptString) {
        Utils.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mWebView.evaluateJavascript(javascriptString, null);
                } else {
                    final String url = MessageFormat.format("javascript:{0}", javascriptString);
                    mWebView.loadUrl(url);
                }
            }

        });
    }

}
