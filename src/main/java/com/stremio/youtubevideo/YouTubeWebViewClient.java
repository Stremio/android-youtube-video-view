package com.stremio.youtubevideo;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

final class YouTubeWebViewClient extends WebViewClient {

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(final WebView webView, final String url) {
        final Uri uri = Uri.parse(url);
        return shouldOverrideUrlLoading(webView, uri);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(final WebView webView, final WebResourceRequest request) {
        final Uri uri = request.getUrl();
        return shouldOverrideUrlLoading(webView, uri);
    }

    private boolean shouldOverrideUrlLoading(final WebView webView, final Uri uri) {
        return false;
    }

}
