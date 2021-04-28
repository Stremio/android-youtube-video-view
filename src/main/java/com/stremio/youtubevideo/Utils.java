package com.stremio.youtubevideo;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

final class Utils {

    private static Handler sMainHandler;

    static String streamToString(final InputStream inputStream) {
        String result = "";

        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final List<String> buffer = new LinkedList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.add(line);
            }
            result = TextUtils.join("\n", buffer);
            reader.close();
        } catch(final IOException e) {
        }

        return result;
    }

    static void runOnUiThread(final Runnable runnable) {
        synchronized (Utils.class) {
            if (sMainHandler == null) {
                sMainHandler = new Handler(Looper.getMainLooper());
            }
        }

        sMainHandler.post(runnable);
    }

}
